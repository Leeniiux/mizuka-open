package fr.friendsc.mizuka.modules.pingounet;

import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PingounetCommand {
    public PingounetCommand(GuildMessageReceivedEvent event, String[] args) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        Role role = guild.getRoleById(gc.pingounet_role);
        Member member = event.getMember();
        if(role == null || member == null) return;

        UserConfig uc = gc.getUser(event.getAuthor());
        if(args.length < 1) {
            if(!member.getRoles().contains(role)) {
                if(uc.language == Language.FRENCH) {
                    event.getMessage().replyEmbeds(new EmbedBuilder()
                            .setColor(0x303037)
                            .setDescription("__Le rôle <@&" + gc.pingounet_role + "> est un rôle particulier...__\nApparu il y a de nombreux mois maintenant, ce rôle peut être mentionné par nos administrateurs, et ce, **N'IMPORTE QUAND !**\n\n*Si tu souhaites tout de même obtenir ce rôle, utilise la commande :\n`.pingounet confirm`*")
                            .build()
                    ).queue();
                } else {
                    event.getMessage().replyEmbeds(new EmbedBuilder()
                            .setColor(0x303037)
                            .setDescription("__The role <@&" + gc.pingounet_role + "> is quite perculiar...__\nIt appeared months ago, and can be mentionned by our admins, **AT ANYTIME !**\n\n*If you still wish to get that role, please use :\n`.pingounet confirm`*")
                            .build()
                    ).queue();
                }
            } else {
                event.getMessage().reply(uc.language == Language.FRENCH ? "Rôle Pingounet retiré !" : "Pingounet role removed !").queue();
                guild.removeRoleFromMember(member, role).queue();
            }
        } else {
            if(args[0].equals("confirm")) {
                if(member.getRoles().contains(role)) event.getMessage().reply(uc.language == Language.FRENCH ? "Utilise la commande `.pingounet` si tu souhaites retirer ton rôle." : "Use `.pingounet` if you want to remove your role.").queue();
                else {
                    guild.addRoleToMember(member, role).queue();
                    event.getMessage().reply(uc.language == Language.FRENCH ? "Rôle Pingounet ajouté !" : "Pingounet role added !").queue();
                    AchievementTools.pingounetRole(event, uc);
                }
            }
        }
    }
}
