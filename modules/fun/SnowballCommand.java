package fr.friendsc.mizuka.modules.fun;

import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SnowballCommand {
    public SnowballCommand(GuildMessageReceivedEvent event) {
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        if(event.getMessage().getMentionedUsers().isEmpty()) {
            if(uc.language == Language.FRENCH) {
                event.getChannel().sendMessage(Emoji.SNOWBALL + " " + event.getAuthor().getAsMention() + " a lancé une boule de neige dans le salon !").queue();
            } else {
                event.getChannel().sendMessage(Emoji.SNOWBALL + " " + event.getAuthor().getAsMention() + " just threw a snowball in the channel!").queue();
            }
        } else {
            if(event.getMessage().getMentionedUsers().get(0).getId().equals(event.getAuthor().getId())) {
                if(uc.language == Language.FRENCH) {
                    event.getChannel().sendMessage(Emoji.SNOWBALL + " ÇA DOIT FAIRE MAL ! " + event.getAuthor().getAsMention() + " s'est pris sa propre boule de neige...").queue();
                } else {
                    event.getChannel().sendMessage(Emoji.SNOWBALL + " IT MUST HAVE HURT ! " + event.getAuthor().getAsMention() + " just hit themselves with their own snowball...").queue();
                }
                AchievementTools.snowballSelf(event);
            } else {
                if(uc.language == Language.FRENCH) {
                    event.getChannel().sendMessage(Emoji.SNOWBALL + " OUCH ! " + event.getAuthor().getAsMention() + " vient de lancer une boule de neige sur " + event.getMessage().getMentionedUsers().get(0).getAsMention() + " !").queue();
                } else {
                    event.getChannel().sendMessage(Emoji.SNOWBALL + " OOF ! " + event.getAuthor().getAsMention() + " just threw a snowball at " + event.getMessage().getMentionedUsers().get(0).getAsMention() + " !").queue();
                }
            }
        }
    }
}
