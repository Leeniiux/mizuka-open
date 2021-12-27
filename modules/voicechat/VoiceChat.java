package fr.friendsc.mizuka.modules.voicechat;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.storage.UserDao;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VoiceChat {
    public static Map<String, String> CHANNELS = new HashMap<>();

    public static void join(Member member, VoiceChannel vc) {
        GuildConfig gc = GuildDao.get(vc.getGuild());
        if(!vc.getId().equals(gc.voicechat_channel)) return;

        create(member, vc);
    }

    public static void leave(Member member, VoiceChannel vc) {
        if(CHANNELS.containsKey(vc.getId())) {
            if(vc.getMembers().isEmpty()) {
                vc.delete().queue();
                CHANNELS.remove(vc.getId());
            }
        }
    }

    public static void create(Member member, VoiceChannel joined) {
        UserConfig uc = UserDao.get(member.getGuild(), member.getUser());
        String name =
            uc.voicename.equals("") ?
                uc.language == Language.FRENCH ?
                    "Salon de " + member.getEffectiveName() :
                    member.getEffectiveName() + "'s channel" :
                uc.voicename;

        VoiceChannel vc = joined.createCopy().setName(name).complete();
        vc.getManager().putPermissionOverride(member, Collections.singleton(Permission.VOICE_CONNECT), null).complete();
        CHANNELS.put(vc.getId(), member.getId());
        member.getGuild().moveVoiceMember(member, vc).complete();
    }
}
