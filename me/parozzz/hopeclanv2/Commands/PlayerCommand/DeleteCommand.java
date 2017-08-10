/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import java.util.Optional;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Clans.HClan.Rank;
import me.parozzz.hopeclanv2.CommandManager;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Events.ClanDisbandEvent;
import me.parozzz.hopeclanv2.HopeClan;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Paros
 */
public class DeleteCommand implements PlayerCommand
{

    private final TextComponent text;
    public DeleteCommand()
    {
        text=new TextComponent(CommandMessageEnum.DELETECONFIRM.get());
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan "+getCommand()));
    }
    
    @Override
    public String getCommand() 
    {
        return CommandType.DELETE.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.DELETEHELP.chat(hp);
    }
    
    @Override
    public boolean canBeUsed(HPlayer hp, final boolean muted) 
    {
        if(hp.getClan()==null)
        {
            CommandMessageEnum.PLAYERNOTINCLAN.chat(hp, muted);
            return false;
        }
        else if(hp.getClan().rankGet(hp)!=Rank.OWNER)
        {
            CommandMessageEnum.COMMANDOWNERONLY.chat(hp, muted);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(HPlayer hp, String[] val) 
    {
        if(hp.hasMetadata("DELETE"))
        {
            Utils.callEvent(new ClanDisbandEvent(hp, hp.getClan()));
            hp.removeMetadata("DELETE");
        }
        else
        {
            hp.addMetadata("DELETE", true);
            if(hp.isOnline())
            {
                hp.getPlayer().spigot().sendMessage(text);
            }
            
            new BukkitRunnable()
            {
                @Override
                public void run() 
                {
                    hp.removeMetadata("DELETE");
                }
            }.runTaskLater(JavaPlugin.getPlugin(HopeClan.class), 200L);
        }
        return true;
    }
    
}
