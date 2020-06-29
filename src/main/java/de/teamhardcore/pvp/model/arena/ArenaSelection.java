/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaSelection {

    private List<Location> selections;
    private Location maxLocation, minLocation;
    private List<ArmorStand> armorStands;

    public ArenaSelection() {
        this.selections = new ArrayList<>();
        this.armorStands = new ArrayList<>();

        this.maxLocation = null;
        this.minLocation = null;
    }

    private void calculateLocations() {
        Location maxLocation = null;
        Location minLocation = null;

        Location temp;

        for (Location location : this.selections) {
            temp = location.clone();

            if (minLocation == null || temp.clone().getY() < maxLocation.clone().getY())
                minLocation = temp.clone();

            if (maxLocation == null || temp.clone().getY() > minLocation.clone().getY())
                maxLocation = temp.clone();
        }


        this.maxLocation = maxLocation != null ? maxLocation.clone() : null;
        this.minLocation = minLocation != null ? minLocation.clone() : null;
    }

    public Location getMaxLocation() {
        return maxLocation;
    }

    public Location getMinLocation() {
        return minLocation;
    }

    public void addSelection(Location location) {
        this.selections.add(location);
        calculateLocations();

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCustomName(String.valueOf(this.selections.size()));
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setCanPickupItems(false);
        this.armorStands.add(armorStand);
    }

    public Location getSelection(int index) {
        if (this.selections.size() < index) return null;

        return this.selections.get(index - 1);
    }

    public void removeArmorStands() {
        for (ArmorStand armorStand : this.armorStands) {
            armorStand.remove();
        }
    }

    public List<Location> getSelections() {
        return selections;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }
}
