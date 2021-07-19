package org.ezapi.storage.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.IllegalClassException;
import org.ezapi.storage.StorageContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Mysql implements Sql {

    private final HikariConfig hikariConfig = new HikariConfig();

    private final HikariDataSource hikariDataSource;

    private final String database;
    private final String table;

    private Connection connection;
    private Statement statement;

    private final Map<String, StorageContext> cache = new HashMap<>();

    private boolean closed = false;

    public Mysql(String host, int port, String databaseName, String tableName, String username, String password) {
        this.database = databaseName;
        this.table = tableName;
        this.hikariConfig.setMaximumPoolSize(100);
        this.hikariConfig.setDataSourceClassName(getDataSource());
        this.hikariConfig.addDataSourceProperty("serverName", host);
        this.hikariConfig.addDataSourceProperty("port", port);
        this.hikariConfig.addDataSourceProperty("databaseName", this.database);
        this.hikariConfig.addDataSourceProperty("user", username);
        this.hikariConfig.addDataSourceProperty("password", password);
        this.hikariConfig.addDataSourceProperty("useSSL", false);
        this.hikariDataSource = new HikariDataSource(this.hikariConfig);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = this.hikariDataSource.getConnection();
            this.statement = this.connection.createStatement();
            String createTable = "CREATE TABLE IF Not Exists `{database}`.`{table}` ( `Name` TEXT , `Context` LONGTEXT ) ENGINE = InnoDB;".replace("{database}", this.database).replace("{table}", this.table);
            this.statement.executeUpdate(createTable);
            String selectFrom = "SELECT * FROM `{database}`.`{table}`;".replace("{database}", this.database).replace("{table}", this.table);
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
            String selectFrom = "SELECT * FROM `{database}`.`{table}`;".replace("{database}", this.database).replace("{table}", this.table);
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
        if (this.cache.containsKey(key)) {
            StorageContext value = this.cache.remove(key);
            String deleteFrom = "DELETE FROM `{database}`.`{table}` WHERE Name='{name}';".replace("{database}", this.database).replace("{table}", this.table).replace("{name}", key);
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
        String dropTable = "DROP TABLE `{database}`.`{table}`;".replace("{database}", this.database).replace("{table}", this.table);
        String createTable = "CREATE TABLE If Not Exists `{database}`.`{table}` ( `Name` TEXT , `Context` TEXT ) ENGINE = InnoDB;".replace("{database}", this.database).replace("{table}", this.table);
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
        if (this.has(key)) {
            this.remove(key);
        }
        String insert = "INSERT INTO `{database}`.`{table}` (`Name`, `Context`) VALUES ('{name}', '{context}');".replace("{database}", this.database).replace("{table}", this.table).replace("{name}", key).replace("{context}", value.toString());
        try {
            this.statement.executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.cache.put(key, value);
    }

    @Override
    public List<String> keys() {
        return new ArrayList<>(this.cache.keySet());
    }

    @Override
    public List<StorageContext> values() {
        return new ArrayList<>(this.values());
    }

    @Override
    public void close() throws SQLException {
        if (!closed) {
            this.connection.close();
            this.statement.close();
            this.hikariDataSource.close();
            this.connection = null;
            this.statement = null;
            closed = true;
        }
    }

    @Override
    public boolean closed() {
        return closed;
    }

    private void clearCache() {
        cache.clear();
    }

    private String getDataSource() {
        try {
            return Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource").getName();
        } catch (ClassNotFoundException ignored) {
            try {
                return Class.forName("com.mysql.cj.jdbc.MysqlDataSource").getName();
            } catch (ClassNotFoundException e) {
                throw new IllegalClassException("Cannot find the MysqlDataSource Class");
            }
        }
    }

}
