/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Players;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.parozzz.hopeclanv2.Clans.Claim.BuildPermission.BuildType;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimManager;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent.ExpChangeCause;
import me.parozzz.hopeclanv2.Events.PlayerHitClanMemberEvent;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import me.parozzz.hopeclanv2.Utils;
import me.parozzz.hopeclanv2.Utils.CreatureType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Openable;

/**
 *
 * @author Paros
 */
public class PlayerHandler implements Listener
{
    private final EnumMap<CreatureType, Double> mobExp;
    private final EnumMap<Material, Double> blockExp;
    private final Predicate<ItemStack> silkTouch;
    public PlayerHandler(final FileConfiguration c)
    {
        mobExp=new EnumMap(CreatureType.class);
        blockExp=new EnumMap(Material.class);
        
        ConfigurationSection expPath=c.getConfigurationSection("Experience");
        silkTouch= expPath.getBoolean("silkTouch") ? tool -> true : tool -> !tool.containsEnchantment(Enchantment.SILK_TOUCH);
        
        ConfigurationSection mPath=expPath.getConfigurationSection("mob");
        mobExp.putAll(mPath.getKeys(false).stream().collect(Collectors.toMap(str -> CreatureType.valueOf(str.toUpperCase()), str -> mPath.getDouble(str))));
        
        ConfigurationSection bPath=expPath.getConfigurationSection("block");
        blockExp.putAll(bPath.getKeys(false).stream().collect(Collectors.toMap(str -> Material.valueOf(str.toUpperCase()), str -> bPath.getDouble(str))));
    }
    
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
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerKillMob(final EntityDeathEvent e)
    {
        Optional.ofNullable(mobExp.get(CreatureType.getByLivingEntity(e.getEntity()))).ifPresent(exp -> 
        {
            Optional.ofNullable(e.getEntity().getKiller()).map(PlayerManager::playerGet).ifPresent(hp -> 
            {
                Optional.ofNullable(hp.clanGet()).ifPresent(clan ->  Utils.callEvent(new ClanExpChangeEvent(hp, clan, exp, ExpChangeCause.MOB)));
            });
        });
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onPlayerMine(final BlockBreakEvent e)
    {
        Optional.ofNullable(blockExp.get(e.getBlock().getType())).ifPresent(exp -> 
        {
            HPlayer hp=PlayerManager.playerGet(e.getPlayer());
            Optional.ofNullable(hp.clanGet()).ifPresent(clan -> Utils.callEvent(new ClanExpChangeEvent(hp, clan, exp, ExpChangeCause.BLOCK)));
        });
    }
}
