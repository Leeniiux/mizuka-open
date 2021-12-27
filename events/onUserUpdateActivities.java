package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.supporter.SupporterEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;

public class onUserUpdateActivities {
    public onUserUpdateActivities(UserUpdateActivitiesEvent event) {
        new SupporterEvent(event.getGuild(), event.getMember());
    }
}
