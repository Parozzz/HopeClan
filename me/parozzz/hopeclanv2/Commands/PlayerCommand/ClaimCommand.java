/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import me.parozzz.hopeclanv2.ClanEnumManager;
import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimManager;
import me.parozzz.hopeclanv2.CommandManager;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Events.ClaimChunkEvent;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Paros
 */
public class ClaimCommand implements PlayerCommand
{  
    @Override
    public String getCommand() 
    {
        return CommandType.CLAIM.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.CLAIMHELP.chat(hp);
    }

    @Override
    public boolean canBeUsed(HPlayer hp, boolean muted) 
    {
        if(hp.getClan()==null)
        {
            CommandMessageEnum.PLAYERNOTINCLAN.chat(hp);
            return false;
        }
        else if(hp.getClan().getRank(hp).getPermissionLevel()<Rank.MODERATOR.getPermissionLevel())
        {
            CommandMessageEnum.COMMANDMODERATORONLY.chat(hp);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(HPlayer hp, String[] val) 
    {
        Claim claim=ClaimManager.get(hp.getPlayer().getLocation().getChunk());
        if(claim!=null)
        {
            CommandMessageEnum.CLAIMALREADY.chat(hp);
        }
        else
        {
            Double cost=CommandManager.getCost(hp.getClan().claimList().size());
            if(cost==null)
            {
                CommandMessageEnum.CLAIMMAXREACHED.chat(hp);
            }
            else
            {
                Utils.callEvent(new ClaimChunkEvent(hp, hp.getClan(), cost, hp.getPlayer().getLocation().getChunk()));
            }
        }
        return true;
    }
    
}
