package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.logs.LogsTools;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

public class onGuildMemberRemove {
    public onGuildMemberRemove(GuildMemberRemoveEvent event) {
        //LOGS
        LogsTools.memberLeft(event);
    }
}
