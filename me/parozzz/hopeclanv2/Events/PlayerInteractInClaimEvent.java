/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Events;

import me.parozzz.hopeclanv2.Clans.Claim.BuildPermission.BuildType;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Paros
 */
public class PlayerInteractInClaimEvent extends Event implements Cancellable
{
    private final HPlayer hp;
    private final Claim claim;
    private final Block b;
    private final BuildType type;
    public PlayerInteractInClaimEvent(final HPlayer hp, final Claim claim, final Block b, final BuildType type)
    {
        this.hp=hp;
        this.claim=claim;
        this.b=b;
        this.type=type;
    }
    
    public HPlayer getPlayer()
    {
        return hp;
    }
    
    public Claim getClaim()
    {
        return claim;
    }
    
    public HClan getClaimClan()
    {
        return claim.getClan();
    }
    
    public BuildType getBuildType()
    {
        return type;
    }
    
    public boolean canInteract()
    {
        return claim.getBuildPermissions().hasBuildType(claim.getClan().getRelation(hp.getClan()), type);
    }
    
    public Block getBlock()
    {
        return b;
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
