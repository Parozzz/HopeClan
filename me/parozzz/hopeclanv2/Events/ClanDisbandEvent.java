/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Events;

import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Paros
 */
public class ClanDisbandEvent extends Event implements Cancellable
{

    private final HPlayer clanOwner;
    private final HClan clan;
    public ClanDisbandEvent(final HPlayer clanOwner, final HClan clan)
    {
        this.clan=clan;
        this.clanOwner=clanOwner;
    }
    
    public HClan getClan()
    {
        return clan;
    }
    
    public HPlayer getClanOwner()
    {
        return clanOwner;
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
