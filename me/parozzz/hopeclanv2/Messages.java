/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Paros
 */
public class Messages
{
    public static enum MessageEnum
    {
        HITALLY, 
        INTERACTIONNOTALLOWED,
        RELATIONCHANGED,
        CLANDISBAND,CLANCREATE,
        CLAIMENTERTITLE, CLAIMENTERSUBTITLE, CLAIMCHUNK, 
        UNCLAIMCHUNK, UNCLAIMALL,
        EXPGAIN, EXPTOOLOW,
        RANKCHANGE;
        
        public void chat(final HPlayer hp)
        {
            hp.sendMessage(messages.get(this));
        }
        
        public void actionBar(final HPlayer hp)
        {
            hp.sendActionBar(messages.get(this));
        }
        
        public String get()
        {
            return messages.get(this);
        }
    }
    
    public static String PREFIX;
    private final static EnumMap<MessageEnum, String> messages=new EnumMap(MessageEnum.class);
    private static String clanChat;
    private static BiConsumer<HClan, List<String>> clanInfo;
    protected static void init(final FileConfiguration c)
    {
        PREFIX = Utils.color(c.getString("Prefix"));
        clanChat=Utils.color(c.getString("clanChatFormat"));
        
        clanInfo = (clan, list) -> { };
        c.getStringList("clanInfoMessages").forEach(str -> 
        {
            BiConsumer<HClan, List<String>> function;
            
            if(str.contains("%clan%"))
            {
                function = (clan, list) -> list.add(str.replace("%clan%", clan.getName()));
            }
            else if(str.contains("%tag%"))
            {
                function = (clan, list) -> list.add(str.replace("%tag%", clan.getTag()));
            }
            else if(str.contains("%owner%"))
            {
                function = (clan, list) ->  list.add(str.replace("%owner%", clan.getOwner().getOfflinePlayer().getName())); 
            }
            else if(str.contains("%members%"))
            {
                function = (clan, list) ->  list.add(str.replace("%members%", clan.getMembers().stream().map(hp -> hp.getOfflinePlayer().getName()).collect(Collectors.joining(", ")))); 
            }
            else if(str.contains("%exp%"))
            {
                function = (clan, list) -> list.add(str.replace("%exp%", Objects.toString(clan.getExp())));; 
            }
            else
            {
                function = (clan, list) ->  list.add(str);
            }
            
            clanInfo.andThen(function);
        });
                
        ConfigurationSection mPath=c.getConfigurationSection("Message");
        messages.putAll(mPath.getKeys(false).stream().collect(Collectors.toMap(str -> MessageEnum.valueOf(str.toUpperCase()), str -> PREFIX+Utils.color(mPath.getString(str)))));
    }
    
    public static String[] parseClanInfo(final HClan clan)
    {
        List<String> infos=new ArrayList<>();
        clanInfo.accept(clan, infos);
        return infos.stream().toArray(String[]::new);
    }
    
    public static String getClanChat(final HPlayer hp, final String message)
    {
        return clanChat.replace("%clan%", hp.getClan().getName()).replace("%player%", hp.getClan().getName()).replace("%message%", message);
    }
}
