package io.github.thebusybiscuit.cscorelib2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;

public abstract class AuthenticatedSQLDatabase<T extends AuthenticatedSQLDatabase<T>> extends SQLDatabase<T>{
	
	protected String host;
	protected String user;
	protected String password;
	protected String database;
	protected int port;
	
	public AuthenticatedSQLDatabase(Plugin plugin, DatabaseLoader<T> callback) {
		super(plugin);
		
		this.callback = callback;
		this.queries = new HashSet<>();
		
		Config cfg = new Config(plugin, getType() + ".yml");
		cfg.setDefaultValue("host", "localhost");
		cfg.setDefaultValue("port", getDefaultPort());
		cfg.setDefaultValue("username", "root");
		cfg.setDefaultValue("password", "password");
		cfg.setDefaultValue("database", "database");
		cfg.setDefaultValue("table-prefix", "");
		cfg.save();
		
		this.host = cfg.getString("host");
		this.port = cfg.getInt("port");
		this.user = cfg.getString("username");
		this.password = cfg.getString("password");
		this.database = cfg.getString("database");
		this.tablePrefix = cfg.getString("table-prefix");
		
		getConnection();
	}
	
	public abstract String getDriver();
	public abstract int getDefaultPort();
	
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
		
		getLogger().log(Level.INFO, "Attempting to connect to Database \"{0}\"", database);
		
		try (Connection connection = DriverManager.getConnection(getIP(), this.user, this.password)) {
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
