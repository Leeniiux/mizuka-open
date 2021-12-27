package fr.friendsc.mizuka.modules.achievements;

import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AchievementTools {

    private static void alert(TextChannel channel, String s, @Nullable Integer time) {
        Message msg = channel.sendMessageEmbeds(new EmbedBuilder().setColor(0x303037).setTitle("SUCCÈS DÉVERROUILLÉ !").setDescription(s).build()).complete();
        if(time != null) msg.delete().queueAfter(time, TimeUnit.SECONDS);
    }

    public static void firstMessage(GuildMessageReceivedEvent event) {
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        if(!uc.achievements.contains(Achievement.FIRST_MESSAGE)) {
            uc.achievements.add(Achievement.FIRST_MESSAGE);
            uc.push();
        }
    }

    public static void firstImage(GuildMessageReceivedEvent event) {
        if(event.getMessage().getAttachments().isEmpty()) return;
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        if(!uc.achievements.contains(Achievement.FIRST_IMAGE)) {
            for(Message.Attachment a : event.getMessage().getAttachments()) {
                if(a.isImage()) {
                    uc.achievements.add(Achievement.FIRST_IMAGE);
                    uc.push();
                    return;
                }
            }
        }
    }

    public static void firstVoice(GuildVoiceJoinEvent event) {
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getMember().getUser());
        if(!uc.achievements.contains(Achievement.FIRST_VOICE)) {
            uc.achievements.add(Achievement.FIRST_VOICE);
            uc.push();
        }
    }

    public static void welcomeNewcomer(GuildMessageReceivedEvent event) {
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        String msg = event.getMessage().getContentRaw().toLowerCase();
        if(msg.contains("bienvenue") || msg.contains("welcome")) {
            if(!uc.achievements.contains(Achievement.WELCOME_NEWCOMER)) {
                uc.achievements.add(Achievement.WELCOME_NEWCOMER);
                uc.push();
            }
        }
    }

    public static void snowballSelf(GuildMessageReceivedEvent event) {
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        if(!uc.achievements.contains(Achievement.SNOWBALL_SELF)) {
            uc.achievements.add(Achievement.SNOWBALL_SELF);
            uc.push();
            AchievementTools.alert(event.getChannel(), event.getAuthor().getAsMention() + " vient de débloquer le succès : **Oups ! J'ai glissé...**", 5);
        }
    }

    public static void receiveGifts(GuildMessageReceivedEvent event, UserConfig uc) {
        if(uc.gifts >= 3) {
            if(!uc.achievements.contains(Achievement.RECEIVE_THREE_GIFTS)) {
                uc.achievements.add(Achievement.RECEIVE_THREE_GIFTS);
                uc.push();
                AchievementTools.alert(event.getChannel(), event.getAuthor().getAsMention() + " vient de débloquer le succès : **Le Père Noël est généreux !**", 5);
            }
        }
    }

    public static void giftSomeone(GuildMessageReceivedEvent event, UserConfig uc) {
        if(!uc.achievements.contains(Achievement.GIFT_SOMEONE)) {
            uc.achievements.add(Achievement.GIFT_SOMEONE);
            uc.push();
        }
    }

    public static void fireworkBeforeYear(GuildMessageReceivedEvent event) {
        UserConfig uc = GuildDao.get(event.getGuild()).getUser(event.getAuthor());
        if(!uc.achievements.contains(Achievement.FIREWORK_BEFORE_NEW_YEAR)) {
            uc.achievements.add(Achievement.FIREWORK_BEFORE_NEW_YEAR);
            uc.push();
            AchievementTools.alert(event.getChannel(), event.getAuthor().getAsMention() + " vient de débloquer le succès : **Sortez le champagne !**", 5);
        }
    }

    public static void becomeDonator(GuildMessageReceivedEvent event, UserConfig uc) {
        if(!uc.achievements.contains(Achievement.BECOME_DONATOR)) {
            uc.achievements.add(Achievement.BECOME_DONATOR);
            uc.push();
            AchievementTools.alert(event.getChannel(), event.getAuthor().getAsMention() + " vient de débloquer le succès : **Money money !**", 5);
        }
    }

    public static void pingounetRole(GuildMessageReceivedEvent event, UserConfig uc) {
        if(!uc.achievements.contains(Achievement.GET_PINGOUNET)) {
            uc.achievements.add(Achievement.GET_PINGOUNET);
            uc.push();
            AchievementTools.alert(event.getChannel(), event.getAuthor().getAsMention() + " vient de débloquer le succès : **AHHH ! Encore une mention...**", 5);
        }
    }
}
