package io.github.thebusybiscuit.cscorelib2.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class BukkitUpdater implements Updater {
	
	private static final String API_URL = "https://api.curseforge.com/servermods/files?projectIds=";
	private static final char[] BLACKLIST = "abcdefghijklmnopqrstuvwxyz-+_ ()[]{}".toCharArray();
	private static final String[] DEV_KEYWORDS = {"DEV", "EXPERIMENTAL", "BETA", "ALPHA", "UNFINISHED"};
	
	private final Plugin plugin;
	private final File file;
	private final int id;
	
	private URL url;
	private Thread thread;
	private URL download;
	
	@Getter
	private String localVersion;
	private String remoteVersion;
	
	@Setter
	protected int timeout = 8000;
	
	@Setter
	protected UpdateCheck predicate;
	
	public BukkitUpdater(@NonNull Plugin plugin, @NonNull File file, int id) {
		this.plugin = plugin;
		this.id = id;
		this.file = file;
		localVersion = plugin.getDescription().getVersion();
		
		predicate = (local, remote) -> {
			if (local.equals(remote)) return false;
			
			String[] localSplit = local.split("\\.");
			String[] remoteSplit = remote.split("\\.");
			
			for (int i = 0; i < remoteSplit.length; i++) {
				if ((localSplit.length - 1) < i) {
					return true;
	        	}
				
				if (Integer.parseInt(localSplit[i]) > Integer.parseInt(remoteSplit[i])) {
	        		return false;
	        	}
				
	        	if (Integer.parseInt(remoteSplit[i]) > Integer.parseInt(localSplit[i])) {
	        		return true;
	        	}
	        }
	        
	        return false;
		};
	}
	
	@Override
	public void start() {
		// Checking if current Version is a dev-build
		for (String dev : DEV_KEYWORDS) {
			if (localVersion.contains(dev)) {
				plugin.getLogger().log(Level.WARNING, " ");
				plugin.getLogger().log(Level.WARNING, "################## - DEVELOPMENT BUILD - ##################");
				plugin.getLogger().log(Level.WARNING, "You appear to be using an experimental build of " + plugin.getName());
				plugin.getLogger().log(Level.WARNING, "Version {0}", localVersion);
				plugin.getLogger().log(Level.WARNING, " ");
				plugin.getLogger().log(Level.WARNING, "Auto-Updates have been disabled. Use at your own risk!");
				plugin.getLogger().log(Level.WARNING, " ");
				return;
			}
		}
		
		localVersion = localVersion.toLowerCase();
		
		// Deleting all unwanted characters
		for (char blocked : BLACKLIST) {
			localVersion = localVersion.replace(String.valueOf(blocked), "");
		}
		
		prepareUpdateFolder();
			
		try {
			url = new URL(API_URL + id);
			
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				thread = new Thread(new UpdaterTask(), "Updater Thread");
				thread.start();
			});
		} catch (MalformedURLException e) {
			plugin.getLogger().log(Level.SEVERE, "Auto-Updater URL is malformed", e);
		}
	}
	
	private class UpdaterTask implements Runnable {

		@Override
		public void run() {
			if (connect()) {
				try {
					check();
				} 
				catch(NumberFormatException x) {
					plugin.getLogger().log(Level.SEVERE, "Could not auto-update " + plugin.getName());
					plugin.getLogger().log(Level.SEVERE, "Unrecognized Version: \"" + localVersion + "\"");
				}
			}
		}
		
		private boolean connect() {
			try {
			    URLConnection connection = url.openConnection();
			    connection.setConnectTimeout(timeout);
			    connection.addRequestProperty("User-Agent", "Auto Updater (by TheBusyBiscuit)");
			    connection.setDoOutput(true);
			    
			    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			    JsonArray array = (JsonArray) new JsonParser().parse(reader.readLine());
			    
			    if (array.size() == 0) {
			    	plugin.getLogger().log(Level.WARNING, "The Auto-Updater could not connect to dev.bukkit.org, is it down?");
				    
				    try {
					    thread.join();
				    } catch (InterruptedException x) {
				    	plugin.getLogger().log(Level.SEVERE, "The Auto-Updater Thread was interrupted", x);
					    Thread.currentThread().interrupt();
				    }
				    
				    return false;
			    }
				
			    JsonObject latest = array.get(array.size() - 1).getAsJsonObject();

			    download = traceURL(latest.get("downloadUrl").getAsString().replace("https:", "http:"));
			    remoteVersion = latest.getAsJsonObject().get("name").getAsString();
			    remoteVersion = remoteVersion.toLowerCase();

			    for (char blocked : BLACKLIST) {
			    	remoteVersion = remoteVersion.replace(String.valueOf(blocked), "");
			    }

			    return true;
			} catch (IOException e) {
				plugin.getLogger().log(Level.WARNING, "Could not connect to github.io, is it down?", e);
				
				try {
					thread.join();
				} catch (InterruptedException x) {
					plugin.getLogger().log(Level.SEVERE, "The Auto-Updater Thread was interrupted", x);
				    Thread.currentThread().interrupt();
				}
				
			    return false;
			}
	    }
		
		private void check() {
			if (predicate.hasUpdate(localVersion, remoteVersion)) {
				install();
			}
			else {
				plugin.getLogger().log(Level.INFO, plugin.getName() + " is up to date!");
				
				try {
					thread.join();
				} catch (InterruptedException x) {
					plugin.getLogger().log(Level.SEVERE, "The Auto-Updater Thread was interrupted", x);
				    Thread.currentThread().interrupt();
				}
			}
		}
		

	    private URL traceURL(String location) throws IOException {
	    	HttpURLConnection connection = null;
	    	
	        while (true) {
	        	connection = (HttpURLConnection) new URL(location).openConnection();

	        	connection.setInstanceFollowRedirects(false);
	            connection.setConnectTimeout(5000);
	            connection.addRequestProperty("User-Agent", "Auto Updater (by mrCookieSlime)");

	            int code = connection.getResponseCode();
	            
	            if (code != HttpURLConnection.HTTP_MOVED_PERM && code != HttpURLConnection.HTTP_MOVED_TEMP) {
	            	break;
	            }
	            else {
	            	String loc = connection.getHeaderField("Location");
                	location = new URL(new URL(location), loc).toExternalForm();
	            }
	        }
	        
	        return new URL(connection.getURL().toString().replace(" ", "%20"));
	    }

		private void install() {
			plugin.getLogger().log(Level.INFO, plugin.getName() + " is outdated!");
			plugin.getLogger().log(Level.INFO, "Downloading " + plugin.getName() + " v" + remoteVersion);
			
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				BufferedInputStream input = null;
				FileOutputStream output = null;
				
				try {
				    input = new BufferedInputStream(download.openStream());
				    output = new FileOutputStream(new File("plugins/" + Bukkit.getUpdateFolder(), file.getName()));

				    byte[] data = new byte[1024];
				    int read;
				    while ((read = input.read(data, 0, 1024)) != -1) {
				    	output.write(data, 0, read);
				    }
				    
				} catch (Exception x) {
					plugin.getLogger().log(Level.SEVERE, "Could not auto-update " + plugin.getName(), x);
				} finally {
				    try {
						if (input != null) input.close();
						if (output != null) output.close();
						
						plugin.getLogger().log(Level.INFO, " ");
						plugin.getLogger().log(Level.INFO, "#################### - UPDATE - ####################");
						plugin.getLogger().log(Level.INFO, plugin.getName() + " was successfully updated (" + localVersion + " -> " + remoteVersion + ")");
						plugin.getLogger().log(Level.INFO, "Please restart your Server in order to use the new Version");
						plugin.getLogger().log(Level.INFO, " ");
				    } catch (IOException e) {
				    	plugin.getLogger().log(Level.SEVERE, "An Error occured while auto-updating \"" + plugin.getName() + "\"", e);
				    }
				    
				    try {
					    thread.join();
				    } catch (InterruptedException x) {
				    	plugin.getLogger().log(Level.SEVERE, "The Auto-Updater Thread was interrupted", x);
					    Thread.currentThread().interrupt();
				    }
				}
			});
		}
	}

}
