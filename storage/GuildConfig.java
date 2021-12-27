package fr.friendsc.mizuka.storage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.friendsc.mizuka.utils.Database;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuildConfig {
    private final LoadingCache<User, UserConfig> USERS = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).build(new CacheLoader<User, UserConfig>() {@Override public UserConfig load(@NotNull User key) { return null; }});
    public final Guild guild;

    //BOOLEANS
    public boolean raidmode = false;

    //LISTS
    public final List<String> support_messages = new ArrayList<>();

    //ROLES
    public String supporter_role = "0";
    public String verification_role = "0";
    public String donator_role = "0";
    public String pingounet_role = "0";

    //CHANNELS
    public String logs_entries_channel = "0";
    public String logs_voice_channel = "0";
    public String logs_text_channel = "0";
    public String voicechat_channel = "0";
    public String french_channel = "0";
    public String english_channel = "0";

    //CATEGORIES
    public String support_category = "0";

    //STRINGS
    public String prefix = ".";
    public String french_welcome = "";
    public String english_welcome = "";

    public GuildConfig(Guild guild) { this.guild = guild; }

    public UserConfig getUser(User user) {
        UserConfig uc = USERS.getIfPresent(user);
        if(uc != null) return uc;
        uc = UserDao.get(guild, user);
        USERS.put(user, uc);
        return uc;
    }

    public void push() { GuildDao.push(this); }
}
