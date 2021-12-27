package fr.friendsc.mizuka.modules.logs;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.utils.Emoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

import java.time.Instant;

public class LogsTools {

    public static void memberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        if(gc.raidmode) return;
        TextChannel tc = guild.getTextChannelById(gc.logs_entries_channel);
        User user = event.getUser();
        if(tc == null) return;

        tc.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(0xC186FF)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription(Emoji.INBOX + " " + user.getAsMention() + "\n`ID` " + user.getId())
                        .build()
        ).queue();
    }

    public static void memberJoinWhileRaidmode(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        if(!gc.raidmode) return;
        TextChannel tc = guild.getTextChannelById(gc.logs_entries_channel);
        User user = event.getUser();
        if(tc == null) return;

        tc.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(0xFFBD47)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription(Emoji.OUTBOX + " " + user.getAsMention() + "\n`ID` " + user.getId() + "\n*Raidmode enabled !*")
                        .build()
        ).queue();
    }

    public static void memberLeft(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        if(gc.raidmode) return;
        TextChannel tc = guild.getTextChannelById(gc.logs_entries_channel);
        User user = event.getUser();
        if(tc == null) return;

        tc.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(0xFF4E4E)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription(Emoji.OUTBOX + " " + user.getAsMention() + "\n`ID` " + user.getId())
                        .build()
        ).queue();
    }

    public static void memberVoiceJoin(GuildVoiceJoinEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        TextChannel tc = guild.getTextChannelById(gc.logs_voice_channel);
        if(tc == null) return;
        Member member = event.getMember();
        VoiceChannel vc = event.getChannelJoined();

        tc.sendMessageEmbeds(
                new EmbedBuilder()
                        .setAuthor(member.getUser().getAsTag(), null, member.getAvatarUrl())
                        .setColor(0x64FF47)
                        .setDescription("`CHANNEL` " + vc.getAsMention() + "\n`ID` " + vc.getId())
                        .setFooter("User • " + member.getId())
                        .setTimestamp(Instant.now())
                        .build()
        ).queue();
    }

    public static void memberVoiceMove(GuildVoiceMoveEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        TextChannel tc = guild.getTextChannelById(gc.logs_voice_channel);
        if(tc == null) return;
        Member member = event.getMember();
        VoiceChannel left = event.getChannelLeft();
        VoiceChannel joined = event.getChannelJoined();

        tc.sendMessageEmbeds(
                new EmbedBuilder()
                        .setAuthor(member.getUser().getAsTag(), null, member.getAvatarUrl())
                        .setColor(0xFFBD47)
                        .setDescription("`LEFT` " + left.getAsMention() + " " + left.getId() + "\n`JOINED` " + joined.getAsMention() + " " + joined.getId())
                        .setFooter("User • " + member.getId())
                        .setTimestamp(Instant.now())
                        .build()
        ).queue();
    }

    public static void memberVoiceLeave(GuildVoiceLeaveEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        TextChannel tc = guild.getTextChannelById(gc.logs_voice_channel);
        if(tc == null) return;
        Member member = event.getMember();
        VoiceChannel vc = event.getChannelLeft();

        tc.sendMessageEmbeds(
                new EmbedBuilder()
                        .setAuthor(member.getUser().getAsTag(), null, member.getAvatarUrl())
                        .setColor(0xFF4040)
                        .setDescription("`CHANNEL` " + vc.getAsMention() + "\n`ID` " + vc.getId())
                        .setFooter("User • " + member.getId())
                        .setTimestamp(Instant.now())
                        .build()
        ).queue();
    }
}
