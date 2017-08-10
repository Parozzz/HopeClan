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
public class ClanExpChangeEvent extends Event implements Cancellable
{
    public enum ExpChangeCause
    {
        MOB, BLOCK, BREW, ENEMYKILL;
    }
    
    private final HPlayer hp;
    private final HClan clan;
    private final double modifier;
    private final ExpChangeCause cause;
    public ClanExpChangeEvent(final HPlayer hp, final HClan clan, final double modifier, final ExpChangeCause cause)
    {
        this.hp=hp;
        this.clan=clan;
        this.modifier=modifier;
        this.cause=cause;
    }
    
    public HPlayer getPlayerInvolved()
    {
        return hp;
    }
    
    public HClan getClan()
    {
        return clan;
    }
    
    public double getExpModifier()
    {
        return modifier;
    }
    
    public ExpChangeCause getCause()
    {
        return cause;
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
