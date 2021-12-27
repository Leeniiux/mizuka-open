package fr.friendsc.mizuka.storage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.friendsc.mizuka.utils.Database;
import fr.friendsc.mizuka.utils.Settings;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class GuildDao {
    private static LoadingCache<Guild, GuildConfig> GUILDS = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build(new CacheLoader<Guild, GuildConfig>() {@Override public GuildConfig load(@NotNull Guild key) { return null; }});

    public static GuildConfig get(Guild guild) {
        GuildConfig gc = GUILDS.getIfPresent(guild);
        if(gc != null) return gc;
        gc = new GuildConfig(guild);
        GUILDS.put(guild, gc);

        try {
            PreparedStatement pstmt = Database.getConnection().prepareStatement("SELECT * FROM " + Settings.DATABASE_GUILDS + " WHERE guild=?");
            pstmt.setString(1, guild.getId());
            ResultSet set = pstmt.executeQuery();
            if(set.next()) {

                //BOOLEANS
                gc.raidmode = set.getInt("raidmode") == 1;

                //LISTS
                gc.support_messages.addAll(Database.unserialize(set.getString("support_messages")));

                //ROLES
                gc.supporter_role = set.getString("supporter_role");
                gc.verification_role = set.getString("verification_role");
                gc.donator_role = set.getString("donator_role");
                gc.pingounet_role = set.getString("pingounet_role");

                //CHANNELS
                gc.logs_entries_channel = set.getString("logs_entries_channel");
                gc.logs_voice_channel = set.getString("logs_voice_channel");
                gc.logs_text_channel = set.getString("logs_text_channel");
                gc.voicechat_channel = set.getString("voicechat_channel");

                //CATEGORIES
                gc.support_category = set.getString("support_category");

                //STRINGS
                gc.prefix = set.getString("prefix");
                gc.french_channel = set.getString("french_channel");
                gc.english_channel = set.getString("english_channel");
                gc.french_welcome = set.getString("french_welcome");
                gc.english_welcome = set.getString("english_welcome");
            } else insert(gc);
        } catch(SQLException ignore) {}
        return gc;
    }

    public static void push(GuildConfig gc) {
        try {
            PreparedStatement pstmt = Database.getConnection().prepareStatement("UPDATE " + Settings.DATABASE_GUILDS + " SET " +
                    "supporter_role=?, " +
                    "support_messages=?, " +
                    "support_category=?, " +
                    "verification_role=?, " +
                    "donator_role=?, " +
                    "logs_entries_channel=?, " +
                    "logs_voice_channel=?, " +
                    "logs_text_channel=?, " +
                    "voicechat_channel=?, " +
                    "prefix=?, " +
                    "french_channel=?, " +
                    "english_channel=?, " +
                    "french_welcome=?, " +
                    "english_welcome=?, " +
                    "raidmode=?, " +
                    "pingounet_role=? " +
            "WHERE guild='" + gc.guild.getId() + "'");

            pstmt.setString(1, gc.supporter_role);
            pstmt.setString(2, Database.serialize(gc.support_messages));
            pstmt.setString(3, gc.support_category);
            pstmt.setString(4, gc.verification_role);
            pstmt.setString(5, gc.donator_role);
            pstmt.setString(6, gc.logs_entries_channel);
            pstmt.setString(7, gc.logs_voice_channel);
            pstmt.setString(8, gc.logs_text_channel);
            pstmt.setString(9, gc.voicechat_channel);
            pstmt.setString(10, gc.prefix);
            pstmt.setString(11, gc.french_channel);
            pstmt.setString(12, gc.english_channel);
            pstmt.setString(13, gc.french_welcome);
            pstmt.setString(14, gc.english_welcome);
            pstmt.setInt(15, gc.raidmode ? 1 : 0);
            pstmt.setString(16, gc.pingounet_role);

            pstmt.executeUpdate();
        } catch(SQLException ignore) {
            ignore.printStackTrace();
        }
    }

    private static void insert(GuildConfig gc) {
        try { Database.getConnection().createStatement().execute("INSERT INTO " + Settings.DATABASE_GUILDS + "(guild) VALUES('" + gc.guild.getId() + "')");
        } catch(SQLException ignore) {}
    }
}
