/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import me.parozzz.hopeclanv2.Clans.ClanManager;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Messages;
import me.parozzz.hopeclanv2.Players.HPlayer;

/**
 *
 * @author Paros
 */
public class InfoCommand implements PlayerCommand
{

    @Override
    public String getCommand() 
    {
        return CommandType.INFO.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.INFOHELP.chat(hp);
    }

    @Override
    public boolean canBeUsed(HPlayer hp, boolean muted) 
    {
        return true;
    }

    @Override
    public boolean onCommand(HPlayer hp, String[] val) 
    {
        if(val.length!=1)
        {
            return false;
        }
        else
        {
            HClan clan=ClanManager.get(val[0]);
            if(clan==null)
            {
                CommandMessageEnum.CLANWRONGNAME.chat(hp);
            }
            else
            {
                hp.sendMessage(Messages.parseClanInfo(clan));
            }
        }
        return true;
    }
    
}
