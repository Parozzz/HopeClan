/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Paros
 */
public class ClanEnumManager 
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
            return rankNames.get(name.toLowerCase());
        }
    }
    
    public static enum Relation
    {
        NEUTRAL, ALLIED, ENEMY, OWN;
        
        public ChatColor getColor()
        {
            return relations.get(this).getColor();
        }
        
        public String getName()
        {
            return relations.get(this).getName();
        }
        
        public static Relation getByName(final String name)
        {
            return relationNames.get(name.toLowerCase());
        }
    }
    
    private final static EnumMap<Rank, Options> ranks=new EnumMap(Rank.class);
    private final static Map<String, Rank> rankNames=new HashMap<>();
    
    private final static EnumMap<Relation, Options> relations=new EnumMap(Relation.class);
    private final static Map<String, Relation> relationNames=new HashMap<>();
    protected static void init(final FileConfiguration c)
    {
        ConfigurationSection rankPath=c.getConfigurationSection("Ranks");
        rankPath.getKeys(false).stream().map(rankPath::getConfigurationSection).forEach(path -> 
        {
            Rank rank=Rank.valueOf(path.getName().toUpperCase());
            
            String rankName=path.getString("name");
            Options options=new Options(rankName, ChatColor.valueOf(path.getString("color").toUpperCase()));
            
            ranks.put(rank, options);
            rankNames.put(rankName.toLowerCase(), rank);
        });
        
        ConfigurationSection relPath=c.getConfigurationSection("Relations");
        rankPath.getKeys(false).stream().map(relPath::getConfigurationSection).forEach(path -> 
        {
            Relation relation = Relation.valueOf(path.getName().toUpperCase());
            
            String relationName=path.getString("name");
            Options options=new Options(relationName, ChatColor.valueOf(path.getString("color").toUpperCase()));
            
            relations.put(relation, options);
            relationNames.put(relationName.toLowerCase(), relation);
        });
    }
    
    private static class Options
    {
        private final String name;
        private final ChatColor color;
        public Options(final String name, final ChatColor color)
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
