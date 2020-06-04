/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import org.bukkit.Location;

import java.util.UUID;

public class Home {

    private UUID uuid;
    private String name;
    private Location location;
    private long creationDate, lastTeleportDate;

    public Home(UUID uuid, String name, Location location, long creationDate, long lastTeleportDate) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
        this.creationDate = creationDate;
        this.lastTeleportDate = lastTeleportDate;
    }

    public void setLastTeleportDate(long lastTeleportDate) {
        this.lastTeleportDate = lastTeleportDate;
    }

    public long getLastTeleportDate() {
        return lastTeleportDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public long getCreationDate() {
        return creationDate;
    }
}
