package ru.astondevs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReaderProperties {
    private final static String KEY_URL = "url";
    private final static String KEY_NAME = "name";
    private final static String KEY_PASSWORD = "password";

    public static Map<String, String> read(String path) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = loader.getResourceAsStream(path);
        Properties properties = new Properties();
        Map<String, String> connectionDetails = new HashMap<>();
        try {
            properties.load(resourceAsStream);
            String url = properties.get(KEY_URL).toString();
            String name = properties.get(KEY_NAME).toString();
            String password = properties.get(KEY_PASSWORD).toString();
            connectionDetails.put("url", url);
            connectionDetails.put("name", name);
            connectionDetails.put("password", password);
        } catch (IOException e) {
            System.out.println("Данные для подключения не найдены");
        }
        return connectionDetails;
    }
}
