package fr.friendsc.mizuka.modules.profile.title;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class TitleCommand {
    public TitleCommand(GuildMessageReceivedEvent event, String[] args) {
        GuildConfig gc = GuildDao.get(event.getGuild());
        UserConfig uc = gc.getUser(event.getAuthor());

        Member member = event.getMember();
        if(uc.isBooster()) {
            if(args.length < 1 || (args[0].equals("") && args.length < 2)) {
                uc.title = "";
                event.getMessage().reply(uc.language == Language.FRENCH ? "**Titre réinitlisé !**\nUtilise `.title <titre personnalisé>` pour mettre à jour ton titre !" : "**Title removed!**\nUse `.title <custom title>` to update your title !").complete();
            } else {
                StringBuilder sb = new StringBuilder();
                for(String arg : args) {
                    sb.append(" ").append(arg);
                }
                String title = sb.toString().trim();
                if(title.length() > 50) { event.getMessage().reply(uc.language == Language.FRENCH ? "Ton titre ne peut pas dépasser 50 caractères." : "Your title cannot be longer than 50 caracters.").complete(); return; }

                uc.title = title;
                uc.push();
                event.getMessage().reply(uc.language == Language.FRENCH ? "Titre actualisé !" : "Title updated !").complete();
            }
        } else {
            event.getMessage().reply(uc.language == Language.FRENCH ? "Cette commande est réservée aux Friends Boosters." : "This command is exclusive to Friends Boosters.").complete();
        }
    }
}
