package fr.friendsc.mizuka.events;

import fr.friendsc.mizuka.modules.experience.ExperienceTools;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.time.Instant;

public class onReadyEvent {
    public onReadyEvent(ReadyEvent event) {
        long time = Instant.now().getEpochSecond();
        for(Guild g : event.getJDA().getGuilds()) {
            for(VoiceChannel vc : g.getVoiceChannels()) {
                for(Member m : vc.getMembers()) {
                    ExperienceTools.inVoice.put(m.getId(), time);
                }
            }
        }

        System.out.println("Mizuka has started successfully !");
    }
}
