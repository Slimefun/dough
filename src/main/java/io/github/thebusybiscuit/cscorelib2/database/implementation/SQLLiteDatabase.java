package io.github.thebusybiscuit.cscorelib2.database.implementation;

import java.io.File;

import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.database.LocalSQLDatabase;

public class SQLLiteDatabase extends LocalSQLDatabase<SQLLiteDatabase> {

    public SQLLiteDatabase(Plugin plugin, String name, DatabaseLoader<SQLLiteDatabase> callback) {
        super(plugin, name, callback);
    }

    @Override
    public String getType() {
        return "SQLLite";
    }

    @Override
    public String getIP() {
        return "jdbc:sqlite:" + new File("plugins/" + plugin.getName(), this.name + ".db").getAbsolutePath();
    }

    @Override
    public String getDriver() {
        return "org.sqlite.JDBC";
    }
}