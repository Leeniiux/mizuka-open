package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.support.SupportEvent;
import fr.friendsc.mizuka.modules.welcome.WelcomeEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class onGuildMessageReactionAdd {
    public onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        //VERIFICATION
        WelcomeEvent.verification(event);

        //SUPPORT
        if(event.getUser().isBot() || event.getUser().isSystem()) return;
        new SupportEvent(event);
    }
}
