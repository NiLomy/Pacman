package ru.kpfu.itis.lobanov.utils;

import ru.kpfu.itis.lobanov.exceptions.DbConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class DatabaseConfigProvider {
    public static void writeData(String dbUrl, String dbUser, String dbPassword) throws DbConfigException {
        try (OutputStream output = Files.newOutputStream(Paths.get("src/main/resources/config.properties"))) {
            Properties prop = new Properties();

            prop.setProperty("db.url", dbUrl);
            prop.setProperty("db.user", dbUser);
            prop.setProperty("db.password", dbPassword);

            prop.store(output, null);
        } catch (IOException e) {
            throw new DbConfigException("Can not write data into the property file.", e);
        }
    }

    public static String getDbData(String data) throws DbConfigException {
        try (InputStream input = DatabaseConfigProvider.class.getClassLoader().getResourceAsStream("property/config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                throw new DbConfigException("Unable to find: " + "property/config.properties" + ".");
            }

            prop.load(input);
            return prop.getProperty(data);
        } catch (IOException e) {
            throw new DbConfigException("Can not read data from the property file.", e);
        }
    }

    private DatabaseConfigProvider() {}
}
