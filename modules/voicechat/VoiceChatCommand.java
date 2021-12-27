package fr.friendsc.mizuka.modules.voicechat;

import fr.friendsc.mizuka.Mizuka;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class VoiceChatCommand {
    GuildMessageReceivedEvent event;
    String[] args;
    GuildConfig gc;
    UserConfig uc;
    VoiceChannel vc;

    public VoiceChatCommand(GuildMessageReceivedEvent event, String[] args) {
        this.event = event;
        this.args = args;
        this.gc = GuildDao.get(event.getGuild());
        this.uc = gc.getUser(event.getAuthor());

        try {
            if(args.length == 0) {
                help();
                return;
            }
            if(event.getMember().getVoiceState().inVoiceChannel()) {
                vc = event.getMember().getVoiceState().getChannel();
                if(VoiceChat.CHANNELS.containsKey(vc.getId())) {
                    if(VoiceChat.CHANNELS.get(vc.getId()).equals(event.getAuthor().getId())) {
                        switch(args[0].toLowerCase()) {
                            case "limit": limit(); break;
                            case "kick": kick(); break;
                            case "rename": rename(); break;
                            case "lock": lock(); break;
                            case "unlock": unlock(); break;
                            case "permit": permit(); break;
                            case "claim": claim(); break;
                            case "bitrate": bitrate(); break;
                            default: help(); break;
                        }
                    } else {
                        if(args[0].equals("claim")) {
                            claim();
                        } else {
                            event.getMessage().reply(uc.language == Language.FRENCH ? "Vous n'avez pas la permission de modifier ce salon." : "You do not have enough permissions to edit this channel.").complete();
                        }
                    }
                } else { event.getMessage().reply(uc.language == Language.FRENCH ? "Le salon n'est pas modifiable." : "This channel cannot be edited.").complete(); }
            } else { event.getMessage().reply(uc.language == Language.FRENCH ? "Vous n'êtes pas connecté dans un salon vocal !" : "You are not connected to a voice channel.").complete(); }
        } catch(NullPointerException exception) { event.getMessage().reply(uc.language == Language.FRENCH ?  "Vous n'êtes pas connecté dans un salon vocal !" : "You are not connected to a voice channel.").complete(); }
    }

    public void limit() {
        if(args.length < 2) {
            help();
        } else {
            try {
                int n = Integer.parseInt(args[1]);
                if(n < 0 || n > 99) {
                    event.getMessage().reply(uc.language == Language.FRENCH ? "La limite doit être comprise entre 0 et 99 inclus." : "Limit must be between 0 and 99.").complete();
                } else {
                    vc.getManager().setUserLimit(n).complete();
                    event.getMessage().reply(uc.language == Language.FRENCH ? "Nouvelle limite définie à " + n + " !" : "New limit set to " + n + " !").complete();
                }
            } catch(NumberFormatException exception) {
                event.getMessage().reply(uc.language == Language.FRENCH ? "Modification impossible !" : "Could not edit !").complete();
            }
        }
    }

    public void kick() {
        if(!event.getMessage().getMentionedMembers().isEmpty()) {
            try {
                VoiceChannel targetVC = event.getMessage().getMentionedMembers().get(0).getVoiceState().getChannel();
                if(targetVC.getId().equals(vc.getId())) {
                    event.getGuild().kickVoiceMember(event.getMessage().getMentionedMembers().get(0)).complete();
                    vc.getManager().putPermissionOverride(event.getMessage().getMentionedMembers().get(0), null, Collections.singleton(Permission.VOICE_CONNECT)).complete();
                }
            } catch(NullPointerException e) { event.getChannel().sendMessage(uc.language == Language.FRENCH ? "L'utilisateur mentionné n'est pas connecté au salon." : "Targeted user is not connected to this channel.").complete(); }
        } else { event.getChannel().sendMessage(uc.language == Language.FRENCH ? "Aucun utilisateur mentionné." : "No mentioned user.").complete(); }
    }

    public void rename() {
        if(args.length < 2) {
            vc.getManager().setName(uc.language == Language.FRENCH ? "Salon de " + event.getAuthor().getName() : event.getAuthor().getName() + "'s channel").queue();
            uc.voicename = "";
            uc.push();
            event.getMessage().reply(uc.language == Language.FRENCH ? "Nom réinitialisé." : "Name reset.").complete();
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i=1; i<args.length; i++) {
                sb.append(" ").append(args[i]);
            }
            String value = sb.toString().trim();
            if(value.length() > 0 && value.length() <= 100) {
                vc.getManager().setName(value).queue();
                uc.voicename = value;
                uc.push();
                event.getMessage().reply(uc.language == Language.FRENCH ? "Nom modifié : `" + value + "`" : "Name edited : `" + value + "`").complete();
            } else { event.getChannel().sendMessage(uc.language == Language.FRENCH ? "Le nom ne peut pas dépasser 100 caractères." : "Channel name cannot be more than 100 characters long.").complete(); }
        }
    }

    public void lock() {
        vc.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singleton(Permission.VOICE_CONNECT)).complete();
        event.getMessage().reply(uc.language == Language.FRENCH ? "Salon verrouillé." : "Channel locked").complete();
    }

    public void unlock() {
        vc.getManager().putPermissionOverride(event.getGuild().getPublicRole(), Collections.singleton(Permission.VOICE_CONNECT), null).complete();
        event.getMessage().reply(uc.language == Language.FRENCH ? "Salon déverrouillé." : "Channel unlocked").complete();
    }

    public void permit() {
        if(event.getMessage().getMentionedMembers().isEmpty()) {
            event.getMessage().reply(uc.language == Language.FRENCH ? "Utilisateur introuvable." : "No member found.").complete();
            return;
        }

        Member target = event.getMessage().getMentionedMembers().get(0);
        vc.getManager().putPermissionOverride(target, Collections.singleton(Permission.VOICE_CONNECT), null).complete();
        event.getMessage().reply(uc.language == Language.FRENCH ? "Cet utilisateur peut maintenant rejoindre." : "This member can now join.").complete();
    }

    public void bitrate() {
        Member member = event.getMember();
        if((member != null && member.getTimeBoosted() != null) || uc.isDonator()) {
            if(args.length < 2) { event.getMessage().reply(uc.language==Language.FRENCH ? "Utilise `.vc bitrate <8000 - " + event.getGuild().getMaxBitrate() + ">`" : "Use `.vc bitrate <8000 - " + event.getGuild().getMaxBitrate() + ">`").complete(); return; }
            try {
                int bitrate = Integer.parseInt(args[1]);
                if(bitrate < 8000 || bitrate > event.getGuild().getMaxBitrate()) {
                    event.getMessage().reply(uc.language==Language.FRENCH ? "Utilise `.vc bitrate <8000 - " + event.getGuild().getMaxBitrate() + ">`" : "Use `.vc bitrate <8000 - " + event.getGuild().getMaxBitrate() + ">`").complete();
                } else {
                    vc.getManager().setBitrate(bitrate).complete();
                    event.getMessage().reply(uc.language==Language.FRENCH ? "Bitrate du salon mis à jour !" : "Channel bitrate updated !").complete();
                }
            } catch(NumberFormatException e) {
                event.getMessage().reply(uc.language==Language.FRENCH ? "Valeur non reconnue." : "Unknown value.").complete();
            }
        } else {
            event.getMessage().reply(uc.language==Language.FRENCH ? "Cette commande est réservée aux Friends Boosters et aux donateurs." : "This command is exclusive to Friends Boosters and donators.").complete();
        }
    }

    public void claim() {
        String ownerId = VoiceChat.CHANNELS.get(vc.getId());

        if(ownerId.equals(event.getAuthor().getId())) {
            event.getMessage().reply(uc.language == Language.FRENCH ? "Tu es déjà propriétaire de ce salon." : "You already are the owner of this channel.").complete();
            return;
        }

        AtomicBoolean found = new AtomicBoolean(false);
        vc.getMembers().forEach(m -> {
            if(m.getId().equals(ownerId)) {
                event.getMessage().reply(uc.language == Language.FRENCH ? "Le propriétaire du salon est toujours dans le salon." : "This channel owner is still connected.").complete();
                found.set(true);
            }
        });
        if(!found.get()) {
            VoiceChat.CHANNELS.replace(vc.getId(), event.getAuthor().getId());
            event.getMessage().reply(uc.language == Language.FRENCH ? "Vous êtes le nouveau propriétaire de ce salon." : "You are the new owner of this channel.").complete();
        }
    }

    public void help() {
        EmbedBuilder embed = new EmbedBuilder().setDescription("").setColor(0x303037);
        if(uc.language == Language.FRENCH) {
            embed.setTitle("VoiceChats - Aide");
            embed.appendDescription("`.vc limit <number>` *Définit la limite d'utilisateurs. (0 pour retirer)*\n");
            embed.appendDescription("`.vc kick <@user>` *Déconnecte et interdit un utilisateur de votre salon vocal.*\n");
            embed.appendDescription("`.vc rename <name>` *Renomme le salon vocal. (laisser vide pour réinitialiser)*\n");
            embed.appendDescription("`.vc lock` *Verrouille le salon vocal.*\n");
            embed.appendDescription("`.vc unlock` *Déverrouille le salon vocal.*\n");
            embed.appendDescription("`.vc permit <@user>` *Autorise un utilisateur à rejoindre.*\n");
            embed.appendDescription("`.vc claim` *Donne la propriété du salon si le propriétaire n'est pas connecté.*\n");
            embed.appendDescription("`.vc bitrate` *Modifie la qualité vocale de votre salon.*");
        } else {
            embed.setTitle("VoiceChats - Help");
            embed.appendDescription("`.vc limit <number` *Sets a new user limit. (0 to reset)*\n");
            embed.appendDescription("`.vc kick <@user>` *Disconnects and denies a user from your channel.*\n");
            embed.appendDescription("`.vc rename <name>` *Renames your channel. (leave empty to reset)*\n");
            embed.appendDescription("`.vc lock` *Locks your channel.*\n");
            embed.appendDescription("`.vc unlock` *Unlocks your channel.*\n");
            embed.appendDescription("`.vc permit <@user>` *Allows a member to join your channel.*\n");
            embed.appendDescription("`.vc claim` *Gives you ownership of this channel if the owner is not connected.*\n");
            embed.appendDescription("`.vc bitrates` *Modifies your channel's audio quality.*");
        }
        event.getChannel().sendMessageEmbeds(embed.build()).complete();
    }
}
