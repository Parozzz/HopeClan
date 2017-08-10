/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Events;

import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Clans.HClan.Relation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Paros
 */
public class ClanRelationChangeEvent extends Event implements Cancellable
{
    private final Relation relation;
    private final HClan relative;
    private final HClan clan;
    public ClanRelationChangeEvent(final HClan clan, final HClan relative, final Relation relation)
    {
        this.relative=relative;
        this.relation=relation;
        this.clan=clan;
    }
    
    public HClan getClan()
    {
        return clan;
    }
    
    public HClan getRelative()
    {
        return relative;
    }
    
    public Relation getRelation()
    {
        return relation;
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