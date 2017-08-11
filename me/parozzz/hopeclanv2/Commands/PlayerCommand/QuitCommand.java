/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Players.HPlayer;

/**
 *
 * @author Paros
 */
public class QuitCommand implements PlayerCommand
{
    @Override
    public String getCommand() 
    {
        return CommandType.QUIT.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.QUITHELP.chat(hp);
    }

    @Override
    public boolean canBeUsed(HPlayer hp, boolean muted) 
    {
        if(hp.getClan()==null)
        {
            CommandMessageEnum.PLAYERNOTINCLAN.chat(hp, muted);
            return false;
        }
        else if(hp.getClan().getRank(hp)==Rank.OWNER)
        {
            CommandMessageEnum.QUITOWNER.chat(hp, muted);
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(HPlayer hp, String[] val) 
    {
        CommandMessageEnum.QUITCLAN.chat(hp);
        hp.getClan().removeMember(hp);
        return true;
    }
    
}
