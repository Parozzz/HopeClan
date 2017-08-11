/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans.Claim;

import java.util.Optional;
import java.util.function.BiConsumer;
import me.parozzz.hopeclanv2.Events.ClaimChunkEvent;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent.ExpChangeCause;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import me.parozzz.hopeclanv2.Events.UnclaimChunkEvent;
import me.parozzz.hopeclanv2.ExpManager;
import me.parozzz.hopeclanv2.Messages;
import me.parozzz.hopeclanv2.Messages.MessageEnum;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

/**
 *
 * @author Paros
 */
public class ClaimHandler implements Listener
{
    private final BiConsumer<HPlayer, Claim> title;
    public ClaimHandler()
    {
        if(Utils.bukkitVersion("1.8","1.9","1.10"))
        {
            title = (hp, claim) ->
            {
                if(hp.isOnline())
                {
                    hp.getPlayer().sendTitle(Messages.MessageEnum.CLAIMENTERTITLE.get(), Messages.MessageEnum.CLAIMENTERSUBTITLE.get().replace("%clan%", claim.getClan().getName()));
                }
                
            };      
        }
        else
        {
            title = (hp, claim) ->
            {
                if(hp.isOnline())
                {
                    hp.getPlayer().sendTitle(Messages.MessageEnum.CLAIMENTERTITLE.get(), Messages.MessageEnum.CLAIMENTERSUBTITLE.get().replace("%clan%", claim.getClan().getName()), 7, 15, 7);
                }
            };
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onBrew(final BrewEvent e)
    {
        Optional.ofNullable(ClaimManager.get(e.getBlock().getChunk())).ifPresent(claim -> 
        {
            Utils.callEvent(new ClanExpChangeEvent(null, claim.getClan(), ExpManager.getBrewExp(), ExpChangeCause.BREW));
        });        
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onChunkClaim(final ClaimChunkEvent e)
    {
        if(e.getClan().getExp()<e.getExpCost())
        {
            MessageEnum.EXPTOOLOW.chat(e.getPlayer());
        }
        else
        {
            MessageEnum.CLAIMCHUNK.chat(e.getPlayer());
            e.getClan().removeExp(e.getExpCost());
            ClaimManager.add(e.getClan(), e.getChunk()); 
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onChunksUnclaim(final UnclaimChunkEvent e)
    {
        if(e.getClaims().size()==1)
        {
            MessageEnum.UNCLAIMCHUNK.chat(e.getPlayer());
        }
        else
        {
            MessageEnum.UNCLAIMALL.chat(e.getPlayer());
        }
        e.getClaims().forEach(ClaimManager::remove);
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerStepIntoChunk(final PlayerStepIntoClaimEvent e)
    {
        title.accept(e.getPlayer(), e.getClaim());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerInteractInClaimEvent(final PlayerInteractInClaimEvent e)
    {
        if(!e.canInteract())
        {
            MessageEnum.INTERACTIONNOTALLOWED.actionBar(e.getPlayer());
            e.setCancelled(true);
        }
    }
}
