package fr.friendsc.mizuka.modules;

import fr.friendsc.mizuka.modules.admin.donator.DonatorCommand;
import fr.friendsc.mizuka.modules.admin.tokiends.TokiendsCommand;
import fr.friendsc.mizuka.modules.fun.FireworkCommand;
import fr.friendsc.mizuka.modules.fun.GiftCommand;
import fr.friendsc.mizuka.modules.fun.SnowballCommand;
import fr.friendsc.mizuka.modules.help.HelpCommand;
import fr.friendsc.mizuka.modules.pingounet.PingounetCommand;
import fr.friendsc.mizuka.modules.profile.ProfileCommand;
import fr.friendsc.mizuka.modules.admin.badges.BadgeCommand;
import fr.friendsc.mizuka.modules.profile.language.LanguageCommand;
import fr.friendsc.mizuka.modules.raidmode.RaidmodeCommand;
import fr.friendsc.mizuka.modules.settings.PrefixCommand;
import fr.friendsc.mizuka.modules.settings.SettingsCommand;
import fr.friendsc.mizuka.modules.profile.title.TitleCommand;
import fr.friendsc.mizuka.modules.shop.ShopCommand;
import fr.friendsc.mizuka.modules.tokiends.roll.DailyCommand;
import fr.friendsc.mizuka.modules.tokiends.roll.RollCommand;
import fr.friendsc.mizuka.modules.voicechat.VoiceChatCommand;
import fr.friendsc.mizuka.storage.GuildConfig;
import fr.friendsc.mizuka.storage.GuildDao;
import fr.friendsc.mizuka.utils.Settings;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.CompletableFuture;

public class CommandListener {
    private static int running = 0; //Amount of running commands (async)

    private GuildConfig gc;
    private GuildMessageReceivedEvent event;
    private String command;
    private String[] args;

    public CommandListener(GuildMessageReceivedEvent event) {

        CompletableFuture.runAsync(() -> {
            if(running > 5) return;
            //START IF COMMAND
            this.gc = GuildDao.get(event.getGuild());
            this.event = event;
            parse();
            if(command == null) return;
            running++;
            //COMMAND
            listen();
            //END OF COMMAND
            running--;
        });
    }

    private void parse() {
        String message = event.getMessage().getContentRaw();
        String prefix = gc.prefix.equals("") ? Settings.PREFIX : gc.prefix;

        if(message.startsWith(prefix)) {
            int index = message.indexOf(' ');
            command = message.substring(Settings.PREFIX.length(), index == -1 ? message.length() : index);
            String temp = message.replaceFirst(prefix + command, "").trim();
            if(temp.isEmpty()) args = new String[]{};
            else args = temp.split(" ");
        }
    }

    private void listen() {
        switch(command) {
            case "help": new HelpCommand(event, args); break;
            case "profile": case "p": new ProfileCommand(event); break;

            //INTRODUCTION
            //case "intro": new IntroCommand(event); break;

            //ROLLS
            case "daily": new DailyCommand(event); break;
            case "roll": new RollCommand(event); break;

            //PROFILE SETTINGS
            case "title": new TitleCommand(event, args); break;
            case "language": new LanguageCommand(event, args); break;
            //case "achievements": new AchievementsCommand(event); break;

            //SHOP
            case "shop": new ShopCommand(event); break;

            //VOICECHAT
            case "voicechat": case "vc": new VoiceChatCommand(event, args); break;

            //USER GESTION
            case "badge": new BadgeCommand(event, args); break;
            case "tokiends": new TokiendsCommand(event, args); break;
            case "donator": new DonatorCommand(event); break;

            case "prefix": new PrefixCommand(event, args); break;
            case "settings": new SettingsCommand(event, args); break;
            case "raidmode": new RaidmodeCommand(event, args); break;

            //Miscellaneous
            case "firework": case "fw": new FireworkCommand(event); break;
            case "snowball": new SnowballCommand(event); break;
            case "gift": new GiftCommand(event); break;
            case "pingounet": new PingounetCommand(event, args); break;
        }
    }
}
