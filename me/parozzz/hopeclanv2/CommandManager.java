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
import me.parozzz.hopeclanv2.Players.HPlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Paros
 */
public class CommandManager
{
    
    public static enum CommandType
    {
        CREATE, DELETE, QUIT, KICK, RANK, RELATION, CLAIM, UNCLAIM, INFO;
        
        public String getName()
        {
            return commandNames.get(this);
        }
    }
    
    public static enum CommandMessageEnum
    {
        COMMANDWRONG, COMMANDOWNERONLY, COMMANDMODERATORONLY,
        CLANWRONGNAME, CLANSAME,
        PLAYERINCLAN, PLAYERNOTINCLAN, OTHERPLAYERNOTINCLAN, PLAYERINEXISTENT, PLAYERWRONGCLAN,
        CREATEHELP, CREATENAMEEXIST, CREATECLAN,
        DELETEHELP, DELETECLAN, DELETECONFIRM,
        QUITHELP, QUITCLAN, QUITOWNER,
        KICKHELP, KICKPLAYER,
        RANKHELP, RANKCHANGE, RANKWRONG, RANKSAME,
        RELATIONHELP, RELATIONALREADY, RELATIONSAME, RELATIONWRONG,
        CLAIMHELP, CLAIMALREADY, CLAIMMAXREACHED,
        UNCLAIMHELP, UNCLAIMEMPTY, UNCLAIMOTHERS, UNCLAIMNOCLAIMS, UNCLAIMALLCONFIRM,
        INFOHELP;
        
        public void chat(final HPlayer hp)
        {
            hp.sendMessage(messages.get(this));
        }
        
        public void chat(final HPlayer hp, final boolean muted)
        {
            if(!muted)
            {
                chat(hp);
            }
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
    
    private final static EnumMap<CommandType, String> commandNames=new EnumMap(CommandType.class);
    private final static EnumMap<CommandMessageEnum, String> messages=new EnumMap(CommandMessageEnum.class);
    
    private final static Map<String, UnclaimSubCommandEnum> unclaimSubCommands=new HashMap<>();
    private static double unclaimExpBackPercentage;
            
    private final static Map<Integer, Double> claimCosts=new HashMap<>();
    protected static void init(final FileConfiguration c)
    {
        ConfigurationSection cPath=c.getConfigurationSection("Commands");
        
        ConfigurationSection mPath=cPath.getConfigurationSection("Message");
        messages.putAll(mPath.getKeys(false).stream().collect(Collectors.toMap(str -> CommandMessageEnum.valueOf(str.toUpperCase()), str -> Messages.PREFIX+Utils.color(mPath.getString(str)))));
    
        ConfigurationSection nPath=cPath.getConfigurationSection("Name");
        commandNames.putAll(nPath.getKeys(false).stream().collect(Collectors.toMap(str -> CommandType.valueOf(str.toUpperCase()), str -> nPath.getString(str))));
    
        ConfigurationSection usPath=c.getConfigurationSection("Commands.UnclaimSubcommand");
        unclaimSubCommands.putAll(usPath.getKeys(false).stream().collect(Collectors.toMap(str -> usPath.getString(str).toLowerCase() , str -> UnclaimSubCommandEnum.valueOf(str.toUpperCase()))));
        unclaimExpBackPercentage=c.getDouble("Commands.UnclaimExpReturn");
        
        ConfigurationSection ccPath=c.getConfigurationSection("Commands.ClaimCosts");
        claimCosts.putAll(ccPath.getKeys(false).stream().collect(Collectors.toMap(str -> Integer.valueOf(str), str -> ccPath.getDouble(str))));
    }
    
    public static Double getCost(final int nextClaim)
    {
        return claimCosts.get(nextClaim);
    }
    
    public static double getUnclaimPercentage()
    {
        return unclaimExpBackPercentage;
    }
    
    public static enum UnclaimSubCommandEnum
    {
        ONE, 
        ALL;
        
        public String getName()
        {
            return unclaimSubCommands.entrySet().stream().filter(entry -> entry.getValue()==this).map(Map.Entry::getKey).findFirst().orElseGet(() -> null);
        }
        
        public static UnclaimSubCommandEnum getByName(final String name)
        {
            return unclaimSubCommands.get(name.toLowerCase());
        }
    }
}
