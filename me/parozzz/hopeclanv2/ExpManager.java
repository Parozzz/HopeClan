/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2;

import java.util.EnumMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import me.parozzz.hopeclanv2.Utils.CreatureType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Paros
 */
public class ExpManager 
{
    private final static EnumMap<CreatureType, Double> mobExp=new EnumMap(CreatureType.class);
    private final static EnumMap<Material, Double> blockExp=new EnumMap(Material.class);
    private static double brewExp;
    private static double enemyKillExp;
    private static Predicate<ItemStack> silkTouch;
    protected static void init(final FileConfiguration c)
    {
        ConfigurationSection expPath=c.getConfigurationSection("Experience");
        silkTouch= expPath.getBoolean("silkTouch") ? tool -> true : tool -> !tool.containsEnchantment(Enchantment.SILK_TOUCH);
        
        ConfigurationSection mPath=expPath.getConfigurationSection("mob");
        mobExp.putAll(mPath.getKeys(false).stream().collect(Collectors.toMap(str -> CreatureType.valueOf(str.toUpperCase()), str -> mPath.getDouble(str))));
        
        ConfigurationSection bPath=expPath.getConfigurationSection("block");
        blockExp.putAll(bPath.getKeys(false).stream().collect(Collectors.toMap(str -> Material.valueOf(str.toUpperCase()), str -> bPath.getDouble(str))));
        
        brewExp=expPath.getDouble("potionBrew");
        enemyKillExp=expPath.getDouble("enemyKill");
    }
    
    public static Double getMobExp(final CreatureType ct)
    {
        return mobExp.get(ct);
    }
    
    public static Double getBlockExp(final Material type)
    {
        return blockExp.get(type);
    }
    
    public static double getBrewExp()
    {
        return brewExp;
    }
    
    public static double getEnemyKillExp()
    {
        return enemyKillExp;
    }
    
    public static boolean checkSilkTouch(final ItemStack tool)
    {
        return silkTouch.test(tool);
    }
}
