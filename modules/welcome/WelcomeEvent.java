package fr.friendsc.mizuka.modules.welcome;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.storage.UserDao;
import fr.friendsc.mizuka.utils.Config;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.ArrayList;
import java.util.List;

public class WelcomeEvent {
    private static final List<String> JOINING = new ArrayList<>();

    public static void join(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        GuildConfig gc = GuildDao.get(guild);
        Role verif = guild.getRoleById(gc.verification_role);
        if(verif == null) return;

        JOINING.add(event.getUser().getId());
    }

    public static void verification(GuildMessageReactionAddEvent event) {
        if(!JOINING.contains(event.getUserId())) return;

        if(event.getReactionEmote().getEmoji().equals(Emoji.FLAG_FR)) {
            JOINING.remove(event.getUserId());
            Guild guild = event.getGuild();
            GuildConfig gc = GuildDao.get(guild);
            TextChannel tc = guild.getTextChannelById(gc.french_channel);
            String text = gc.french_welcome;
            Role verif = guild.getRoleById(gc.verification_role);
            if(verif != null) guild.addRoleToMember(event.getMember(), verif).queue();
            if(tc == null || text.isEmpty()) return;
            tc.sendMessage(text.replaceAll("%user%", event.getUser().getAsMention())).queue();
        }
        else if(event.getReactionEmote().getEmoji().equals(Emoji.FLAG_GB)) {
            JOINING.remove(event.getUserId());
            Guild guild = event.getGuild();
            GuildConfig gc = GuildDao.get(guild);
            TextChannel tc = guild.getTextChannelById(gc.french_channel);
            String text = gc.english_welcome;
            Role verif = guild.getRoleById(gc.verification_role);
            if(verif != null) guild.addRoleToMember(event.getMember(), verif).queue();
            if(tc == null || text.isEmpty()) return;
            tc.sendMessage(text.replaceAll("%user%", event.getUser().getAsMention())).queue();

            UserConfig uc = gc.getUser(event.getUser());
            uc.language = Language.ENGLISH;
            uc.push();
        }
    }
}
