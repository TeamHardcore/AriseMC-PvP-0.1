/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FightStatistics {

    /*
    TODO: Fight Statistiken
     */

    private final Player player;
    private final long startTime;
    private final long endTime;

    private final Map<Player, Double> damageTaken;
    private final Map<Player, Integer> hitsTaken;

    public FightStatistics(Player player) {
        this.player = player;
        this.damageTaken = new HashMap<>();
        this.hitsTaken = new HashMap<>();
        this.endTime = -1L;
        this.startTime = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Map<Player, Double> getDamageTaken() {
        return damageTaken;
    }

    public Map<Player, Integer> getHitsTaken() {
        return hitsTaken;
    }
}
