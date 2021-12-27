package fr.friendsc.mizuka.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class Listener implements EventListener {
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if(event instanceof ReadyEvent) new onReadyEvent((ReadyEvent) event);

        if(event instanceof GuildMemberJoinEvent) new onGuildMemberJoin((GuildMemberJoinEvent) event);
        if(event instanceof GuildMemberRemoveEvent) new onGuildMemberRemove((GuildMemberRemoveEvent) event);

        if(event instanceof GuildMessageReceivedEvent) new onGuildMessageReceived((GuildMessageReceivedEvent) event);
        if(event instanceof GuildMessageUpdateEvent) new onGuildMessageUpdate((GuildMessageUpdateEvent) event);
        if(event instanceof GuildMessageDeleteEvent) new onGuildMessageDelete((GuildMessageDeleteEvent) event);
        if(event instanceof GuildMessageReactionAddEvent) new onGuildMessageReactionAdd((GuildMessageReactionAddEvent) event);

        if(event instanceof UserUpdateActivitiesEvent) new onUserUpdateActivities((UserUpdateActivitiesEvent) event);

        if(event instanceof GuildVoiceJoinEvent) new onGuildVoiceJoin((GuildVoiceJoinEvent) event);
        if(event instanceof GuildVoiceMoveEvent) new onGuildVoiceMove((GuildVoiceMoveEvent) event);
        if(event instanceof GuildVoiceLeaveEvent) new onGuildVoiceLeave((GuildVoiceLeaveEvent) event);
    }
}
