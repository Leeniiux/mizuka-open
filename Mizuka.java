package fr.friendsc.mizuka;

import fr.friendsc.mizuka.events.Listener;
import fr.friendsc.mizuka.utils.Settings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class Mizuka {
    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        listenArgs(args);

        //JDA BUILD
        jda = JDABuilder.createDefault(Settings.DEVELOPMENT ? Settings.TOKEN_DEVELOPMENT : Settings.TOKEN_OFFICIAL)
                .addEventListeners(new Listener())
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES)
                .enableCache(CacheFlag.VOICE_STATE, CacheFlag.ACTIVITY)
                .build();
    }

    public static void listenArgs(String[] args) {
        for(String a : args) {
            switch(a) {
                case "-debug": Settings.DEBUG = true; break;
                case "-dev": Settings.DEVELOPMENT = true; break;
                case "-voice-xp": Settings.VOICE_XP = false; break;
                case "-chat-xp": Settings.CHAT_XP = false; break;
                case "-profile": Settings.PROFILE = false; break;
                case "-daily": Settings.DAILY = false; break;
                case "-clock": Settings.CLOCK = false; break;
                case "-roll": Settings.ROLL = false; break;
            }
        }
    }
}
