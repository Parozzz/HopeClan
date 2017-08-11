/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Players;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.reflection.ActionBar;
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
        metadata=new HashMap<>();
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
    
    private volatile String chat;
    public synchronized void updateChat()
    {
        if(clan==null)
        {
            chat=op.getName();
        }
        else
        {
            chat=new StringBuilder().append(clan.getTag()).append(" ").append(clan.getRank(this).getColor()).append(op.getName()).toString();
        }
    }
    
    public String getChat()
    {
        return chat;
    }
    
    private volatile HClan clan;
    public synchronized void setClan(final HClan clan)
    {
        this.clan=clan;
    }
    
    public HClan getClan()
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
    
    
    public void sendMessage(final String... message)
    {
        if(op.isOnline())
        {
            op.getPlayer().sendMessage(message);
        }
    }
    
    public void sendActionBar(final String message)
    {
        if(op.isOnline())
        {
            ActionBar.send(op.getPlayer(), message);
        }
    }
    
    private final Map<String, Object> metadata;
    public void addMetadata(final String key, final Object value)
    {
        metadata.put(key, value);
    }
    
    public boolean hasMetadata(final String key)
    {
        return metadata.containsKey(key);
    }
    
    public Object removeMetadata(final String key)
    {
        return metadata.remove(key);
    }
    
    public Object getMetadata(final String key)
    {
        return metadata.get(key);
    }
    
}
