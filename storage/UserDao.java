package fr.friendsc.mizuka.storage;

import fr.friendsc.mizuka.utils.Database;
import fr.friendsc.mizuka.utils.Language;
import fr.friendsc.mizuka.utils.Settings;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static volatile boolean getting = false;

    public static UserConfig get(Guild guild, User user) {
        while(getting) { Thread.onSpinWait(); }
        getting = true;
        UserConfig uc = new UserConfig(user, guild);
        try {
            PreparedStatement pstmt = Database.getConnection().prepareStatement("SELECT * FROM " + Settings.DATABASE_USERS + " WHERE guild=? AND user=?");
            pstmt.setString(1, guild.getId());
            pstmt.setString(2, user.getId());
            ResultSet set = pstmt.executeQuery();
            if(set.next()) {
                uc.language = set.getString("language").equals("FRENCH") ? Language.FRENCH : Language.ENGLISH;

                uc.tokiends = set.getLong("tokiends");
                uc.daily = set.getLong("daily");
                uc.roll = set.getLong("roll");
                uc.experience = set.getLong("experience");
                uc.prestige = set.getLong("prestige");
                uc.donator = set.getLong("donator");

                uc.title = set.getString("title");
                uc.voicename = set.getString("voicename");

                uc.gifted = set.getInt("gifted") == 1;

                uc.achievements = Database.unserializeAchievements(set.getString("achievements"));
            } else insert(uc);
        } catch(SQLException ignore) {}
        getting = false;
        return uc;
    }

    public static void push(UserConfig uc) {
        try {
            PreparedStatement pstmt = Database.getConnection().prepareStatement("UPDATE " + Settings.DATABASE_USERS + " SET " +
                    "language=?, " +
                    "tokiends=?, " +
                    "daily=?, " +
                    "roll=?, " +
                    "experience=?, " +
                    "prestige=?, " +
                    "donator=?, " +
                    "title=?, " +
                    "voicename=?, " +
                    "gifted=?, " +
                    "achievements=? " +
                    "WHERE guild='" + uc.guild.getId() + "' AND user='" + uc.user.getId() + "'");

            pstmt.setString(1, uc.language.name());
            pstmt.setLong(2, uc.tokiends);
            pstmt.setLong(3, uc.daily);
            pstmt.setLong(4, uc.roll);
            pstmt.setLong(5, uc.experience);
            pstmt.setLong(6, uc.prestige);
            pstmt.setLong(7, uc.donator);
            pstmt.setString(8, uc.title);
            pstmt.setString(9, uc.voicename);
            pstmt.setInt(10, uc.gifted ? 1 : 0);
            pstmt.setString(11, Database.serializeAchievements(uc.achievements));

            pstmt.executeUpdate();
        } catch(SQLException ignore) {
            ignore.printStackTrace();
        }
    }

    private static void insert(UserConfig uc) {
        try { Database.getConnection().createStatement().execute("INSERT INTO " + Settings.DATABASE_USERS + "(guild, user) VALUES('" + uc.guild.getId() + "', '" + uc.user.getId() + "')");
        } catch(SQLException ignore) {
            ignore.printStackTrace();
        }
    }
}
