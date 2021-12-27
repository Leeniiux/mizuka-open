package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.logs.LogsTools;
import fr.friendsc.mizuka.modules.raidmode.RaidmodeEvent;
import fr.friendsc.mizuka.modules.welcome.WelcomeEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class onGuildMemberJoin {
    public onGuildMemberJoin(GuildMemberJoinEvent event) {
        //LOGS
        LogsTools.memberJoin(event);

        //VERIFICATION
        WelcomeEvent.join(event);

        //RAIDMODE
        RaidmodeEvent.join(event);
    }
}
