/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Events;

import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Paros
 */
public class PlayerRankChangeEvent extends Event implements Cancellable
{
    private final HPlayer hp;
    private final Rank oldRank;
    private final Rank newRank;
    public PlayerRankChangeEvent(final HPlayer hp, final Rank oldRank, final Rank newRank)
    {
        this.hp=hp;
        this.oldRank=oldRank;
        this.newRank=newRank;
    }
    
    public HPlayer getPlayer()
    {
        return hp;
    }
    
    public Rank getOldRank()
    {
        return oldRank;
    }
    
    public Rank getNewRank()
    {
        return newRank;
    }
    
    public HClan getClan()
    {
        return hp.getClan();
    }
    
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() 
    { 
        return handlers; 
    }
    
    public static HandlerList getHandlerList() 
    { 
        return handlers; 
    }

    private boolean cancelled=false;
    @Override
    public boolean isCancelled() 
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean bln) 
    {
        cancelled=bln;
    }
}
