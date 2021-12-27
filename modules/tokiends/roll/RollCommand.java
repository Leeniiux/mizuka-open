package fr.friendsc.mizuka.modules.tokiends.roll;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;

public class RollCommand {
    public RollCommand(GuildMessageReceivedEvent event) {
        GuildConfig gc = GuildDao.get(event.getGuild());
        UserConfig uc = gc.getUser(event.getAuthor());
        Member member = event.getMember();
        if(member == null) return;

        if(uc.isBooster() || uc.isDonator()) {
            if(uc.canRoll()) {
                int amount = 800 + (int) (Math.random()*200);
                double rand = Math.random();
                int factor = rand < 0.988 ? 1 : rand < 0.998 ? 5 : 10;

                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(factor == 1 ? 0x303037 : factor == 5 ? 0x9e2ab8 : 0xb8902a);
                if(uc.language == Language.FRENCH) {
                    embed.setTitle(factor == 1 ? null : factor == 5 ? "JACKPOT RARE" : "JACKPOT LÉGENDAIRE");
                    embed.setDescription("Tu viens d'empocher **`" + amount*factor + "`** tokiends !");
                    embed.setFooter("Utilise-les dans la boutique : .shop");
                } else {
                    embed.setTitle(factor == 1 ? null : factor == 5 ? "RARE JACKPOT" : "LEGENDARY JACKPOT");
                    embed.setDescription("You just earned **`" + amount*factor + "`** tokiends !");
                    embed.setFooter("Use them in the store : .shop");
                }
                event.getMessage().replyEmbeds(embed.build()).complete();

                uc.tokiends += (long) amount*factor;
                uc.roll = Instant.now().getEpochSecond();
                uc.push();

            } else {
                if(uc.language == Language.FRENCH) {
                    event.getMessage().reply("Désolé, ton prochain `.roll` sera disponible dans `" + uc.getRollString() + "`").complete();
                } else {
                    event.getMessage().reply("Sorry, your next `.roll` will be available in `" + uc.getRollString() + "`").complete();
                }
            }
        } else { event.getMessage().reply(uc.language == Language.FRENCH ? "La commande `.roll` est réservée aux FriendsBoosters ainsi qu'aux donateurs." : "The `.roll` command is exclusive to FriendsBoosters and donators.").complete(); }
    }
}
