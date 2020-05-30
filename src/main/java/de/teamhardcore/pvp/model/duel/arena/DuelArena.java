/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.arena;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class DuelArena {

    private DuelArenaType type;
    private Location location;

    private Map<Integer, Location> spawns;

    public DuelArena(DuelArenaType type, Location location) {
        this.type = type;
        this.location = location;

        this.spawns = new HashMap<>();
        this.spawns.put(0, location.clone().add(0, 1, 4));
        this.spawns.put(1, location.clone().add(0, 1, -4));
    }

    public Location getSpawnLocation(int place) {
        return this.spawns.get(place);
    }

    public Map<Integer, Location> getSpawns() {
        return spawns;
    }

    public DuelArenaType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }
}
