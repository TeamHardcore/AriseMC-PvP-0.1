/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.model.customspawner.EnumSpawnerType;
import de.teamhardcore.pvp.utils.SimpleUID;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SpawnerManager {

    private final Main plugin;

    private final HashMap<Player, Location> playersInSpawnerChoosing = new HashMap<>();
    private final HashMap<Location, CustomSpawner> spawners = new HashMap<>();

    public SpawnerManager(Main plugin) {
        this.plugin = plugin;

        loadCustomSpawners();
    }

    private void loadCustomSpawners() {
        FileConfiguration cfg = this.plugin.getFileManager().getSpawnerFile().getConfig();

        if (cfg.get("Spawners") == null) return;

        for (String id : cfg.getConfigurationSection("Spawners").getKeys(false)) {
            Location location = Util.stringToLocation(cfg.getString("Spawners." + id + ".Location"));

            if (location.getBlock() == null || location.getBlock().getType() != Material.MOB_SPAWNER) continue;

            EnumSpawnerType type = getSpawnerType(EntityType.valueOf(cfg.getString("Spawners." + id + ".Type")));
            UUID owner = UUID.fromString(cfg.getString("Spawners." + id + ".Owner"));

            CreatureSpawner spawner = (CreatureSpawner) location.getBlock().getState();
            spawner.setSpawnedType(type.getType());
            spawner.update();

            this.spawners.put(location, new CustomSpawner(id, location, type, owner));
        }
    }

    public void addSpawner(UUID uuid, EnumSpawnerType type, Location location) {
        if (this.spawners.containsKey(location)) return;

        SimpleUID uid = SimpleUID.generate(8);
        CustomSpawner customSpawner = new CustomSpawner(uid.toString(), location, type, uuid);
        customSpawner.saveData();
        this.spawners.put(location, customSpawner);
    }

    public void removeSpawner(Location location) {
        if (!this.spawners.containsKey(location)) return;

        CustomSpawner customSpawner = getCustomSpawner(location);
        customSpawner.deleteData();
        this.spawners.remove(location);
    }

    public CustomSpawner getCustomSpawner(Location location) {
        for (CustomSpawner spawner : this.spawners.values()) {
            if (spawner.getLocation().equals(location))
                return spawner;
        }
        return null;
    }

    public EnumSpawnerType getSpawnerType(EntityType entityType) {
        for (EnumSpawnerType type : EnumSpawnerType.values()) {
            if (type.getType().equals(entityType))
                return type;
        }
        return null;
    }

    public EnumSpawnerType getSpawnerType(short durability) {
        for (EnumSpawnerType type : EnumSpawnerType.values()) {
            if (type.getDurability() == durability)
                return type;
        }
        return null;
    }

    public HashMap<Player, Location> getPlayersInSpawnerChoosing() {
        return playersInSpawnerChoosing;
    }
}
