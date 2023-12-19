package ru.kpfu.itis.lobanov.utils.db;

import ru.kpfu.itis.lobanov.exceptions.DbConfigException;
import ru.kpfu.itis.lobanov.exceptions.DbException;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;

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
                        DatabaseConfigProvider.getDbData(GameResources.URL_KEY),
                        DatabaseConfigProvider.getDbData(GameResources.USER_KEY),
                        DatabaseConfigProvider.getDbData(GameResources.PASSWORD_KEY)
                );
            } catch (SQLException | ClassNotFoundException e) {
                throw new DbException(LogMessages.ESTABLISH_CONNECTION_DB_EXCEPTION, e);
            } catch (DbConfigException e) {
                throw new DbException(LogMessages.GET_CONFIGURATION_DB_EXCEPTION, e);
            }
        }
        return connection;
    }

    private DatabaseConnectionProvider() {
    }
}
