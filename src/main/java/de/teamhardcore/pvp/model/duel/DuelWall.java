/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class DuelWall {

    private final World world;
    private final Location minPos, maxPos;
    private final List<Location> wallLocations;

    public DuelWall(Location minPos, Location maxPos) {
        this.wallLocations = new ArrayList<>();
        this.world = minPos.getWorld();
        this.minPos = minPos;
        this.maxPos = maxPos;

        calculateRegion();
    }

    public boolean contains(Location location) {
        return wallLocations.stream().anyMatch(location1 -> location1.getBlockX() == location.getBlockX() && location1.getBlockZ() == location.getBlockZ() && location1.getBlockY() == location.getBlockY());
    }

    private void calculateRegion() {
        int maxX = Math.max(this.minPos.getBlockX(), this.maxPos.getBlockX());
        int minX = Math.min(this.minPos.getBlockX(), this.maxPos.getBlockX());
        int maxZ = Math.max(this.minPos.getBlockZ(), this.maxPos.getBlockZ());
        int minZ = Math.min(this.minPos.getBlockZ(), this.maxPos.getBlockZ());

        for (int x = this.minPos.getBlockX(); x <= this.maxPos.getBlockX(); x++) {
            for (int y = this.minPos.getBlockY(); y <= this.maxPos.getBlockY(); y++) {
                for (int z = this.minPos.getBlockZ(); z <= this.maxPos.getBlockZ(); z++) {
                    if (!(x == minX || x == maxX || z == minZ || z == maxZ)) continue;
                    Location loc = new Location(this.world, x, y, z);
                    this.wallLocations.add(loc);
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

    public List<Location> getWallLocations() {
        return wallLocations;
    }
}
