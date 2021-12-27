package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.CommandListener;
import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import fr.friendsc.mizuka.modules.experience.ExperienceTools;
import fr.friendsc.mizuka.modules.supporter.SupporterEvent;
import fr.friendsc.mizuka.utils.XP;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class onGuildMessageReceived {
    public onGuildMessageReceived(GuildMessageReceivedEvent event) {
        User author = event.getAuthor();
        if(author.isSystem() || author.isBot()) return;

        //COMMAND LISTENER
        new CommandListener(event);

        //SUPPORTER UPDATE
        new SupporterEvent(event.getGuild(), event.getMember());

        //EXPERIENCE
        ExperienceTools.gainXP(XP.CHAT, event.getGuild(), author);

        //ACHIEVEMENTS
        AchievementTools.firstMessage(event);
        AchievementTools.firstImage(event);
        AchievementTools.welcomeNewcomer(event);
    }
}
