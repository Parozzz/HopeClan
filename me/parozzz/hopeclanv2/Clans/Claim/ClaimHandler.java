/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans.Claim;

import java.util.Optional;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent;
import me.parozzz.hopeclanv2.Events.ClanExpChangeEvent.ExpChangeCause;
import me.parozzz.hopeclanv2.ExpManager;
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
    @EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
    private void onBrew(final BrewEvent e)
    {
        Optional.ofNullable(ClaimManager.claimGet(e.getBlock().getChunk())).ifPresent(claim -> 
        {
            Utils.callEvent(new ClanExpChangeEvent(null, claim.getClan(), ExpManager.getBrewExp(), ExpChangeCause.BREW));
        });        
    }
}
