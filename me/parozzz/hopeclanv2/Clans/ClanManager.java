/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans;

import java.util.HashMap;
import java.util.Map;
import me.parozzz.hopeclanv2.Players.HPlayer;

/**
 *
 * @author Paros
 */
public class ClanManager 
{
    private final static Map<String, HClan> clans=new HashMap<>();
    
    protected static void add(final String name, final String tag, final HPlayer owner)
    {
        clans.put(name, new HClan(name, tag, owner));
    }
    
    protected static boolean remove(final HClan clan)
    {
        return remove(clan.getName());
    }
    
    protected static boolean remove(final String clanName)
    {
        return clans.remove(clanName)!=null;
    }
    
    public static HClan get(final String name)
    {
        return clans.get(name);
    }
}
