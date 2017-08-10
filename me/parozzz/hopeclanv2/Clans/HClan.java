/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import me.parozzz.hopeclanv2.Clans.Claim.Claim;
import me.parozzz.hopeclanv2.Players.HPlayer;

/**
 *
 * @author Paros
 */
public class HClan 
{
    public static enum Rank
    {
        OWNER(4), MODERATOR(3), TRUSTED(2), MEMBER(1), NOTMEMBER(0);
        
        private final int level;
        private Rank(final int level)
        {
            this.level=level;
        }
        
        public int getPermissionLevel()
        {
            return level;
        }
    }
    
    public static enum Relation
    {
        NEUTRAL, ALLIED, ENEMY, OWN;
    }
    
    private volatile String tag;
    private volatile String name;
    protected HClan(final String name, final String tag, final HPlayer owner)
    {
        this.name=name;
        this.tag=tag;
        this.owner=owner;
        
        members=new HashMap<>();
        relations=new HashMap<>();
        claims=new HashMap<>();
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
    public void ownerSet(final HPlayer hp)
    {
        owner=hp;
    }
    
    public HPlayer ownerGet()
    {
        return owner;
    }
    
    private final Map<HPlayer, Rank> members;
    public void memberAdd(final HPlayer hp)
    {
        members.put(hp, Rank.MEMBER);
    }
    
    public boolean memberRemove(final HPlayer hp)
    {
        return hp.equals(owner) ? false : members.remove(hp)!=null;
    }
    
    public boolean rankSet(final HPlayer hp, final Rank rank)
    {
        return hp.equals(owner) ? false : members.replace(hp, rank)!=null;
    }
    
    public Rank rankGet(final HPlayer hp)
    {
        return owner.equals(hp)? Rank.OWNER : members.getOrDefault(hp, Rank.NOTMEMBER);
    }
    
    private final Map<HClan, Relation> relations;
    public void relationAdd(final HClan relative, final Relation rel)
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
    
    public Relation relationGet(final HClan relative)
    {
        return relative.equals(this) ? Relation.OWN : Optional.ofNullable(relations.get(relative)).orElseGet(() -> Relation.NEUTRAL);
    }
    
    private final Map<String, Claim> claims;
    public void claimAdd(final Claim claim)
    {
        claims.put(claim.getName().toLowerCase(), claim);
    }
    
    public Claim claimGet(final String name)
    {
        return claims.get(name.toLowerCase());
    }
    
    public Collection<Claim> claimList()
    {
        return claims.values();
    }
}
