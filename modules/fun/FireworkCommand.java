package fr.friendsc.mizuka.modules.fun;

import fr.friendsc.mizuka.modules.achievements.AchievementTools;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class FireworkCommand {
    public FireworkCommand(GuildMessageReceivedEvent event) {
        AchievementTools.fireworkBeforeYear(event);
    }
}
