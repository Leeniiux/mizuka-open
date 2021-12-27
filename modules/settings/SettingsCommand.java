package fr.friendsc.mizuka.modules.settings;

import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.utils.Emoji;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


public class SettingsCommand {
    private GuildConfig gc;
    private UserConfig uc;
    private GuildMessageReceivedEvent event;
    private String[] args;

    public SettingsCommand(GuildMessageReceivedEvent event, String[] args) {
        Member member = event.getMember();
        if(member == null || !member.hasPermission(Permission.ADMINISTRATOR)) {
            event.getMessage().replyEmbeds(new EmbedBuilder().setColor(0x303037).setDescription("").build()).queue();
            return;
        }

        this.gc = GuildDao.get(event.getGuild());
        this.uc = gc.getUser(event.getAuthor());
        this.event = event;
        this.args = args;

        if(args.length < 1) {
            help();
        } else {
            switch(args[0].toLowerCase()) {
                case "support": support(); break;
                case "support-category": supportCategory(); break;
                case "donator": donator(); break;
                case "supporter": supporter(); break;
                case "verification": verification(); break;
                case "voicechat": voicechat(); break;
                case "channel-fr": frenchChannel(); break;
                case "channel-en": englishChannel(); break;
                case "welcome-fr": frenchWelcome(); break;
                case "welcome-en": englishWelcome(); break;
                case "pingounet": pingounet(); break;
            }
        }
    }

    private void help() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x303037);
        embed.setTitle(uc.language == Language.FRENCH ? "Mizuka - Paramètres" : "Mizuka - Settings");
        embed.setDescription(
                "`.settings support <id>` *Ajouter/Retirer un message de support*\n" +
                "`.settings support-category` *Initialiser la catégorie de ticket*\n" +
                "`.settings donator <@role|id>` *Définir le rôle donateur*\n" +
                "`.settings supporter <@role|id>` *Définir le rôle supporter*\n" +
                "`.settings verification <@role|id>` *Définir le rôle de vérification*\n" +
                "`.settings pingounet <@role|id>` *Définir le rôle Pingounet*\n" +
                "`.settings voicechat <id>` *Définir le salon de création VoiceChat*\n" +
                "`.settings channel-fr <id>` *Définir le salon d'accueil français*\n" +
                "`.settings channel-en <id>` *Définir le salon d'accueil anglais*\n" +
                "`.settings welcome-fr <message>` *Définir le message d'accueil français (%user% pour mentionner)*\n" +
                "`.settings welcome-en <message>` *Définir le salon d'accueil anglais (%user% pour mentionner)*\n"
        );
        event.getMessage().replyEmbeds(embed.build()).queue();
    }

    private void support() {
        if(args.length < 2) {
            help();
            return;
        }
        if(gc.support_messages.contains(args[1])) {
            gc.support_messages.remove(args[1]);
        } else {
            gc.support_messages.add(args[1]);
        }
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void supportCategory() {
        if(args.length < 2) {
            help();
            return;
        }
        gc.support_category = args[1];
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void donator() {
        if(args.length < 2) {
            help();
            return;
        }
        Role r;
        if(event.getMessage().getMentionedRoles().isEmpty()) {
            try {
                r = event.getGuild().getRoleById(args[1]);
            } catch(IllegalArgumentException ignore) {
                event.getMessage().addReaction(Emoji.X).queue();
                return;
            }
        } else {
            r = event.getMessage().getMentionedRoles().get(0);
        }
        if(r == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.donator_role = r.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void supporter() {
        if(args.length < 2) {
            help();
            return;
        }
        Role r;
        if(event.getMessage().getMentionedRoles().isEmpty()) {
            try {
                r = event.getGuild().getRoleById(args[1]);
            } catch(IllegalArgumentException ignore) {
                event.getMessage().addReaction(Emoji.X).queue();
                return;
            }
        } else {
            r = event.getMessage().getMentionedRoles().get(0);
        }
        if(r == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.supporter_role = r.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void verification() {
        if(args.length < 2) {
            help();
            return;
        }
        Role r;
        if(event.getMessage().getMentionedRoles().isEmpty()) {
            try {
                r = event.getGuild().getRoleById(args[1]);
            } catch(IllegalArgumentException ignore) {
                event.getMessage().addReaction(Emoji.X).queue();
                return;
            }
        } else {
            r = event.getMessage().getMentionedRoles().get(0);
        }
        if(r == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.verification_role = r.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void voicechat() {
        if(args.length < 2) {
            help();
            return;
        }

        VoiceChannel vc;
        try {
            vc = event.getGuild().getVoiceChannelById(args[1]);
        } catch(IllegalArgumentException ignore) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        if(vc == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.voicechat_channel = vc.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void frenchChannel() {
        if(args.length < 2) {
            help();
            return;
        }

        TextChannel tc;
        try {
            tc = event.getGuild().getTextChannelById(args[1]);
        } catch(IllegalArgumentException ignore) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        if(tc == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.french_channel = tc.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void englishChannel() {
        if(args.length < 2) {
            help();
            return;
        }

        TextChannel tc;
        try {
            tc = event.getGuild().getTextChannelById(args[1]);
        } catch(IllegalArgumentException ignore) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        if(tc == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.english_channel = tc.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void frenchWelcome() {
        if(args.length < 2) {
            gc.french_welcome = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i=1; i<args.length; i++) sb.append(" ").append(args[i]);
            gc.french_welcome = sb.toString().trim();
        }
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void englishWelcome() {
        if(args.length < 2) {
            gc.english_welcome = "";
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i=1; i<args.length; i++) sb.append(" ").append(args[i]);
            gc.english_welcome = sb.toString().trim();
        }
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }

    private void pingounet() {
        if(args.length < 2) {
            help();
            return;
        }
        Role r;
        if(event.getMessage().getMentionedRoles().isEmpty()) {
            try {
                r = event.getGuild().getRoleById(args[1]);
            } catch(IllegalArgumentException ignore) {
                event.getMessage().addReaction(Emoji.X).queue();
                return;
            }
        } else {
            r = event.getMessage().getMentionedRoles().get(0);
        }
        if(r == null) {
            event.getMessage().addReaction(Emoji.X).queue();
            return;
        }
        gc.pingounet_role = r.getId();
        gc.push();
        event.getMessage().addReaction(Emoji.V).queue();
    }
}
