/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import com.google.common.collect.Sets;
import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Clans.Claim.ClaimManager;
import me.parozzz.hopeclanv2.CommandManager;
import me.parozzz.hopeclanv2.CommandManager.CommandMessageEnum;
import me.parozzz.hopeclanv2.CommandManager.CommandType;
import me.parozzz.hopeclanv2.CommandManager.UnclaimSubCommandEnum;
import me.parozzz.hopeclanv2.Events.UnclaimChunkEvent;
import me.parozzz.hopeclanv2.HopeClan;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Paros
 */
public final class UnclaimCommand implements PlayerCommand
{
    
    private final TextComponent confirm;
    public UnclaimCommand(final FileConfiguration c)
    {
        confirm=new TextComponent(CommandMessageEnum.UNCLAIMALLCONFIRM.get());
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, new StringBuilder("/clan ").append(getCommand()).append(" ").append(UnclaimSubCommandEnum.ALL.getName()).toString()));
    }
    
    @Override
    public String getCommand() 
    {
        return CommandType.UNCLAIM.getName();
    }

    @Override
    public void sendHelp(HPlayer hp) 
    {
        CommandMessageEnum.UNCLAIMHELP.chat(hp);
    }

    @Override
    public boolean canBeUsed(HPlayer hp, boolean muted) 
    {
        if(hp.getClan()==null)
        {
            CommandMessageEnum.PLAYERNOTINCLAN.chat(hp, muted);
            return false;
        }
        else if(hp.getClan().getRank(hp).getPermissionLevel()<Rank.MODERATOR.getPermissionLevel())
        {
            CommandMessageEnum.COMMANDMODERATORONLY.chat(hp, muted);
            return false;
        }
        
        return true;
    }

    @Override
    public boolean onCommand(HPlayer hp, String[] val) 
    {
        if(val.length!=1)
        {
            return false;
        }
        
        switch(UnclaimSubCommandEnum.getByName(val[0]))
        {
            case ONE:
                Claim claim=ClaimManager.get(hp.getPlayer().getLocation().getChunk());
                if(claim==null)
                {
                    CommandMessageEnum.UNCLAIMEMPTY.chat(hp);
                }
                else if(!claim.getClan().equals(hp.getClan()))
                {
                    CommandMessageEnum.UNCLAIMOTHERS.chat(hp);
                }
                else
                {
                    Utils.callEvent(new UnclaimChunkEvent(hp, Sets.newHashSet(claim)));
                }
                break;
            case ALL:
                if(hp.getClan().claimList().isEmpty())
                {
                    CommandMessageEnum.UNCLAIMNOCLAIMS.chat(hp);
                }
                else if(hp.hasMetadata("UNCLAIM_ALL"))
                {
                    Utils.callEvent(new UnclaimChunkEvent(hp, hp.getClan().claimList()));
                    hp.removeMetadata("UNCLAIM_ALL");
                }
                else
                {
                    hp.addMetadata("UNCLAIM_ALL", true);
                    hp.getPlayer().spigot().sendMessage(confirm);
                    new BukkitRunnable()
                    {
                        @Override
                        public void run() 
                        {
                            hp.removeMetadata("UNCLAIM_ALL");
                        }
                    }.runTaskLater(JavaPlugin.getPlugin(HopeClan.class), 200L);
                }
                break;
            default:
                return false;
        }
        return true;
    }
    
}
