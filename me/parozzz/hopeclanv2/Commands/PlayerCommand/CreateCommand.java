/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import java.util.Optional;
import me.parozzz.hopeclanv2.Clans.ClanManager;
import me.parozzz.hopeclanv2.CommandManager;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Events.ClanCreateEvent;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;

/**
 *
 * @author Paros
 */
public class CreateCommand implements PlayerCommand
{
    @Override
    public String getCommand() 
    {
        return CommandType.CREATE.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.CREATEHELP.chat(hp);
    }
    
    @Override
    public boolean canBeUsed(HPlayer hp, final boolean muted) 
    {
        if(hp.getClan()==null)
        {
            CommandMessageEnum.PLAYERINCLAN.chat(hp, muted);
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
        else if(ClanManager.get(val[0])!=null)
        {
            CommandMessageEnum.CREATENAMEEXIST.chat(hp);
        }
        else
        {
            Utils.callEvent(new ClanCreateEvent(val[0], val[1], hp));
        }
        return true;
    }

}
