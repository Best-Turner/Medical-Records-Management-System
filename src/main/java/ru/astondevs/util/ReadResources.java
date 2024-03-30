package ru.astondevs.util;

import java.io.InputStream;

public class ReadResources implements ReaderFile {
    @Override
    public InputStream read(String path) {
        return ReadResources.class.getClassLoader().getResourceAsStream(path);
    }
}
