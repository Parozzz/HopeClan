/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimHandler;
import me.parozzz.hopeclanv2.Clans.ClanHandler;
import me.parozzz.hopeclanv2.Commands.MainCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.ChangeRankCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.ChangeRelationCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.ClaimCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.CreateCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.DeleteCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.InfoCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.KickCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.PlayerCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.QuitCommand;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.UnclaimCommand;
import me.parozzz.hopeclanv2.Players.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
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
    
    public void load(final boolean reload) throws UnsupportedEncodingException, IOException, FileNotFoundException, InvalidConfigurationException
    {
        if(reload)
        {
            unregisterAll();
        }
        
        FileConfiguration c=Utils.fileStartup(this, new File(this.getDataFolder(), "config.yml"));
        initializeStatics(c);
        
        PlayerHandler playerHandler=new PlayerHandler();
        ClanHandler clanHandler=new ClanHandler();
        ClaimHandler claimHandler=new ClaimHandler();
        
        registerListeners(playerHandler, clanHandler, claimHandler);
        
        MainCommand command=new MainCommand();
        addPlayerCommands(command);
        this.getCommand("clan").setExecutor(command);
    }
    
    private void registerListeners(final Listener... listeners)
    {
        Stream.of(listeners).forEach(l -> Bukkit.getServer().getPluginManager().registerEvents(l, this));
    }
    
    private void initializeStatics(final FileConfiguration c)
    {
        Messages.init(c);
        ExpManager.init(c);
        ClanEnumManager.init(c);
    }
    
    private void addPlayerCommands(final MainCommand main)
    {
        main.addPlayerCommand(new ChangeRankCommand());
        main.addPlayerCommand(new ChangeRelationCommand());
        main.addPlayerCommand(new ClaimCommand());
        main.addPlayerCommand(new CreateCommand());
        main.addPlayerCommand(new DeleteCommand());
        main.addPlayerCommand(new InfoCommand());
        main.addPlayerCommand(new KickCommand());
        main.addPlayerCommand(new QuitCommand());
        main.addPlayerCommand(new UnclaimCommand());
    }
    
    private void unregisterAll()
    {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }
}
