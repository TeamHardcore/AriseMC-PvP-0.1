/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.teamhardcore.pvp.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    private final Main plugin;

    private HikariDataSource source;
    private ExecutorService service;

    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean init() {
        FileConfiguration cfg = this.plugin.getFileManager().getConfigFile().getConfig();
        String host = cfg.getString("MySQL.Host");
        String port = cfg.getString("MySQL.Port");
        String user = cfg.getString("MySQL.User");
        String pass = cfg.getString("MySQL.Pass");
        String database = cfg.getString("MySQL.DB");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfig.addDataSourceProperty("serverName", host);
        hikariConfig.addDataSourceProperty("port", port);
        hikariConfig.addDataSourceProperty("databaseName", database);
        hikariConfig.addDataSourceProperty("user", user);
        hikariConfig.addDataSourceProperty("password", pass);
        this.source = new HikariDataSource(hikariConfig);
        this.service = Executors.newCachedThreadPool();


        return true;
    }

    public void closeHikrari() {
        if (this.source != null && !this.source.isClosed()) {
            this.source.close();
        }
        this.service.shutdown();
    }

    public void close(PreparedStatement st, ResultSet rs) {
        try {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception exception) {
        }
    }

    public void close(PreparedStatement... statements) {
        for (PreparedStatement st : statements) {
            try {
                st.close();
            } catch (Exception exception) {
            }
        }
    }

    public void close(ResultSet... resultSets) {
        for (ResultSet rs : resultSets) {
            try {
                rs.close();
            } catch (Exception exception) {
            }
        }
    }

    public void executeUpdate(String statement) {
        Connection conn = null;
        try {
            conn = this.source.getConnection();
            PreparedStatement st = conn.prepareStatement(statement);
            st.executeUpdate();
            close(st, null);
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("executeUpdate konnte nicht ausgeführt werden: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed())
                    conn.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void executeUpdate(PreparedStatement statement) {
        try {
            statement.executeUpdate();
            close(statement, null);
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("executeUpdate konnte nicht ausgeführt werden: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(PreparedStatement statement) {
        try {
            return statement.executeQuery();
        } catch (Exception e) {
            Main.getInstance().getLogger().warning("executeQuery konnte nicht ausgeführt werden: " + e.getMessage());

            return null;
        }
    }

    public HikariDataSource getSource() {
        return source;
    }

    public ExecutorService getService() {
        return service;
    }

    public Main getPlugin() {
        return plugin;
    }
}
