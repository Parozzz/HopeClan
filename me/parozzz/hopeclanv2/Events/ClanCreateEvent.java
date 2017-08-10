/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Events;

import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Paros
 */
public class ClanCreateEvent extends Event implements Cancellable
{
    private final String name;
    private final String tag;
    private final HPlayer owner;
    public ClanCreateEvent(final String name, final String tag, final HPlayer hp)
    {
        this.name=name;
        this.tag=tag;
        owner=hp;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getTag()
    {
        return tag;
    }
    
    public HPlayer getOwner()
    {
        return owner;
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
