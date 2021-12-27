package fr.friendsc.mizuka.modules.experience;

import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.storage.UserDao;
import fr.friendsc.mizuka.utils.XP;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ExperienceTools {

    public static final Map<String, Long> inVoice = new HashMap<>();

    public static void gainXP(XP type, Guild guild, User user) {
        if(type == XP.CHAT) {
            UserConfig uc = GuildDao.get(guild).getUser(user);
            uc.experience += 1;
            uc.push();
        }
        else if(type == XP.VOICE) {
            if(inVoice.containsKey(user.getId())) {
                long current = inVoice.get(user.getId());
                long min = (Instant.now().getEpochSecond() - current)/60;
                UserConfig uc = GuildDao.get(guild).getUser(user);
                uc.experience += min;
                inVoice.replace(user.getId(), current+min*60);
                uc.push();
            }
        }
    }
}
