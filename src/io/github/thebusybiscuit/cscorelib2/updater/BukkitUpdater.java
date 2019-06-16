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

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.Setter;

public class BukkitUpdater implements Updater {
	
	private static final String api_url = "https://api.curseforge.com/servermods/files?projectIds=";
	private static final char[] blacklist = "abcdefghijklmnopqrstuvwxyz-+_ ()[]{}".toCharArray();
	private static final String[] development_builds = {"DEV", "EXPERIMENTAL", "BETA", "ALPHA", "UNFINISHED"};
	
	private Plugin plugin;
	private int id;
	private URL url;
	private Thread thread;
	private URL download;
	private File file;
	
	@Getter
	private String localVersion;
	private String remoteVersion;
	
	@Setter
	protected int timeout = 5000;
	
	@Setter
	protected UpdateCheck predicate;
	
	public BukkitUpdater(Plugin plugin, File file, int id) {
		this.plugin = plugin;
		this.id = id;
		this.file = file;
		localVersion = plugin.getDescription().getVersion();
		
		this.predicate = (local, remote) -> {
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
		
		// Checking if current Version is a dev-build
		for (String dev: development_builds) {
			if (localVersion.contains(dev)) {
				System.err.println(" ");
				System.err.println("################## - DEVELOPMENT BUILD - ##################");
                System.err.println("You appear to be using an experimental build of " + plugin.getName());
				System.err.println("Version " + localVersion);
				System.err.println(" ");
				System.err.println("Auto-Updates have been disabled. Use at your own risk!");
				System.err.println(" ");
				return;
			}
		}

		localVersion = localVersion.toLowerCase();
		
		// Deleting all unwanted characters
        for (char blocked: blacklist) {
			localVersion = localVersion.replace(String.valueOf(blocked), "");
        }
	}
	
	@Override
	public void start() {
		try {
			this.url = new URL(api_url + id);
			
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				thread = new Thread(new UpdaterTask());
				thread.start();
			});
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public class UpdaterTask implements Runnable {

		@Override
		public void run() {
			if (connect()) {
				try {
					check();
				} 
				catch(NumberFormatException x) {
					System.err.println(" ");
		        	System.err.println("#################### - ERROR - ####################");
					System.err.println("Could not auto-update " + plugin.getName());
					System.err.println("Unrecognized Version: \"" + localVersion + "\"");
					System.err.println("#################### - ERROR - ####################");
					System.err.println(" ");
				}
			}
		}
		
		private boolean connect() {
			try {
			    final URLConnection connection = url.openConnection();
			    connection.setConnectTimeout(timeout);
			    connection.addRequestProperty("User-Agent", "Auto Updater (by TheBusyBiscuit)");
			    connection.setDoOutput(true);

			    final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			    final JsonArray array = (JsonArray) new JsonParser().parse(reader.readLine());
			    if (array.size() == 0) {
				    System.err.println("[CS-CoreLib - Updater] Could not connect to BukkitDev for Plugin \"" + plugin.getName() + "\", is it down?");
				    
				    try {
					    thread.join();
				    } catch (InterruptedException x) {
					    x.printStackTrace();
				    }
				    
				    return false;
			    }
				
			    JsonObject latest = array.get(array.size() - 1).getAsJsonObject();

			    download = traceURL(latest.get("downloadUrl").getAsString().replace("https:", "http:"));
			    remoteVersion = latest.getAsJsonObject().get("name").getAsString();
			    remoteVersion = remoteVersion.toLowerCase();

			    for (char blocked: blacklist) {
			    	remoteVersion = remoteVersion.replace(String.valueOf(blocked), "");
			    }

			    return true;
			} catch (IOException e) {
				System.err.println("[CS-CoreLib - Updater] Could not connect to BukkitDev for Plugin \"" + plugin.getName() + "\", is it down?");
				try {
					thread.join();
				} catch (InterruptedException x) {
					x.printStackTrace();
				}
			    	return false;
			}
	    }
		
		private void check() {
			if (predicate.hasUpdate(localVersion, remoteVersion)) {
				install();
				return;
			}
			else {
				System.out.println("[CS-CoreLib - Updater] " + plugin.getName() + " is up to date!");
				try {
					thread.join();
				} catch (InterruptedException x) {
					x.printStackTrace();
				}
				return;
			}
		}
		

	    private URL traceURL(String location) throws IOException {
	    	HttpURLConnection connection = null;
	    	
	        while (true) {
	        	URL url = new URL(location);
	        	connection = (HttpURLConnection) url.openConnection();

	        	connection.setInstanceFollowRedirects(false);
	            connection.setConnectTimeout(5000);
	            connection.addRequestProperty("User-Agent", "Auto Updater (by mrCookieSlime)");

	            switch (connection.getResponseCode()) {
	                case HttpURLConnection.HTTP_MOVED_PERM:
	               	case HttpURLConnection.HTTP_MOVED_TEMP:
	                    	String loc = connection.getHeaderField("Location");
	                    	location = new URL(new URL(location), loc).toExternalForm();
	                    	continue;
	            }
			
	            break;
	        }
	        
	        return new URL(connection.getURL().toString().replaceAll(" ", "%20"));
	    }

		private void install() {
			System.out.println("[CS-CoreLib - Updater] " + plugin.getName() + " is outdated!");
			System.out.println("[CS-CoreLib - Updater] Downloading " + plugin.getName() + " v" + remoteVersion);
			
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				BufferedInputStream input = null;
				FileOutputStream output = null;
				System.out.println(download.toString());
				try {
				    input = new BufferedInputStream(download.openStream());
				    output = new FileOutputStream(new File("plugins/" + Bukkit.getUpdateFolder(), file.getName()));

				    final byte[] data = new byte[1024];
				    int read;
				    while ((read = input.read(data, 0, 1024)) != -1) {
				    	output.write(data, 0, read);
				    }
				} catch (Exception ex) {
					System.err.println(" ");
					System.err.println("#################### - ERROR - ####################");
					System.err.println("Could not auto-update " + plugin.getName());
					System.err.println("#################### - ERROR - ####################");
					System.err.println(" ");
					ex.printStackTrace();
				} finally {
				    try {
						if (input != null) input.close();
						if (output != null) output.close();
						System.err.println(" ");
						System.err.println("#################### - UPDATE - ####################");
						System.err.println(plugin.getName() + " was successfully updated (" + localVersion + " -> " + remoteVersion + ")");
						System.err.println("Please restart your Server in order to use the new Version");
						System.err.println(" ");
				    } catch (IOException e) {
				    	e.printStackTrace();
				    }
				    try {
					    thread.join();
				    } catch (InterruptedException x) {
					    x.printStackTrace();
				    }
				}
			});
		}
	}

}
