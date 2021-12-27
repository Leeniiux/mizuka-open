package fr.friendsc.mizuka.modules.settings;

import fr.friendsc.mizuka.Mizuka;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PrefixCommand {
    public PrefixCommand(GuildMessageReceivedEvent event, String[] args) {
        Member member = event.getMember();
        if(member != null && !member.hasPermission(Permission.ADMINISTRATOR)) return;

        GuildConfig gc = GuildDao.get(event.getGuild());

        StringBuilder sb = new StringBuilder();
        for(String arg : args) {
            sb.append(" ").append(arg);
        }
        String prefix = sb.toString().replaceFirst(" ", "");
        if(prefix.length() > 10) {
            event.getChannel().sendMessage("Votre préfix ne peut pas dépasser 10 caractères.").complete();
        } else if(prefix.length() <= 0) {
            event.getChannel().sendMessage("Votre préfix ne peut pas être vide.").complete();
        } else {
            gc.prefix = prefix;
            gc.push();
            event.getChannel().sendMessage("Nouveau préfix initialisé pour ce serveur : `" + prefix + "`").complete();
        }
    }
}
