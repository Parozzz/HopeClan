/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Clans.Claim;

import me.parozzz.hopeclanv2.Clans.HClan;

/**
 *
 * @author Paros
 */
public class Claim 
{
    public Claim(final HClan clan, final String chunk, final String name)
    {
        this.clan=clan;
        this.chunk=chunk;
        this.name=name;
        
        permissions=new BuildPermission();
    }
    
    private final HClan clan;
    public HClan getClan()
    {
        return clan;
    }
    
    private final String chunk;
    public String getChunk()
    {
        return chunk;
    }
    
    private final String name;
    public String getName()
    {
        return name;
    }
    
    private final BuildPermission permissions;
    public BuildPermission getBuildPermissions()
    {
        return permissions;
    }
}
