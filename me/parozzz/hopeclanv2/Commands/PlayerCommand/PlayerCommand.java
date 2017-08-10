/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeclanv2.Commands.PlayerCommand;

import me.parozzz.hopeclanv2.Players.HPlayer;

/**
 *
 * @author Paros
 */
public interface PlayerCommand 
{
    String getCommand();
    void sendHelp(final HPlayer hp);
    boolean canBeUsed(final HPlayer hp, final boolean muted);
    boolean onCommand(final HPlayer hp, final String[] val);
}
