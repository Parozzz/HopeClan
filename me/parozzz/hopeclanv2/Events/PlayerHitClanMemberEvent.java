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
public class PlayerHitClanMemberEvent extends Event implements Cancellable
{
    private final HPlayer damager;
    private final HPlayer hit;
    public PlayerHitClanMemberEvent(final HPlayer damager, final HPlayer hit)
    {
        this.damager=damager;
        this.hit=hit;
    }
    
    public HPlayer getDamager()
    {
        return damager;
    }
    
    public HPlayer getHit()
    {
        return hit;
    }
    
    public HClan getHitClan()
    {
        return hit.getClan();
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
