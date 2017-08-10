/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Players;

import java.util.HashSet;
import java.util.Set;
import me.parozzz.hopeclanv2.Clans.HClan;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
/**
 *
 * @author Paros
 */
public class HPlayer 
{
    private final OfflinePlayer op;
    public HPlayer(final OfflinePlayer op)
    {
        this.op=op;
        invites=new HashSet<>();
    }
    
    public OfflinePlayer getOfflinePlayer()
    {
        return op;
    }
    
    public boolean isOnline()
    {
        return op.isOnline();
    }
    
    public Player getPlayer()
    {
        return op.getPlayer();
    }
    
    private HClan clan;
    public void clanSet(final HClan clan)
    {
        this.clan=clan;
    }
    
    public HClan clanGet()
    {
        return clan;
    }
    
    private final Set<HClan> invites;
    public void inviteAdd(final HClan clan)
    {
        invites.add(clan);
    }
    
    public boolean inviteRemove(final HClan clan)
    {
        return invites.remove(clan);
    }
}