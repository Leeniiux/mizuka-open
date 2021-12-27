package fr.friendsc.mizuka.modules.profile.language;

import fr.friendsc.mizuka.Mizuka;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class LanguageCommand {
    private GuildMessageReceivedEvent event;
    private String[] args;

    public LanguageCommand(GuildMessageReceivedEvent event, String[] args) {
        this.event = event;
        this.args = args;

        if(args.length < 1) {
            help();
        } else {
            switch(args[0].toLowerCase()) {
                case "en": case "gb": case "english": case "eng": english(); break;
                case "fr": case "french": case "france": case "français": case "francais": french(); break;
                default: help(); break;
            }
        }
    }

    private void help() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setDescription(Emoji.FLAG_FR + " Utilise `.language fr` pour passer la langue du bot en français\n" + Emoji.FLAG_GB + " Use `.language en` to switch your bot language to english");
        embed.setColor(0x303037);
        event.getChannel().sendMessageEmbeds(embed.build()).complete();
    }

    private void english() {
        event.getMessage().reply("Mizuka just switched to english for you!").complete();
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        uc.language = Language.ENGLISH;
        uc.push();
    }

    private void french() {
        event.getMessage().reply("Mizuka vient de passer en français pour toi !").complete();
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        uc.language = Language.FRENCH;
        uc.push();
    }
}
