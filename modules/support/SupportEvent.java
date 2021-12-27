package fr.friendsc.mizuka.modules.support;

import fr.friendsc.mizuka.Mizuka;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.storage.UserConfig;
import fr.friendsc.mizuka.storage.UserDao;
import fr.friendsc.mizuka.utils.Language;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SupportEvent {
    private GuildMessageReactionAddEvent event;
    private Map<String, String> emotes = new HashMap<>();
    private GuildConfig gc;

    public SupportEvent(GuildMessageReactionAddEvent event) {
        gc = GuildDao.get(event.getGuild());
        if(gc.support_messages.contains(event.getMessageId())) {
            this.event = event;
            open();
        }
    }

    private void open() {
        emotes.put("\uD83C\uDF97", "pub");
        emotes.put("\uD83D\uDD12", "mod");
        emotes.put("\uD83D\uDCA1", "sugg");
        emotes.put("\uD83C\uDF89", "event");
        emotes.put("\uD83D\uDD28", "bug");
        emotes.put("\uD83C\uDF9F", "ticket");

        if(emotes.containsKey(event.getReactionEmote().getEmoji())) {
            Category category = event.getGuild().getCategoryById(gc.support_category);
            if(category != null) {
                TextChannel channel = category.createTextChannel(emotes.get(event.getReactionEmote().getEmoji()) + "-" + Instant.now().getEpochSecond())
                        .addMemberPermissionOverride(event.getUserIdLong(), Collections.singletonList(Permission.MESSAGE_READ), null)
                        .complete();

                UserConfig uc = UserDao.get(event.getGuild(), event.getUser());
                if(uc.language == Language.FRENCH) {
                    channel.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setColor(0x303037)
                                .setTitle("Nous t'écoutons !")
                                .setDescription("Un problème, une suggestion ? " + event.getUser().getAsMention() + " a ouvert un ticket support.\nDiscute ici avec l'intégralité de l'équipe concernée !")
                                .setFooter("- L'équipe support de FRIENDS.")
                                .build()
                    ).queue();
                } else {
                    channel.sendMessageEmbeds(
                            new EmbedBuilder()
                                    .setColor(0x303037)
                                    .setTitle("We are listening !")
                                    .setDescription("An issue, a suggestion? " + event.getUser().getAsMention() + " opened a support ticket.\nChat here with the entire concerned staff team!")
                                    .setFooter("- FRIENDS Support Team.")
                                    .build()
                    ).queue();
                }

                channel.sendMessage(event.getUser().getAsMention()).complete().delete().queue();
            } else {
                event.getChannel().sendMessage("Aucune catégorie de ticket définie !").complete().delete().queueAfter(3, TimeUnit.SECONDS);
            }
        }
        event.getReaction().removeReaction(event.getUser()).complete();
    }
}
