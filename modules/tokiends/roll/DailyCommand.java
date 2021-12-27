package fr.friendsc.mizuka.modules.tokiends.roll;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;

public class DailyCommand {
    public DailyCommand(GuildMessageReceivedEvent event) {
        GuildConfig gc = GuildDao.get(event.getGuild());
        UserConfig uc = gc.getUser(event.getAuthor());

        if(uc.canDaily()) {
            int amount = 200 + (int) (Math.random()*300);
            double rand = Math.random();
            int factor = rand < 0.8 ? 1 : rand < 0.95 ? 2 : 3;

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(factor == 1 ? 0x303037 : factor == 2 ? 0x9e2ab8 : 0xb8902a);
            if(uc.language == Language.FRENCH) {
                embed.setTitle(factor == 1 ? null : factor == 2 ? "JACKPOT RARE" : "JACKPOT LÉGENDAIRE");
                embed.setDescription("Tu viens d'empocher **`" + amount*factor + "`** tokiends !");
                embed.setFooter("Utilise-les dans la boutique : .shop");
            } else {
                embed.setTitle(factor == 1 ? null : factor == 2 ? "RARE JACKPOT" : "LEGENDARY JACKPOT");
                embed.setDescription("You just earned **`" + amount*factor + "`** tokiends !");
                embed.setFooter("Use them in the store : .shop");
            }
            event.getMessage().replyEmbeds(embed.build()).complete();

            uc.tokiends += (long) amount*factor;
            uc.daily = Instant.now().getEpochSecond();
            uc.push();
        } else {
            if(uc.language == Language.FRENCH) {
                event.getMessage().reply("Désolé, ton prochain `.daily` sera disponible dans `" + uc.getDailyString() + "`").complete();
            } else {
                event.getMessage().reply("Sorry, your next `.daily` will be available in `" + uc.getDailyString() + "`").complete();
            }
        }
    }
}
