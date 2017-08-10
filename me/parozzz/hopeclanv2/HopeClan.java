/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import java.util.stream.Stream;
import me.parozzz.hopeclanv2.Clans.ClanHandler;
import me.parozzz.hopeclanv2.Players.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Paros
 */
public class HopeClan extends JavaPlugin
{
    public static String NAME="HopeClan";
    
    @Override
    public void onEnable()
    {
        
    }
    
    @Override
    public void onDisable()
    {
        unregisterAll();
    }
    
    public void load(final boolean reload)
    {
        if(reload)
        {
            unregisterAll();
        }
        
        PlayerHandler playerHandler=new PlayerHandler();
        ClanHandler clanHandler=new ClanHandler();
        
        registerListeners(playerHandler, clanHandler);
    }
    
    private void registerListeners(final Listener... listeners)
    {
        Stream.of(listeners).forEach(l -> Bukkit.getServer().getPluginManager().registerEvents(l, this));
    }
    
    private void unregisterAll()
    {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }
}
