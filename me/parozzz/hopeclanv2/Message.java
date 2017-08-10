/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import java.util.EnumMap;
import java.util.stream.Collectors;
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Paros
 */
public class Message
{
    public static enum MessageEnum
    {
        HITALLY, 
        INTERACTIONNOTALLOWED,
        RELATIONCHANGED,
        CLANDISBAND,CLANCREATE,
        CLAIMENTERTITLE, CLAIMENTERSUBTITLE;
        
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
    
    protected static void init(final FileConfiguration c)
    {
        PREFIX = Utils.color(c.getString("Prefix"));
        
        ConfigurationSection mPath=c.getConfigurationSection("Message");
        messages.putAll(mPath.getKeys(false).stream().collect(Collectors.toMap(str -> MessageEnum.valueOf(str.toUpperCase()), str -> Utils.color(mPath.getString(str)))));
    }
}
