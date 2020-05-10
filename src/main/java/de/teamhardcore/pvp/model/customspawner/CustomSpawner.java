/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.customspawner;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class CustomSpawner {

    private String randomID;
    private UUID owner;
    private AbstractSpawnerType type;
    private Location location;

    public CustomSpawner(String randomID, Location location, AbstractSpawnerType type, UUID owner) {
        this.randomID = randomID;
        this.location = location;
        this.type = type;
        this.owner = owner;
    }

    public void setType(AbstractSpawnerType type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getOwner() {
        return owner;
    }

    public AbstractSpawnerType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public void saveData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getSpawnerFile().getConfig();
        cfg.set("Spawners." + this.randomID + ".Type", this.type.getType().name());
        cfg.set("Spawners." + this.randomID + ".Owner", this.owner.toString());
        cfg.set("Spawners." + this.randomID + ".Location", Util.locationToString(this.location));
        Main.getInstance().getFileManager().getSpawnerFile().saveConfig();
    }

    public void deleteData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getSpawnerFile().getConfig();
        cfg.set("Spawners." + this.randomID, null);
        Main.getInstance().getFileManager().getSpawnerFile().saveConfig();
    }

}
