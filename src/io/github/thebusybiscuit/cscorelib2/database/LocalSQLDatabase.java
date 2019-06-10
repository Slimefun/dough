package io.github.thebusybiscuit.cscorelib2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;

import org.bukkit.plugin.Plugin;

public abstract class LocalSQLDatabase<T extends LocalSQLDatabase<T>> extends SQLDatabase<T> {

	protected String name;
	
	public LocalSQLDatabase(Plugin plugin, String name, DatabaseLoader<T> callback) {
		super(plugin);
		
		this.name = name;
		this.callback = callback;
		this.queries = new HashSet<>();
		
		getConnection();
	}

	public abstract String getDriver();
	
	@SuppressWarnings("unchecked")
	@Override
	public Connection getConnection() {
		try {
			if (this.connection != null && this.connection.isValid(1)) return this.connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println("[Slimefun - Database] Loading SQL Driver...");
			Class.forName(getDriver());
		}
		catch (Exception x) {
			System.err.println("ERROR: Failed to load SQL Driver: " + getDriver());
			x.printStackTrace();
			
			callback.onLoad((T) this, null);
			return null;
		}
		
		System.out.println("[Slimefun - Database] Attempting to connect to local Database \"" + this.name + "\"");
		
		try {
			Connection connection = DriverManager.getConnection(getIP());
			System.out.println("> Connection Result: SUCCESSFUL");
			
			callback.onLoad((T) this, connection);
			this.connection = connection;
			return connection;
		} catch (Exception x) {
			System.err.println("> Connection Result: FAILED");
			System.err.println(" ");
			System.err.println("ERROR: Could not connect to local Database \"" + this.name + "\"");
			x.printStackTrace();
			
			callback.onLoad((T) this, null);
			return null;
		}
	}
	
}
