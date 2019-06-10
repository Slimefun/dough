package io.github.thebusybiscuit.cscorelib2.database.implementation;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.database.AuthenticatedSQLDatabase;

public class PostgreSQLDatabase extends AuthenticatedSQLDatabase<PostgreSQLDatabase> {
	
	public PostgreSQLDatabase(Plugin plugin, DatabaseLoader<PostgreSQLDatabase> callback) {
		super(plugin, callback);
	}
	
	@Override
	public String getType() {
		return "PostgreSQL";
	}
	
	@Override
	public int getDefaultPort() {
		return 5432;
	}
	
	@Override
	public String getDriver() {
		return "org.postgresql.Driver";
	}
	
	@Override
	public String getIP() {
		return "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database;
	}
}