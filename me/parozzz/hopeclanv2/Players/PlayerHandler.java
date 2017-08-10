/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Players;

import java.util.EnumSet;
import java.util.Optional;
import me.parozzz.hopeclanv2.Clans.Claim.BuildPermission.BuildType;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimManager;
import me.parozzz.hopeclanv2.Clans.HClan.Relation;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent.ExpChangeCause;
import me.parozzz.hopeclanv2.Events.PlayerHitClanMemberEvent;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import me.parozzz.hopeclanv2.ExpManager;
import me.parozzz.hopeclanv2.Utils;
import me.parozzz.hopeclanv2.Utils.CreatureType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    public PlayerHandler()
    {
    }
    
    @EventHandler(ignoreCancelled=true)
    private void onPlayerEnter(final PlayerLoginEvent e)
    {
        PlayerManager.setOnline(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=true)
    private void onPlayerQuit(final PlayerQuitEvent e)
    {
        PlayerManager.setOffline(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=true)
    private void onPlayerKick(final PlayerKickEvent e)
    {
        PlayerManager.setOffline(e.getPlayer());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerHit(final EntityDamageByEntityEvent e)
    {
        if(e.getDamager().getType()==EntityType.PLAYER && e.getEntityType()==EntityType.PLAYER)
        {
            e.setCancelled(Optional.of(PlayerManager.getOnline((Player)e.getEntity()))
                    .filter(hp -> hp.getClan()!=null)
                    .map(hit -> new PlayerHitClanMemberEvent(PlayerManager.getOnline((Player)e.getDamager()), hit))
                    .map(Utils::callEvent)
                    .filter(Cancellable::isCancelled).isPresent());
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerMove(final PlayerMoveEvent e)
    {
        if(Optional.of(e.getFrom().getChunk())
                .filter(c -> !c.equals(e.getTo().getChunk()))
                .flatMap(c -> Optional.ofNullable(ClaimManager.get(c)))
                .map(claim -> new PlayerStepIntoClaimEvent(PlayerManager.getOnline(e.getPlayer()), claim))
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
                .flatMap(claim -> Optional.ofNullable(ClaimManager.get(claim)))
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
                    
                    return Optional.ofNullable(type!=null? new PlayerInteractInClaimEvent(PlayerManager.getOnline(e.getPlayer()), claim, e.getClickedBlock(), type) : null);
                })
                .map(Utils::callEvent)
                .filter(Cancellable::isCancelled).isPresent());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerDeath(final PlayerDeathEvent e)
    {
        Optional.ofNullable(e.getEntity().getKiller()).map(PlayerManager::getOnline).ifPresent(killer -> 
        {
            HPlayer hp=PlayerManager.getOnline(e.getEntity());
            Optional.ofNullable(killer.getClan())
                    .filter(clan -> clan.getRelation(hp.getClan())==Relation.ENEMY)
                    .ifPresent(clan -> Utils.callEvent(new ClanExpChangeEvent(killer, clan, ExpManager.getEnemyKillExp(), ExpChangeCause.ENEMYKILL)));
        });
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerKillMob(final EntityDeathEvent e)
    {
        Optional.ofNullable(ExpManager.getMobExp(CreatureType.getByLivingEntity(e.getEntity()))).ifPresent(exp -> 
        {
            Optional.ofNullable(e.getEntity().getKiller()).map(PlayerManager::getOnline).ifPresent(hp -> 
            {
                Optional.ofNullable(hp.getClan()).ifPresent(clan ->  Utils.callEvent(new ClanExpChangeEvent(hp, clan, exp, ExpChangeCause.MOB)));
            });
        });
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerMine(final BlockBreakEvent e)
    {
        Optional.ofNullable(ExpManager.getBlockExp(e.getBlock().getType()))
                .filter(exp -> ExpManager.checkSilkTouch(Utils.getMainHand(e.getPlayer().getEquipment())))
                .ifPresent(exp -> 
                {
                    HPlayer hp=PlayerManager.getOnline(e.getPlayer());
                    Optional.ofNullable(hp.getClan()).ifPresent(clan -> Utils.callEvent(new ClanExpChangeEvent(hp, clan, exp, ExpChangeCause.BLOCK)));
                });
    }
}
