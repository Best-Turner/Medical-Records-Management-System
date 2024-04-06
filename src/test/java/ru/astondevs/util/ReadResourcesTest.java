package ru.astondevs.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReadResourcesTest {

    private ReaderFile reader;

    @BeforeEach
    void setUp() {
        reader = new ReadResources();
    }

    @Test
    void testReadReturnFalse() {
        String path = "myresource.txt";
        assertNull(reader.read(path));
    }

    @Test
    void testReadReturnTrue() {
        String path = "test.txt";
        assertNotNull(reader.read(path));
    }

}