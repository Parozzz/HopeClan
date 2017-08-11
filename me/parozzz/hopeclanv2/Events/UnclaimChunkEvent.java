/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Events;

import java.util.Set;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Paros
 */
public class UnclaimChunkEvent extends Event implements Cancellable
{
    private final HPlayer hp;
    private final Set<Claim> claims;
    public UnclaimChunkEvent(final HPlayer hp, final Set<Claim> claims)
    {
        this.hp=hp;
        this.claims=claims;
    }
    
    public HPlayer getPlayer()
    {
        return hp;
    }
    
    public Set<Claim> getClaims()
    {
        return claims;
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
