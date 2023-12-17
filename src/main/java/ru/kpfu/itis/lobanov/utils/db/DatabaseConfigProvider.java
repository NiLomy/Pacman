package ru.kpfu.itis.lobanov.utils.db;

import ru.kpfu.itis.lobanov.exceptions.DbConfigException;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.LogMessages;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConfigProvider {
    public static final String PROPERTY_FILE_NAME = "property/config.properties";
    public static final String PROPERTY_FILE_PATH = "src/main/resources/" + PROPERTY_FILE_NAME;

    public static void writeData(String dbUrl, String dbUser, String dbPassword) throws DbConfigException {
        try (OutputStream output = Files.newOutputStream(Paths.get(PROPERTY_FILE_PATH))) {
            Properties prop = new Properties();

            prop.setProperty(GameResources.URL_KEY, dbUrl);
            prop.setProperty(GameResources.USER_KEY, dbUser);
            prop.setProperty(GameResources.PASSWORD_KEY, dbPassword);

            prop.store(output, null);
        } catch (IOException e) {
            throw new DbConfigException(LogMessages.WRITE_DB_CONFIG_EXCEPTION, e);
        }
    }

    public static String getDbData(String data) throws DbConfigException {
        try (InputStream input = DatabaseConfigProvider.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME)) {
            Properties prop = new Properties();

            if (input == null) {
                throw new DbConfigException(String.format(LogMessages.NOT_FOUND_DB_CONFIG_EXCEPTION, PROPERTY_FILE_NAME));
            }

            prop.load(input);
            return prop.getProperty(data);
        } catch (IOException e) {
            throw new DbConfigException(LogMessages.READ_DB_CONFIG_EXCEPTION, e);
        }
    }

    private DatabaseConfigProvider() {
    }
}
