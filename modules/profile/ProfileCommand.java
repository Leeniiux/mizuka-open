package fr.friendsc.mizuka.modules.profile;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ProfileCommand {
    public ProfileCommand(GuildMessageReceivedEvent event) {
        GuildConfig gc = GuildDao.get(event.getGuild());
        UserConfig uc;
        if(event.getMessage().getMentionedUsers().isEmpty()) uc = gc.getUser(event.getAuthor());
        else uc = gc.getUser(event.getMessage().getMentionedUsers().get(0));

        EmbedBuilder embed = new EmbedBuilder();

        //AUTHOR
        embed.setAuthor(uc.user.getAsTag(), null, uc.user.getEffectiveAvatarUrl());
        embed.setColor(0x303037);
        //TITLE
        if(!uc.title.isEmpty()) embed.setDescription("```" + uc.title + "```");
        //BADGES
        boolean booster = uc.isBooster();
        boolean donator = uc.isDonator();
        boolean supporter = uc.isSupporter();
        if(booster || donator || supporter) embed.appendDescription("`Badges` ");
        if(supporter) embed.appendDescription(Emoji.REMINDER_RIBBON + " ");
        if(donator) embed.appendDescription(Emoji.RIBBON + " ");
        if(booster) embed.appendDescription(Emoji.BOOSTER + " ");
        //EXPERIENCE
        long level = (int) Math.sqrt(uc.experience/20d);
        long xpForLevel = level*level*20;
        long xpNeed = (level+1)*(level+1)*20 - xpForLevel;
        long xpHas = uc.experience - xpForLevel;
        int nbr = (int) (xpHas*5/xpNeed);
        if(uc.language == Language.FRENCH) {
            embed.addField(Emoji.TEST_TUBE + " Expérience", "`niv." + level + "`" + Emoji.PURPLE_SQUARE.repeat(nbr) + Emoji.BLACK_SQUARE.repeat(5-nbr) + "`niv." + (level+1) + "`", true);
            embed.addField(Emoji.TROPHY + " Rang", "Classé #" + uc.getRank(), true);
            if(uc.canDaily()) {
                embed.addField(Emoji.COIN + " Tokiends", "**" + uc.tokiends + "** `.daily` pour remporter des tokiends !", false);
            } else {
                embed.addField(Emoji.COIN + " Tokiends", "**" + uc.tokiends + "** `.daily` dans " + uc.getDailyString(), false);
            }
        } else {
            embed.addField(Emoji.TEST_TUBE + " Experience", "`lvl." + level + "`" + Emoji.PURPLE_SQUARE.repeat(nbr) + Emoji.BLACK_SQUARE.repeat(5-nbr) + "`lvl." + (level+1) + "`", true);
            embed.addField(Emoji.TROPHY + " Ranking", "Rank #" + uc.getRank(), true);
            if(uc.canDaily()) {
                embed.addField(Emoji.COIN + " Tokiends", "**" + uc.tokiends + "** `.daily` to earn tokiends !", false);
            } else {
                embed.addField(Emoji.COIN + " Tokiends", "**" + uc.tokiends + "** `.daily` in " + uc.getDailyString(), false);
            }
        }

        /*
        embed.setColor(uc.profile_color);
        embed.setThumbnail(uc.profile_image);
        embed.setImage(uc.profile_banner);
        */

        event.getMessage().replyEmbeds(embed.build()).queue();
    }


}
