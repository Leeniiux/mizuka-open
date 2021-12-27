package fr.friendsc.mizuka.modules.raidmode;

import fr.friendsc.mizuka.modules.logs.LogsTools;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class RaidmodeEvent {
    public static void join(GuildMemberJoinEvent event) {
        GuildConfig gc = GuildDao.get(event.getGuild());
        if(gc.raidmode) {
            event.getGuild().kick(event.getMember(), "Raidmode is enabled !").queue();
            LogsTools.memberJoinWhileRaidmode(event);
        }
    }
}
