package ru.itmo.ArsikAndEva.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static String url;
    private static String user;
    private static String password;
    private static boolean initialized = false;

    private Database() {}

    public static synchronized void init() {
        if (initialized) return;


        url = System.getenv("DB_URL");
        user = System.getenv("DB_USER");
        password = System.getenv("DB_PASSWORD");


        if (url == null || user == null || password == null) {
            Properties props = new Properties();
            Path cfg = Path.of("config", "db.properties");
            try (InputStream in = Files.newInputStream(cfg)) {
                props.load(in);
                url = props.getProperty("db.url");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");
            } catch (IOException e) {
                throw new IllegalStateException(
                        "Не удалось прочитать config/db.properties. ", e);
            }
        }

        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Не заданы параметры подключения к БД (url/user/password).");
        }
        initialized = true;
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) init();
        return DriverManager.getConnection(url, user, password);
    }
}