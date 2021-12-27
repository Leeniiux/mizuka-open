package fr.friendsc.mizuka.modules.help;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand {
    private GuildMessageReceivedEvent event;
    private String[] args;

    public HelpCommand(GuildMessageReceivedEvent event, String[] args) {
        if(args.length == 0) help(0);
        else {
            switch(args[0].toLowerCase()) {
                case "": help(1); break;
            }
        }
    }

    private void help(int page) {
        if(page == 0) {
            
        }
    }
}
