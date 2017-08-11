/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import me.parozzz.hopeclanv2.ClanEnumManager;
import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.ClanEnumManager.Relation;
import me.parozzz.hopeclanv2.Clans.ClanManager;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.CommandManager;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.Events.RelationChangeEvent;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;

/**
 *
 * @author Paros
 */
public class ChangeRelationCommand implements PlayerCommand
{

    @Override
    public String getCommand() 
    {
        return CommandType.RELATION.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.RELATIONHELP.chat(hp);
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
        
        HClan relative=ClanManager.get(val[0]);
        if(relative==null)
        {
            CommandMessageEnum.CLANWRONGNAME.chat(hp);
        }
        else if(hp.getClan().equals(relative))
        {
            CommandMessageEnum.CLANSAME.chat(hp);
        }
        else
        {
            Relation oldRelation=hp.getClan().getRelation(relative);
            Relation newRelation=Relation.getByName(val[1]);
            
            if(oldRelation!=Relation.NEUTRAL)
            {
                CommandMessageEnum.RELATIONALREADY.chat(hp);
            }
            else if(newRelation==null)
            {
                CommandMessageEnum.RELATIONWRONG.chat(hp);
            }  
            else if(oldRelation==newRelation)
            {
                CommandMessageEnum.RELATIONSAME.chat(hp);
            }
            else
            {
                Utils.callEvent(new RelationChangeEvent(hp, hp.getClan(), relative, newRelation));
            }
        }
        
        return true;
    }
    
}
