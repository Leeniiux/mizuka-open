package fr.friendsc.mizuka.utils;

import fr.friendsc.mizuka.modules.achievements.Achievement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Database {
    private static Connection connection = null;

    public static Connection getConnection() {
        if(connection != null) return connection;
        connect();
        return connection;
    }

    private static void connect() {
        try {
            Properties prop = new Properties();
            prop.setProperty("autoReconnect", "true");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    Settings.DATABASE_HOST +
                            "/" + Settings.DATABASE_NAME +
                            "?user=" + Settings.DATABASE_USERNAME +
                            "&password=" + Settings.DATABASE_PASSWORD +
                            "&serverTimezone=UTC"
                    , prop);
        } catch(SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            System.out.println("Error : Database connection failed.\n Shutting down...");
            System.exit(40);
        }
    }

    public static String serialize(List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(sb.append(" ")::append);
        return sb.toString().trim();
    }

    public static List<String> unserialize(String string) {
        if(string == null) return new ArrayList<>();
        return Arrays.asList(string.split(" "));
    }

    public static String serializeAchievements(List<Achievement> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(a -> sb.append(" ").append(a.name()));
        return sb.toString().trim();
    }

    public static List<Achievement> unserializeAchievements(String string) {
        List<Achievement> list = new ArrayList<>();
        if(string == null) return list;
        for(String s : string.split(" ")) {
            if(!s.isEmpty()) list.add(Achievement.valueOf(s));
        }
        return list;
    }
}