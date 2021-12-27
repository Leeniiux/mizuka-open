package fr.friendsc.mizuka.modules.raidmode;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RaidmodeCommand {
    public RaidmodeCommand(GuildMessageReceivedEvent event, String[] args) {
        Member member = event.getMember();
        if(member == null || !member.hasPermission(Permission.ADMINISTRATOR)) {
            event.getMessage().replyEmbeds(new EmbedBuilder().setColor(0x303037).setDescription("").build()).queue();
            return;
        }

        GuildConfig gc = GuildDao.get(event.getGuild());
        UserConfig uc = gc.getUser(event.getAuthor());

        if(args.length > 0) {
            event.getMessage().replyEmbeds(
                !gc.raidmode ?
                    new EmbedBuilder().setColor(0x303037).setDescription(
                        uc.language == Language.FRENCH ? Emoji.X + " Raidmode désactivé !" :
                        Emoji.X + " Raidmode is disabled !"
                    ).build()
                :
                    new EmbedBuilder().setColor(0x303037).setDescription(
                        uc.language == Language.FRENCH ? Emoji.V + " Raidmode activé !" :
                        Emoji.V + " Raidmode is enabled !"
                    ).build()
            ).queue();
            return;
        }

        event.getMessage().replyEmbeds(
                gc.raidmode ?
                    new EmbedBuilder().setColor(0x303037).setDescription(
                        uc.language == Language.FRENCH ? Emoji.X + " Raidmode désactivé avec succès !" :
                        Emoji.X + " Raidmode successfully disabled !"
                    ).build()
                :
                    new EmbedBuilder().setColor(0x303037).setDescription(
                        uc.language == Language.FRENCH ? Emoji.V + " Raidmode activé avec succès !" :
                        Emoji.V + " Raidmode successfully enabled !"
                    ).build()
        ).queue();
        gc.raidmode = !gc.raidmode;
        gc.push();
    }
}
