/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimManager;
import me.parozzz.hopeclanv2.ClanEnumManager.Relation;
import me.parozzz.hopeclanv2.Events.ClaimChunkEvent;
import me.parozzz.hopeclanv2.Events.ClanCreateEvent;
import me.parozzz.hopeclanv2.Events.ClanDisbandEvent;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent;
import me.parozzz.hopeclanv2.Events.PlayerHitClanMemberEvent;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerRankChangeEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import me.parozzz.hopeclanv2.Events.RelationChangeEvent;
import me.parozzz.hopeclanv2.Messages.MessageEnum;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Paros
 */
public class ClanHandler implements Listener
{
    public ClanHandler()
    {

    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanCreation(final ClanCreateEvent e)
    {
        MessageEnum.CLANCREATE.chat(e.getOwner());
        ClanManager.add(e.getName(), e.getTag(), e.getOwner());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanDisband(final ClanDisbandEvent e)
    {
        MessageEnum.CLANDISBAND.chat(e.getClanOwner());
        ClanManager.remove(e.getClan());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanRelationChange(final RelationChangeEvent e)
    {
        if(e.getRelation()!=Relation.OWN)
        {
            e.getClanOwner().sendMessage(MessageEnum.RELATIONCHANGED.get().replace("%relative%", e.getRelative().getName()).replace("%relation%", e.getRelation().getColor() + e.getRelation().getName()));
            e.getClan().addRelation(e.getRelative(), e.getRelation());
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerHitClanMember(final PlayerHitClanMemberEvent e)
    {
        switch(e.getHitClan().getRelation(e.getDamager().getClan()))
        {
            case ALLIED:
            case OWN:
                MessageEnum.HITALLY.actionBar(e.getDamager());
                e.setCancelled(true);
                break;
            case ENEMY:
            case NEUTRAL:
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerRankChange(final PlayerRankChangeEvent e)
    {
        e.getClan().setRank(e.getPlayer(), e.getNewRank());
        e.getPlayer().sendMessage(MessageEnum.RANKCHANGE.get().replace("%player%", e.getPlayer().getOfflinePlayer().getName()).replace("%rank%", e.getNewRank().getColor()+e.getNewRank().getName()));
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onExpChange(final ClanExpChangeEvent e)
    {
        Optional.ofNullable(e.getPlayerInvolved())
                .ifPresent(hp -> hp.sendActionBar(MessageEnum.EXPGAIN.get().replace("%exp%", Objects.toString(e.getExpModifier()))));
        e.getClan().addExp(e.getExpModifier());
    }
}
