/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Players;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.parozzz.hopeclanv2.HopeClan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Paros
 */
public class PlayerManager 
{
    private static final String METADATA="HopeClan.Player";
    
    private static final Map<Player, HPlayer> onlinePlayers=new HashMap<>();
    private static final Map<String, HPlayer> offlinePlayers=new HashMap<>();
    
    protected static void setOnline(final Player p)
    {
        HPlayer hp=Optional.ofNullable(offlinePlayers.remove(p.getName())).orElseGet(() -> new HPlayer(p));
        
        p.setMetadata(METADATA, new FixedMetadataValue(JavaPlugin.getPlugin(HopeClan.class), hp));
        
        onlinePlayers.put(p, hp);
    }
    
    protected static void setOffline(final Player p)
    {
        offlinePlayers.put(p.getName(), onlinePlayers.remove(p));
        
        p.removeMetadata(METADATA, JavaPlugin.getPlugin(HopeClan.class));
    }
    
    public static HPlayer getOnline(final Player p)
    {
        return (HPlayer)p.getMetadata(METADATA).get(0).value();
    }
    
    public static HPlayer getOffline(final String name)
    {
        return offlinePlayers.get(name);
    }
}
