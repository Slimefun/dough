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
		
		this.predicate = (local, remote) -> {
			return Integer.parseInt(remote) > Integer.parseInt(local);
		};
		
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
					    x.printStackTrace();
					    Thread.currentThread().interrupt();
				    }
				    
				    return false;
			    }
				
			    remoteVersion = String.valueOf(obj.get("last_successful").getAsInt());
			    downloadURL = new URL(api_url + getRepository() + "/" + getRepository().split("/")[1] + "-" + remoteVersion + ".jar");
			    
			    return true;
			} catch (IOException e) {
				System.err.println("[CS-CoreLib - Updater] Could not connect to github.io for Plugin \"" + plugin.getName() + "\", is it down?");
				try {
					thread.join();
				} catch (InterruptedException x) {
					x.printStackTrace();
				    Thread.currentThread().interrupt();
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
				    Thread.currentThread().interrupt();
				}
				return;
			}
		}

		private void install() {
			System.out.println("[CS-CoreLib - Updater] " + plugin.getName() + " is outdated!");
			System.out.println("[CS-CoreLib - Updater] Downloading " + plugin.getName() + " v" + remoteVersion);
			
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				BufferedInputStream input = null;
				FileOutputStream output = null;
				System.out.println(downloadURL.toString());
				try {
				    input = new BufferedInputStream(downloadURL.openStream());
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
					    Thread.currentThread().interrupt();
				    }
				}
			});
		}
	}

}
