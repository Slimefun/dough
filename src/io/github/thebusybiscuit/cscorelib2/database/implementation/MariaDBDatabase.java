package io.github.thebusybiscuit.cscorelib2.database.implementation;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.database.AuthenticatedSQLDatabase;

public class MariaDBDatabase extends AuthenticatedSQLDatabase<MariaDBDatabase> {
	
	public MariaDBDatabase(Plugin plugin, DatabaseLoader<MariaDBDatabase> callback) {
		super(plugin, callback);
	}
	
	@Override
	public String getType() {
		return "MariaDB";
	}
	
	@Override
	public int getDefaultPort() {
		return 3306;
	}
	
	@Override
	public String getDriver() {
		return "org.mariadb.jdbc.Driver";
	}
	
	@Override
	public String getIP() {
		return "jdbc:mariadb://" + this.host + ":" + this.port + "/" + this.database;
	}
}
