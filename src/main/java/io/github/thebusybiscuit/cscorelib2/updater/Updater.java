package io.github.thebusybiscuit.cscorelib2.updater;

import java.io.File;

import org.bukkit.Bukkit;

public interface Updater {
	
	String getLocalVersion();
	void start();
	
	default void prepareUpdateFolder() {
		File dir = new File("plugins/" + Bukkit.getUpdateFolder());
		if (!dir.exists()) dir.mkdirs();
	}
	
}
