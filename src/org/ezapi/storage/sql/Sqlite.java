package org.ezapi.storage.sql;

import org.ezapi.storage.FileStorage;
import org.ezapi.storage.StorageContext;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Sqlite extends FileStorage implements Sql {

    private final String url;

    private final Map<String, StorageContext> cache = new HashMap<>();

    private Connection connection;
    private Statement statement;

    private final String table;

    private boolean closed = false;

    public Sqlite(File file, String table) {
        super(file);
        this.table = table;
        this.url = "jdbc:sqlite:" + file.getPath();
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
            this.statement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS `{table}` ( `Name` TEXT , `Context` TEXT );".replace("{table}", table);
            this.statement.executeUpdate(createTable);
            String selectFrom = "SELECT * FROM `{table}`;".replace("{table}", this.table);
            ResultSet resultSet = this.statement.executeQuery(selectFrom);
            clearCache();
            while (resultSet.next()) {
                cache.put(resultSet.getString("Name"), StorageContext.getByString(resultSet.getString("Context")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reload() {
        try {
            String selectFrom = "SELECT * FROM `{table}`;".replace("{table}", this.table);
            ResultSet resultSet = this.statement.executeQuery(selectFrom);
            clearCache();
            while (resultSet.next()) {
                cache.put(resultSet.getString("Name"), StorageContext.getByString(resultSet.getString("Context")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean has(String key) {
        return cache.containsKey(key);
    }

    @Override
    public StorageContext remove(String key) {
        if (cache.containsKey(key)) {
            StorageContext value = cache.remove(key);
            String deleteFrom = "DELETE FROM `{table}` WHERE Name='{name}';".replace("{table}", table);
            try {
                this.statement.executeUpdate(deleteFrom);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return value;
        }
        return new StorageContext();
    }

    @Override
    public void removeAll() {
        this.cache.clear();
        String dropTable = "DROP TABLE `{table}`;".replace("{table}", this.table);
        String createTable = "CREATE TABLE `{table}` ( `Name` TEXT , `Context` TEXT );".replace("{table}", this.table);
        try {
            this.statement.executeUpdate(dropTable);
            this.statement.executeUpdate(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StorageContext get(String key) {
        if (has(key)) {
            return cache.get(key);
        }
        return new StorageContext();
    }

    @Override
    public void set(String key, StorageContext value) {
        if (this.cache.containsKey(key)) {
            this.remove(key);
        }
        String insert = "INSERT INTO `{table}` (`Name`, `Context`) VALUES ('{name}', '{context}');".replace("{table}", this.table).replace("{name}", key).replace("{context}", value.toString());
        try {
            this.statement.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.cache.put(key, value);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(cache.keySet());
    }

    @Override
    public List<StorageContext> values() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public void close() throws SQLException {
        this.connection.close();
        this.statement.close();
        closed = true;
    }

    @Override
    public boolean closed() {
        return closed;
    }

    private void clearCache() {
        cache.clear();
    }

}
