package fr.friendsc.mizuka.modules.fun;

import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class GiftCommand {
    public GiftCommand(GuildMessageReceivedEvent event) {
        GuildConfig gc = GuildDao.get(event.getGuild());
        UserConfig uc = gc.getUser(event.getAuthor());

        if(uc.gifted) {
            event.getMessage().reply(uc.language == Language.FRENCH ? "Toutes vos économies sont déjà parties dans votre dernier cadeau..." : "All you savings went into your last gift...").complete().delete().queueAfter(5, TimeUnit.SECONDS);
            event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
        } else {
            if(event.getMessage().getMentionedUsers().isEmpty()) {
                event.getMessage().reply(uc.language == Language.FRENCH ? "Utilisation : `.gift <@user>`\n*Attention : Vous ne pouvez faire __qu'un seul cadeau__ !*" : "Usage : `.gift <@user>`\n*Warning : You can only gift __one user__ !").complete().delete().queueAfter(7, TimeUnit.SECONDS);
                event.getMessage().delete().queueAfter(7, TimeUnit.SECONDS);
            } else {
                if(event.getMessage().getMentionedUsers().get(0).getId().equals(event.getAuthor().getId())) {
                    event.getMessage().reply(uc.language == Language.FRENCH ? "Ça va, on ne te dérange pas trop ?\nhttps://i.ytimg.com/vi/MEQMYfx_kn8/mqdefault.jpg" : "You good ? I hope we're not disturbing.\nhttps://i.ytimg.com/vi/MEQMYfx_kn8/mqdefault.jpg").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                    event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                    return;
                }
                int amount = 1000 + (int) (Math.random()*500);
                User target = event.getMessage().getMentionedUsers().get(0);
                event.getMessage().reply(uc.language == Language.FRENCH ?
                        Emoji.PRESENT + " Hey, " + target.getAsMention() + " !\n" + event.getAuthor().getAsMention() + " vient de t'offrir **" + amount + " tokiends !** Profites-en bien !" :
                        Emoji.PRESENT + " Hey, " + target.getAsMention() + " !\n" + event.getAuthor().getAsMention() + " just gifted you **" + amount + " tokiends!** Have fun with them!"
                ).queue();
                UserConfig tc = gc.getUser(target);
                tc.tokiends += amount;
                tc.gifts++;
                AchievementTools.receiveGifts(event, tc);
                tc.push();
                uc.gifted = true;

                AchievementTools.giftSomeone(event, uc);
                uc.push();
            }
        }
    }
}
