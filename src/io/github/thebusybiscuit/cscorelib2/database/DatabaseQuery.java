package io.github.thebusybiscuit.cscorelib2.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import org.bukkit.scheduler.BukkitTask;

public class DatabaseQuery {
	
	protected Database database;
	protected String query;
	protected boolean finished;
	
	protected BukkitTask task;
	
	public DatabaseQuery(Database database, String query) {
		this.database = database;
		this.query = query;
	}
	
	public void execute() {
		task = database.getPlugin().getServer().getScheduler().runTaskAsynchronously(database.getPlugin(), () -> {
			try {
				waitForConnection();
				
				if (task.isCancelled()) return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			database.update(query);
			finished = true;
		});
	}
	
	public void execute(Consumer<ResultSet> func) {
		task = database.getPlugin().getServer().getScheduler().runTaskAsynchronously(database.getPlugin(), () -> {
			try {
				waitForConnection();
				
				if (task.isCancelled()) return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				ResultSet set = database.query(query);

				func.accept(set);
				set.close();
				finished = true;
			} catch (SQLException e) {
				System.out.println(query);
				e.printStackTrace();
			}
		});
	}
	
	public void forEach(Runnable callback, Consumer<ResultSet> func) {
		task = database.getPlugin().getServer().getScheduler().runTaskAsynchronously(database.getPlugin(), () -> {
			try {
				waitForConnection();
				
				if (task.isCancelled()) return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			callback.run();
			
			try {
				ResultSet set = database.query(query);
				
				while (set.next()) {
					func.accept(set);
				}
				set.close();
				
				finished = true;
			} catch (SQLException e) {
				System.out.println(query);
				e.printStackTrace();
			}
		});
	}
	
	protected void waitForConnection() throws InterruptedException {
		while (!database.isConnected()) {
			Thread.sleep(50);
		}
	}

	public boolean hasFinished() {
		return finished;
	}
	
	public static class ImportantDatabaseQuery extends DatabaseQuery {

		public ImportantDatabaseQuery(Database database, String query) {
			super(database, query);
		}
		
		@Override
		protected void waitForConnection() throws InterruptedException {
			while (database.getConnection() == null) {
				Thread.sleep(50);
			}
		}
		
	}

}
