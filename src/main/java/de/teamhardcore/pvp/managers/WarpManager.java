/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Warp;
import org.bukkit.Location;

import java.util.Map;

public class WarpManager {

    private final Main plugin;

    public WarpManager(Main plugin) {
        this.plugin = plugin;
    }

    public void addWarp(String name, Location location) {
        this.plugin.getFileManager().getWarpFile().addWarp(name, location);
    }

    public void removeWarp(String name) {
        this.plugin.getFileManager().getWarpFile().removeWarp(name);
    }

    public Warp getWarp(String name) {
        for (Map.Entry<String, Warp> entry : this.plugin.getFileManager().getWarpFile().getWarps().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }


}
