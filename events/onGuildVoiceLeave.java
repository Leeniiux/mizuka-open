package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.experience.ExperienceTools;
import fr.friendsc.mizuka.modules.logs.LogsTools;
import fr.friendsc.mizuka.modules.voicechat.VoiceChat;
import fr.friendsc.mizuka.utils.XP;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

public class onGuildVoiceLeave {
    public onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        //EXPERIENCE
        ExperienceTools.gainXP(XP.VOICE, event.getGuild(), event.getMember().getUser());
        ExperienceTools.inVoice.remove(event.getMember().getId());

        //VOICECHAT
        VoiceChat.leave(event.getMember(), event.getChannelLeft());

        //LOGS
        LogsTools.memberVoiceLeave(event);
    }
}
