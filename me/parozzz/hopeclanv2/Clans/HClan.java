/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Players.HPlayer;
import me.parozzz.hopeclanv2.ClanEnumManager.Rank;
import me.parozzz.hopeclanv2.ClanEnumManager.Relation;

/**
 *
 * @author Paros
 */
public class HClan 
{        
    private volatile String tag;
    private volatile String name;
    protected HClan(final String name, final String tag, final HPlayer owner)
    {
        this.name=name;
        this.tag=tag;
        this.owner=owner;
        
        members=new HashMap<>();
        relations=new HashMap<>();
        claims=new HashSet<>();
    }
    
    public synchronized String getName()
    {
        return name;
    }
    
    public synchronized String getTag()
    {
        return tag;
    }
    
    private HPlayer owner;
    public void setOwner(final HPlayer hp)
    {
        owner=hp;
    }
    
    public HPlayer getOwner()
    {
        return owner;
    }
    
    private final Map<HPlayer, Rank> members;
    public void addMember(final HPlayer hp)
    {
        members.put(hp, Rank.MEMBER);
    }
    
    public boolean removeMember(final HPlayer hp)
    {
        return hp.equals(owner) ? false : members.remove(hp)!=null;
    }
    
    public boolean setRank(final HPlayer hp, final Rank rank)
    {
        return hp.equals(owner) ? false : members.replace(hp, rank)!=null;
    }
    
    public Rank getRank(final HPlayer hp)
    {
        return owner.equals(hp)? Rank.OWNER : members.get(hp);
    }
    
    private final Map<HClan, Relation> relations;
    public void addRelation(final HClan relative, final Relation rel)
    {
        if(rel==Relation.NEUTRAL)
        {
            relations.remove(relative);
        }
        else
        {
            relations.put(relative, rel);
        }
    }
    
    public Relation getRelation(final HClan relative)
    {
        return relative.equals(this) ? Relation.OWN : Optional.ofNullable(relations.get(relative)).orElseGet(() -> Relation.NEUTRAL);
    }
    
    private final Set<Claim> claims;
    public void addClaim(final Claim claim)
    {
        claims.add(claim);
    }
    
    public boolean removeClaim(final Claim claim)
    {
        return claims.remove(claim);
    }
    
    public Set<Claim> claimList()
    {
        return claims;
    }
    
    private double exp=0D;
    public void addExp(final double add)
    {
        exp+=add;
    }
    
    public void removeExp(final double remove)
    {
        exp-=remove;
    }
    
    public void setExp(final double exp)
    {
        this.exp=exp;
    }
    
    public double getExp()
    {
        return exp;
    }
    
    private int level=0;
    public void levelUp()
    {
        level+=1;
    }
    
    public int levelGet()
    {
        return level;
    }
}
