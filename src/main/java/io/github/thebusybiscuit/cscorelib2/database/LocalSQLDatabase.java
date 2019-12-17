package io.github.thebusybiscuit.cscorelib2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import lombok.Getter;

public abstract class LocalSQLDatabase<T extends LocalSQLDatabase<T>> extends SQLDatabase<T> {

	@Getter
	protected final String name;
	
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
			getLogger().log(Level.SEVERE, "An Exeption occured while connecting to a Database", e);
		}
		
		try {
			getLogger().log(Level.INFO, "Loading SQL Driver...");
			Class.forName(getDriver());
		}
		catch (Exception x) {
			getLogger().log(Level.SEVERE, "An Exception occured while loading the Database Driver: " + getDriver(), x);
			
			callback.onLoad((T) this, null);
			return null;
		}
		
		getLogger().log(Level.INFO, "Attempting to connect to local Database \"{0}\"", name);
		
		try (Connection connection = DriverManager.getConnection(getIP())) {
			getLogger().log(Level.INFO, "> Connection Result: SUCCESSFUL");
			
			callback.onLoad((T) this, connection);
			this.connection = connection;
			return connection;
		} catch (Exception x) {
			getLogger().log(Level.SEVERE, "> Connection Result: FAILED");
			getLogger().log(Level.SEVERE, "> Double-check the Host and Credentials you specified in the \"" + getType() + ".yml\" under /plugins/" + plugin.getName() + "/" + getType() + ".yml");
			getLogger().log(Level.SEVERE, "An Exception occured while opening a Database Connection", x);

			callback.onLoad((T) this, null);
			return null;
		}
	}
	
}
