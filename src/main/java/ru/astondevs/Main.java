package ru.astondevs;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        String line = "3";
        System.out.println(line.matches("\\d"));
    }
}
