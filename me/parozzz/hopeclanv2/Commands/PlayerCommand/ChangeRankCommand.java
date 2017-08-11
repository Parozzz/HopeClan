/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import java.util.Optional;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Players.PlayerManager;
import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import org.bukkit.Bukkit;

/**
 *
 * @author Paros
 */
public class ChangeRankCommand implements PlayerCommand
{

    @Override
    public String getCommand() 
    {
        return CommandType.RANK.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.RANKHELP.chat(hp);
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
        if(val.length!=2)
        {
            return false;
        }
        
        HPlayer toRank=Optional.ofNullable(Bukkit.getPlayer(val[0])).map(PlayerManager::getOnline).orElseGet(() -> PlayerManager.getOffline(val[0]));
        if(toRank==null)
        {
            CommandMessageEnum.PLAYERINEXISTENT.chat(hp);
        }
        else if(toRank.getClan()==null)
        {
            CommandMessageEnum.PLAYERNOTINCLAN.chat(hp);
        }
        else if(!toRank.getClan().equals(hp.getClan()))
        {
            CommandMessageEnum.PLAYERWRONGCLAN.chat(hp);
        }
        else
        {
            Rank rank=Rank.getByName(val[1]);
            if(rank==null)
            {
                CommandMessageEnum.RANKWRONG.chat(hp);
            }
            else
            {
                hp.getClan().setRank(toRank, rank);
                hp.sendMessage(CommandMessageEnum.RANKCHANGE.get().replace("%player%", hp.getOfflinePlayer().getName()).replace("%rank%", rank.getColor()+rank.getName()));
            }
        }
        return true;
    }
    
}
