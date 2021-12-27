package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import fr.friendsc.mizuka.modules.experience.ExperienceTools;
import fr.friendsc.mizuka.modules.logs.LogsTools;
import fr.friendsc.mizuka.modules.voicechat.VoiceChat;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

import java.time.Instant;

public class onGuildVoiceJoin {
    public onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        //EXPERIENCE
        if(!ExperienceTools.inVoice.containsKey(event.getMember().getId())) {
            ExperienceTools.inVoice.put(event.getMember().getId(), Instant.now().getEpochSecond());
        }

        //VOICECHAT
        VoiceChat.join(event.getMember(), event.getChannelJoined());

        //LOGS
        LogsTools.memberVoiceJoin(event);

        //ACHIEVEMENTS
        AchievementTools.firstVoice(event);
    }
}
