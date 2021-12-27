package fr.friendsc.mizuka.storage;

import fr.friendsc.mizuka.modules.achievements.Achievement;
import fr.friendsc.mizuka.utils.Config;
import fr.friendsc.mizuka.utils.Database;
import fr.friendsc.mizuka.utils.Language;
import fr.friendsc.mizuka.utils.Settings;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserConfig {
    public final User user;
    public final Guild guild;

    public Language language = Language.FRENCH;

    public long tokiends = 0;
    public long daily = 0;
    public long roll = 0;
    public long experience = 0;
    public long prestige = 0;
    public long donator = 0;

    public String title = "";
    public String voicename = "";

    public List<String> shop_colors = new ArrayList<>();
    public List<String> shop_images = new ArrayList<>();
    public List<String> shop_banners = new ArrayList<>();

    public List<Achievement> achievements = new ArrayList<>();

    public boolean gifted = false;
    public int gifts = 0;



    public UserConfig(User user, Guild guild) { this.user = user; this.guild = guild; }

    public void push() {
        UserDao.push(this);
    }

    public int getRank() {
        try {
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM " + Settings.DATABASE_USERS + " WHERE experience > " + experience + " AND guild='" + guild.getId() + "'");
            if(rs.next()) return rs.getInt("total") + 1;
        } catch(SQLException ignore) {}
        return 0;
    }

    public boolean isBooster() {
        Member member = guild.retrieveMember(user).complete();
        return member != null && member.getTimeBoosted() != null;
    }

    public boolean isDonator() {
        return Instant.now().getEpochSecond() - donator < Config.TIME_DONATOR;
    }

    public boolean isSupporter() {
        Member member = guild.retrieveMember(user).complete();
        for(Activity activity : member.getActivities()) {
            if(activity.getName().contains(".gg/friends")) return true;
        }
        return false;
    }

    public boolean canDaily() {
        long time = isBooster() ? Config.TIME_BETWEEN_DAILY_BOOSTER : Config.TIME_BETWEEN_DAILY;
        return daily + time <= Instant.now().getEpochSecond();
    }

    public boolean canRoll() {
        return roll + Config.TIME_BETWEEN_ROLL <= Instant.now().getEpochSecond();
    }

    public String getDailyString() {
        long time = isBooster() ? Config.TIME_BETWEEN_DAILY_BOOSTER : Config.TIME_BETWEEN_DAILY;
        long seconds = daily + time - Instant.now().getEpochSecond();
        long minutes = seconds/60;
        long hours = minutes/60;
        seconds -= minutes*60;
        minutes -= hours*60;
        return Math.abs(hours) + "h " + Math.abs(minutes) + "m " + Math.abs(seconds) + "s";
    }

    public String getRollString() {
        long seconds = roll + Config.TIME_BETWEEN_ROLL - Instant.now().getEpochSecond();
        long minutes = seconds/60;
        long hours = minutes/60;
        long days = hours/24;
        seconds -= minutes*60;
        minutes -= hours*60;
        hours -= days*24;
        return Math.abs(days) + "d " + Math.abs(hours) + "h " + Math.abs(minutes) + "m " + Math.abs(seconds) + "s";
    }
}
