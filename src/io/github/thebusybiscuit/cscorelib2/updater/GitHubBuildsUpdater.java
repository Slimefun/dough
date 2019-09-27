package io.github.thebusybiscuit.cscorelib2.updater;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.Setter;

public class GitHubBuildsUpdater implements Updater {
	
	private static final String api_url = "https://thebusybiscuit.github.io/builds/";
	
	private Plugin plugin;
	private URL versionsURL;
	private URL downloadURL;
	private Thread thread;
	private File file;
	
	@Getter
	private String repository;
	
	@Getter
	private String localVersion;
	private String remoteVersion;
	
	@Setter
	protected int timeout = 5000;
	
	@Setter
	protected UpdateCheck predicate;
	
	public GitHubBuildsUpdater(Plugin plugin, File file, String repo) {
		this.plugin = plugin;
		this.file = file;
		this.repository = repo;
		
		localVersion = extractBuild(plugin.getDescription().getVersion());
		
		this.predicate = (local, remote) -> Integer.parseInt(remote) > Integer.parseInt(local);
		
		prepareUpdateFolder();
	}
	
	private String extractBuild(String version) {
		if (version.startsWith("DEV - ")) {
			return version.substring("DEV - ".length()).split(" ")[0];
		}
		
		throw new IllegalArgumentException("Not a valid Development-Build Version: " + version);
	}

	@Override
	public void start() {
		try {
			this.versionsURL = new URL(api_url + getRepository() + "/builds.json");
			
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
					plugin.getLogger().log(Level.SEVERE, "Could not auto-update " + plugin.getName());
					plugin.getLogger().log(Level.SEVERE, "Unrecognized Version: \"" + localVersion + "\"");
				}
			}
		}
		
		private boolean connect() {
			try {
			    final URLConnection connection = versionsURL.openConnection();
			    connection.setConnectTimeout(timeout);
			    connection.addRequestProperty("User-Agent", "Auto Updater (by TheBusyBiscuit)");
			    connection.setDoOutput(true);

			    final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			    final JsonObject obj = (JsonObject) new JsonParser().parse(reader.readLine());
			    if (obj == null) {
				    System.err.println("[CS-CoreLib - Updater] Could not connect to github.io for Plugin \"" + plugin.getName() + "\", is it down?");
				    
				    try {
					    thread.join();
				    } catch (InterruptedException x) {
					    plugin.getLogger().log(Level.SEVERE, "The Auto-Updater Thread was interrupted", x);
					    Thread.currentThread().interrupt();
				    }
				    
				    return false;
			    }
				
			    remoteVersion = String.valueOf(obj.get("last_successful").getAsInt());
			    downloadURL = new URL(api_url + getRepository() + "/" + getRepository().split("/")[1] + "-" + remoteVersion + ".jar");
			    
			    return true;
			} catch (IOException e) {
				plugin.getLogger().log(Level.WARNING, "Could not connect to github.io for Plugin \"" + plugin.getName() + "\", is it down? (" + e.getMessage() + ")");
				
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

		private void install() {
			plugin.getLogger().log(Level.INFO, plugin.getName() + " is outdated!");
			plugin.getLogger().log(Level.INFO, "Downloading " + plugin.getName() + " v" + remoteVersion);
			
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				BufferedInputStream input = null;
				FileOutputStream output = null;
				
				try {
				    input = new BufferedInputStream(downloadURL.openStream());
				    output = new FileOutputStream(new File("plugins/" + Bukkit.getUpdateFolder(), file.getName()));

				    final byte[] data = new byte[1024];
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
