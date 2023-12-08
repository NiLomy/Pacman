package ru.kpfu.itis.lobanov.utils;

import ru.kpfu.itis.lobanov.exceptions.DbConfigException;
import ru.kpfu.itis.lobanov.exceptions.DbException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionProvider {
    public static final String DRIVER = "org.postgresql.Driver";

    private static Connection connection;

    public static Connection getConnection() throws DbException {
        if (connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(
                        DatabaseConfigProvider.getDbData("db.url"),
                        DatabaseConfigProvider.getDbData("db.user"),
                        DatabaseConfigProvider.getDbData("db.password")
                );
            } catch (SQLException | ClassNotFoundException e) {
                throw new DbException("Can not connect to DB.", e);
            } catch (DbConfigException e) {
                throw new DbException("Can not get configurations for current DB.", e);
            }
        }
        return connection;
    }

    private DatabaseConnectionProvider() {}
}
