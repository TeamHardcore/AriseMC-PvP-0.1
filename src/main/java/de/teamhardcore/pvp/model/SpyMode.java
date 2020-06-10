/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import org.bukkit.entity.Player;

import java.util.List;

public class SpyMode {

    private boolean all;
    private List<Player> players;

    public SpyMode(boolean all, List<Player> players) {
        this.all = all;
        this.players = players;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
