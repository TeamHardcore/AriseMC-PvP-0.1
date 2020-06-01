/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelWall {

    private final World world;
    private final Location minPos, maxPos;
    private final List<Location> wallLocs;

    public DuelWall(Location minPos, Location maxPos) {
        this.wallLocs = new ArrayList<>();
        this.world = minPos.getWorld();
        this.minPos = minPos;
        this.maxPos = maxPos;
        calculateRegion();
    }

    public boolean contains(Location loc) {
        if (this.world != loc.getWorld())
            return false;
        return (loc.getY() <= this.maxPos.getY() && loc.getY() >= this.minPos.getY() && ((loc
                .getX() == this.minPos.getX() && loc.getZ() >= this.minPos.getZ() && loc.getZ() <= this.maxPos.getZ()) || (loc
                .getZ() == this.minPos.getZ() && loc.getX() >= this.minPos.getX() && loc.getX() <= this.maxPos.getX()) || (loc
                .getX() == this.maxPos.getX() && loc.getZ() >= this.minPos.getZ() && loc.getZ() <= this.maxPos.getZ()) || (loc
                .getZ() == this.maxPos.getZ() && loc.getX() >= this.minPos.getX() && loc.getX() <= this.maxPos.getX())));
    }

    private void calculateRegion() {
        for (int x = this.minPos.getBlockX(); x <= this.maxPos.getBlockX(); x++) {
            for (int y = this.minPos.getBlockY(); y <= this.maxPos.getBlockY(); y++) {
                for (int z = this.minPos.getBlockZ(); z <= this.maxPos.getBlockZ(); z++) {
                    Location loc = new Location(this.world, x, y, z);
                    if (contains(loc))
                        this.wallLocs.add(loc);
                }
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public Location getMinPos() {
        return minPos;
    }

    public Location getMaxPos() {
        return maxPos;
    }

    public List<Location> getWallLocs() {
        return wallLocs;
    }
}
