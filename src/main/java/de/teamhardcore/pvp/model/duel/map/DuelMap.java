/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.map;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class DuelMap {

    private final String name;
    private final String category;

    private List<Location> locations;

    private boolean isValid;

    public DuelMap(String name, String category) {
        this.name = name;
        this.category = category;
        this.locations = new ArrayList<>();
        this.isValid = true;
    }

    public void addLocation(Location location) {
        this.locations.add(location);
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
