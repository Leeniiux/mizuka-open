package fr.friendsc.mizuka.modules.admin.donator;

import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;

public class DonatorCommand {
    public DonatorCommand(GuildMessageReceivedEvent event) {
        Member member = event.getMember();
        if(member == null || !member.hasPermission(Permission.ADMINISTRATOR)) return;

        if(event.getMessage().getMentionedUsers().isEmpty()) {
            event.getMessage().reply("Utilisation : `.donator <@user>`").queue();
        } else {
            UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getMessage().getMentionedUsers().get(0));
            uc.donator = Instant.now().getEpochSecond();
            AchievementTools.becomeDonator(event, uc);
            uc.push();
            event.getMessage().reply("Cet utilisateur est maintenant donateur pendant un mois !").queue();
        }
    }
}
