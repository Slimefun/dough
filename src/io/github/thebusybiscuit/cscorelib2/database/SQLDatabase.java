package io.github.thebusybiscuit.cscorelib2.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.database.DatabaseQuery.ImportantDatabaseQuery;
import lombok.Getter;
import lombok.Setter;

public abstract class SQLDatabase<T extends SQLDatabase<T>> implements Database {
	
	protected Connection connection;
	protected DatabaseLoader<T> callback;
	protected Set<ImportantDatabaseQuery> queries;
	
	@Getter @Setter
	protected String tablePrefix = "";

	@Getter
	protected Plugin plugin;
	
	protected SQLDatabase(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void addSetupQuery(String sql) {
		ImportantDatabaseQuery query = new ImportantDatabaseQuery(this, sql);
		queries.add(query);
		query.execute();
	}
	
	@Override
	public String getTable(String table) {
		return tablePrefix + table;
	}
	
	@Override
	public boolean isConnected() {
		try {
			if (this.connection != null && this.connection.isValid(1)) {
				for (ImportantDatabaseQuery query: queries) {
					if (!query.hasFinished()) return false;
				}
				
				return true;
			}
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void closeResources(ResultSet set, PreparedStatement statement) {
		try {
			if (set != null) set.close();
			if (statement != null) statement.close();
		} catch(Exception x) {
			x.printStackTrace();
		}
	}
	
	@Override
	public void closeConnection() throws SQLException {
		if (!isConnected()) return;
		this.connection.close();
		this.connection = null;
	}
	
	@Override
	public void update(String query) {
		PreparedStatement statement = null;
		try {
			statement = getConnection().prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(query);
			e.printStackTrace();
		} finally {
			closeResources(null, statement);
		}
	}

}
