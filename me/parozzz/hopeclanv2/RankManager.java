/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Paros
 */
public class RankManager 
{
    public static enum Rank
    {
        OWNER(4, false), MODERATOR(3, true), TRUSTED(2, true), MEMBER(1, true);
        
        private final boolean isApplicable;
        private final int level;
        private Rank(final int level, final boolean isApplicable)
        {
            this.level=level;
            this.isApplicable=isApplicable;
        }
        
        public int getPermissionLevel()
        {
            return level;
        }
        
        public boolean isApplicable()
        {
            return isApplicable;
        }
        
        public ChatColor getColor()
        {
            return ranks.get(this).getColor();
        }
        
        public String getName()
        {
            return ranks.get(this).getName();
        }
        
        public static Rank getByName(final String name)
        {
            return names.get(name.toLowerCase());
        }
    }
    
    private final static EnumMap<Rank, RankOptions> ranks=new EnumMap(Rank.class);
    private final static Map<String, Rank> names=new HashMap<>();
    protected static void init(final FileConfiguration c)
    {
        ConfigurationSection rPath=c.getConfigurationSection("Ranks");
        rPath.getKeys(false).stream().map(str -> rPath.getConfigurationSection(str)).forEach(path -> 
        {
            Rank rank=Rank.valueOf(path.getName().toUpperCase());
            
            String rankName=path.getString("name");
            RankOptions options=new RankOptions(rankName, ChatColor.valueOf(path.getString("color").toUpperCase()));
            
            ranks.put(rank, options);
            names.put(rankName.toLowerCase(), rank);
        });
    }
    
    public static class RankOptions
    {
        private final String name;
        private final ChatColor color;
        public RankOptions(final String name, final ChatColor color)
        {
            this.name=name;
            this.color=color;
        }
        
        public String getName()
        {
            return name;
        }
        
        public ChatColor getColor()
        {
            return color;
        }
    }
}
