/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans;

import java.util.Optional;
import me.parozzz.hopeclanv2.Clans.HClan.Relation;
import me.parozzz.hopeclanv2.Events.ClanCreateEvent;
import me.parozzz.hopeclanv2.Events.ClanDisbandEvent;
import me.parozzz.hopeclanv2.Events.ClanRelationChangeEvent;
import me.parozzz.hopeclanv2.Events.PlayerHitClanMemberEvent;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Paros
 */
public class ClanHandler implements Listener
{
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanCreation(final ClanCreateEvent e)
    {
        ClanManager.clanAdd(e.getName(), e.getTag(), e.getOwner());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanDisband(final ClanDisbandEvent e)
    {
        ClanManager.clanRemove(e.getClan());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanRelationChange(final ClanRelationChangeEvent e)
    {
        if(e.getRelation()==Relation.NEUTRAL)
        {
            e.getClan().relationRemove(e.getRelative());
        }
        else
        {
            e.getClan().relationAdd(e.getRelative(), e.getRelation());
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerStepIntoChunk(final PlayerStepIntoClaimEvent e)
    {
        
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerHitClanMember(final PlayerHitClanMemberEvent e)
    {
        switch(e.getHitClan().relationGet(e.getDamager().clanGet()))
        {
            case ALLIED:
                e.setCancelled(true);
                break;
            case ENEMY:
            case NEUTRAL:
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerInteractInClaimEvent(final PlayerInteractInClaimEvent e)
    {
        e.setCancelled(!e.canInteract());
    }
}
