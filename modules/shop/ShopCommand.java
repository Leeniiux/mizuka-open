package fr.friendsc.mizuka.modules.shop;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Calendar;
import java.util.TimeZone;


public class ShopCommand {
    public ShopCommand(GuildMessageReceivedEvent event) {
        String[] lines = {
                "W      e `      o k`  x      ,  h        fl   .     p            !",
                "Wh    he `      ork` ex      , th  mo    fl  s..   app      y    !",
                "Wh n the `.  re ork` expl    , the mo  y flows... Happ  n w y  r !",
                "Whe  the `.  re ork` expl  es, the mo  y flows... Happy n w ye r !",
                "Whe  the `.  re ork` explodes, the mon y flows... Happy new ye r !",
                "When the `.  rework` explodes, the mon y flows... Happy new year !",
                "When the `.firework` explodes, the money flows... Happy new year !",
        };

        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(day >= 26) event.getMessage().reply(lines[day-26]).queue();
    }
}
