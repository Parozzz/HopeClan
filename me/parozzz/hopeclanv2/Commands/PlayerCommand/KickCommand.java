/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import java.util.Optional;
import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Players.PlayerManager;
import org.bukkit.Bukkit;

/**
 *
 * @author Paros
 */
public class KickCommand implements PlayerCommand
{

    @Override
    public String getCommand() 
    {
        return CommandType.KICK.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.KICKHELP.chat(hp);
    }

    @Override
    public boolean canBeUsed(HPlayer hp, boolean muted) 
    {
        if(hp.getClan()==null)
        {
            CommandMessageEnum.PLAYERNOTINCLAN.chat(hp, muted);
            return false;
        }
        else if(hp.getClan().getRank(hp).getPermissionLevel()<Rank.MODERATOR.getPermissionLevel())
        {
            CommandMessageEnum.COMMANDMODERATORONLY.chat(hp, muted);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(HPlayer hp, String[] val) 
    {
        if(val.length!=1)
        {
            return false;
        }
        
        HPlayer toKick=Optional.ofNullable(Bukkit.getPlayer(val[0])).map(PlayerManager::getOnline).orElseGet(() -> PlayerManager.getOffline(val[0]));
        if(toKick==null)
        {
            CommandMessageEnum.PLAYERINEXISTENT.chat(hp);
        }
        else
        {
            if(!hp.getClan().removeMember(toKick))
            {
                CommandMessageEnum.OTHERPLAYERNOTINCLAN.chat(hp);
            }
            else
            {
                hp.sendMessage(CommandMessageEnum.KICKPLAYER.get().replace("%player%", toKick.getOfflinePlayer().getName()));
            }
        }
        return true;
    }
    
}
