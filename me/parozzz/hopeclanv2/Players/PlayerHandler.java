/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Players;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import me.parozzz.hopeclanv2.Clans.Claim.BuildPermission.BuildType;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimManager;
import me.parozzz.hopeclanv2.Events.PlayerHitClanMemberEvent;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import me.parozzz.hopeclanv2.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Openable;

/**
 *
 * @author Paros
 */
public class PlayerHandler implements Listener
{
    @EventHandler(ignoreCancelled=true)
    private void onPlayerEnter(final PlayerLoginEvent e)
    {
        PlayerManager.playerSetOnline(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=true)
    private void onPlayerQuit(final PlayerQuitEvent e)
    {
        PlayerManager.playerSetOffline(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=true)
    private void onPlayerKick(final PlayerKickEvent e)
    {
        PlayerManager.playerSetOffline(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerHit(final EntityDamageByEntityEvent e)
    {
        if(e.getDamager().getType()==EntityType.PLAYER && e.getEntityType()==EntityType.PLAYER)
        {
            e.setCancelled(Optional.of(PlayerManager.playerGet((Player)e.getEntity()))
                    .filter(hp -> hp.clanGet()!=null)
                    .map(hit -> new PlayerHitClanMemberEvent(PlayerManager.playerGet((Player)e.getDamager()), hit))
                    .map(Utils::callEvent)
                    .filter(Cancellable::isCancelled).isPresent());
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerMove(final PlayerMoveEvent e)
    {
        if(Optional.of(e.getFrom().getChunk())
                .filter(c -> !c.equals(e.getTo().getChunk()))
                .flatMap(c -> Optional.ofNullable(ClaimManager.claimGet(c)))
                .map(claim -> new PlayerStepIntoClaimEvent(PlayerManager.playerGet(e.getPlayer()), claim))
                .map(Utils::callEvent)
                .filter(event -> event.isCancelled()).isPresent())
        {
            e.setTo(e.getFrom());
        }
    }
    
    private static final EnumSet<Material> redstone=EnumSet.of(Material.LEVER, Material.WOOD_BUTTON, Material.STONE_BUTTON, Material.WOOD_PLATE, Material.STONE_PLATE, Material.GOLD_PLATE, Material.IRON_PLATE);
    
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerInteract(final PlayerInteractEvent e)
    {
        e.setCancelled(Optional.ofNullable(e.getClickedBlock())
                .map(Block::getChunk)
                .flatMap(claim -> Optional.ofNullable(ClaimManager.claimGet(claim)))
                .flatMap(claim -> 
                {
                    BuildType type=null;
                    switch(e.getAction())
                    {
                        case LEFT_CLICK_BLOCK:
                            type=BuildType.BUILD;
                            break;
                        case RIGHT_CLICK_BLOCK:
                            if(e.getClickedBlock().getState() instanceof InventoryHolder)
                            {
                                type=BuildType.INVENTORY;
                            }
                            else if(e.getClickedBlock().getState().getData() instanceof Openable)
                            {
                                type=BuildType.DOOR;
                            }
                            else if(redstone.contains(e.getClickedBlock().getType()))
                            {
                                type=BuildType.REDSTONE;
                            }
                            else if(e.getItem().getType().isBlock())
                            {
                                type=BuildType.BUILD;
                            }
                            break;
                        case PHYSICAL:
                            if(redstone.contains(e.getClickedBlock().getType()))
                            {
                                type=BuildType.REDSTONE;
                            }
                    }
                    
                    return Optional.ofNullable(type!=null? new PlayerInteractInClaimEvent(PlayerManager.playerGet(e.getPlayer()), claim, e.getClickedBlock(), type) : null);
                })
                .map(Utils::callEvent)
                .filter(Cancellable::isCancelled).isPresent());
    }
}
