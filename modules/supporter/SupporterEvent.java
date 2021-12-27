package fr.friendsc.mizuka.modules.supporter;

import fr.friendsc.mizuka.modules.achievements.Achievement;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import net.dv8tion.jda.api.entities.*;

public class SupporterEvent {
    public SupporterEvent(Guild guild, Member member) {
        GuildConfig gc = GuildDao.get(guild);
        Role role = guild.getRoleById(gc.supporter_role);
        if(role == null || member == null) return;
        UserConfig uc = gc.getUser(member.getUser());

        if(uc.isSupporter()) {
            guild.addRoleToMember(member, role).queue();
            if(!uc.achievements.contains(Achievement.BECOME_SUPPORTER)) {
                uc.achievements.add(Achievement.BECOME_SUPPORTER);
                uc.push();
            }
        } else guild.removeRoleFromMember(member, role).queue();
    }
}
