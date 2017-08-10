/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans.Claim;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.stream.Stream;
import me.parozzz.hopeclanv2.Clans.HClan.Rank;
import me.parozzz.hopeclanv2.Clans.HClan.Relation;

/**
 *
 * @author Paros
 */
public class BuildPermission 
{
    public static enum BuildType
    {
        INVENTORY, BREAK, BUILD, REDSTONE, DOOR;
    }
    
    public BuildPermission()
    {
        permissions=new EnumMap(Relation.class);
        
        Stream.of(Relation.values()).forEach(relation -> permissions.put(relation, EnumSet.noneOf(BuildType.class)));
    }
    
    private Rank rank;
    public void rankSet(final Rank rank)
    {
        this.rank=rank;
    }
    
    public Rank rankGet()
    {
        return rank;
    }
    
    private final EnumMap<Relation,EnumSet<BuildType>> permissions;
    public boolean hasBuildType(final Relation relation, final BuildType type)
    {
        return permissions.get(relation).contains(type);
    }
    
    public boolean addBuildType(final Relation relation, final BuildType type)
    {
        return permissions.get(relation).add(type);
    }
    
    public boolean removeBuildType(final Relation relation, final BuildType type)
    {
        return permissions.get(relation).remove(type);
    }
}
