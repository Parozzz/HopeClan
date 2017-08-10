/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans.Claim;

import java.util.HashMap;
import java.util.Map;
import me.parozzz.hopeclanv2.Clans.HClan;
import me.parozzz.hopeclanv2.Utils;
import org.bukkit.Chunk;

/**
 *
 * @author Paros
 */
public class ClaimManager 
{
    private static final Map<String, Claim> claims=new HashMap<>();
    
    public static void claimGet(final HClan clan, final Chunk c, final String name)
    {
        claimAdd(clan, Utils.chunkToString(c), name);
    }
    
    public static void claimAdd(final HClan clan, final String chunk, final String name)
    {
        Claim claim=new Claim(clan, chunk, name);
        clan.claimAdd(claim);
        claims.put(name, claim);
    }
    
    public static Claim claimGet(final Chunk c)
    {
        return claims.get(Utils.chunkToString(c));
    }
    
}
