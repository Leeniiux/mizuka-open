package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.logs.LogsTools;
import fr.friendsc.mizuka.modules.voicechat.VoiceChat;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

public class onGuildVoiceMove {
    public onGuildVoiceMove(GuildVoiceMoveEvent event) {
        //VOICECHAT
        VoiceChat.join(event.getMember(), event.getChannelJoined());
        VoiceChat.leave(event.getMember(), event.getChannelLeft());

        //LOGS
        LogsTools.memberVoiceMove(event);
    }
}
