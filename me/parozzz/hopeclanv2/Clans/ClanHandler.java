/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Clans.HClan.Relation;
import me.parozzz.hopeclanv2.Events.ClanCreateEvent;
import me.parozzz.hopeclanv2.Events.ClanDisbandEvent;
import me.parozzz.hopeclanv2.Events.ClanRelationChangeEvent;
import me.parozzz.hopeclanv2.Events.PlayerHitClanMemberEvent;
import me.parozzz.hopeclanv2.Events.PlayerInteractInClaimEvent;
import me.parozzz.hopeclanv2.Events.PlayerStepIntoClaimEvent;
import me.parozzz.hopeclanv2.Message;
import me.parozzz.hopeclanv2.Message.MessageEnum;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Paros
 */
public class ClanHandler implements Listener
{
    private final BiConsumer<HPlayer, Claim> title;
    public ClanHandler()
    {
        if(Utils.bukkitVersion("1.8","1.9","1.10"))
        {
            title = (hp, claim) ->
            {
                if(hp.isOnline())
                {
                    hp.getPlayer().sendTitle(MessageEnum.CLAIMENTERTITLE.get(), MessageEnum.CLAIMENTERSUBTITLE.get().replace("%clan%", claim.getClan().getName()));
                }
                
            };      
        }
        else
        {
            title = (hp, claim) ->
            {
                if(hp.isOnline())
                {
                    hp.getPlayer().sendTitle(MessageEnum.CLAIMENTERTITLE.get(), MessageEnum.CLAIMENTERSUBTITLE.get().replace("%clan%", claim.getClan().getName()), 7, 15, 7);
                }
            };
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanCreation(final ClanCreateEvent e)
    {
        MessageEnum.CLANCREATE.chat(e.getOwner());
        ClanManager.clanAdd(e.getName(), e.getTag(), e.getOwner());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanDisband(final ClanDisbandEvent e)
    {
        MessageEnum.CLANDISBAND.chat(e.getClanOwner());
        ClanManager.clanRemove(e.getClan());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onClanRelationChange(final ClanRelationChangeEvent e)
    {
        if(e.getRelation()!=Relation.OWN)
        {
            e.getClanOwner().sendMessage(MessageEnum.RELATIONCHANGED.get().replace("%relative%", e.getRelative().getName()).replace("%relation%", e.getRelation().name()));
            e.getClan().relationAdd(e.getRelative(), e.getRelation());
        }
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerStepIntoChunk(final PlayerStepIntoClaimEvent e)
    {
        title.accept(e.getPlayer(), e.getClaim());
    }
    
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    private void onPlayerHitClanMember(final PlayerHitClanMemberEvent e)
    {
        switch(e.getHitClan().relationGet(e.getDamager().clanGet()))
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
    private void onPlayerInteractInClaimEvent(final PlayerInteractInClaimEvent e)
    {
        if(!e.canInteract())
        {
            MessageEnum.INTERACTIONNOTALLOWED.actionBar(e.getPlayer());
            e.setCancelled(true);
        }
    }
}
