/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import me.parozzz.hopeclanv2.CommandManager;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.Commands.PlayerCommand.PlayerCommand;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Players.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Paros
 */
public class MainCommand implements CommandExecutor
{

    private final Map<String, PlayerCommand> playerCommands;
    public MainCommand()
    {
        playerCommands=new HashMap<>();
    }
    
    public void addPlayerCommand(final PlayerCommand cmd)
    {
        playerCommands.put(cmd.getCommand(), cmd);
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] val) 
    {
        if(val.length==0)
        {
            if(cs instanceof Player)
            {
                HPlayer hp=PlayerManager.playerGet((Player)cs);
                playerCommands.values().stream().filter(pCmd -> pCmd.canBeUsed(hp, true)).forEach(pCmd -> pCmd.sendHelp(hp));
            }
        }
        else
        {
            if(cs instanceof Player)
            {
                HPlayer hp=PlayerManager.playerGet((Player)cs);
                Optional.ofNullable(playerCommands.get(val[0]))
                        .filter(pCmd -> pCmd.canBeUsed(hp, false))
                        .map(pCmd -> 
                        { 
                            if(!pCmd.onCommand(hp, Stream.of(val).skip(1).toArray(String[]::new)))
                            {
                                pCmd.sendHelp(hp);
                            }
                            return pCmd; 
                        })
                        .orElseGet(() -> 
                        {   
                            CommandMessageEnum.COMMANDWRONG.chat(hp);
                            return null;
                        });
            }
        }
        return true;
    }
}
