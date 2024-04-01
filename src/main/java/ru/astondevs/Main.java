package ru.astondevs;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {


        String user = "user";
        String userID = "user/11546465/";

        System.out.println(user.matches("user/?"));
        System.out.println(userID.matches("user/\\d+/?"));
    }
}
