package io.github.thebusybiscuit.cscorelib2.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public interface Database {
	
	Connection getConnection();
	String getIP();
	boolean isConnected();
	
	Plugin getPlugin();
	Logger getLogger();
	String getType();
	
	void update(String query);
	
	void closeConnection() throws SQLException;
	
	void addSetupQuery(String query);
	
	String getTablePrefix();
	String getTable(String table);

	default ResultSet query(String query) throws SQLException {
		try (Statement statement = getConnection().createStatement()) {
			return statement.executeQuery(query);
		}
	}
	
	@FunctionalInterface
	public static interface DatabaseLoader<T extends Database> {
		
		void onLoad(T database, Connection connection);
		
	}

}
