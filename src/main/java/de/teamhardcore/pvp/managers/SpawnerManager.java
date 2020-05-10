/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.AbstractSpawnerType;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.model.customspawner.premium.CreeperSpawnerType;
import de.teamhardcore.pvp.model.customspawner.standart.PigSpawnerType;
import de.teamhardcore.pvp.model.customspawner.standart.SkeletonSpawnerType;
import de.teamhardcore.pvp.utils.SimpleUID;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SpawnerManager {

    private final Main plugin;

    private final HashMap<Player, Location> playersInSpawnerChoosing = new HashMap<>();

    private List<AbstractSpawnerType> spawnerTypes = new ArrayList<AbstractSpawnerType>() {{
        add(new PigSpawnerType());
        add(new SkeletonSpawnerType());
        add(new CreeperSpawnerType());
    }};
    private HashMap<Location, CustomSpawner> spawners = new HashMap<>();

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

            AbstractSpawnerType type = getSpawnerType(EntityType.valueOf(cfg.getString("Spawners." + id + ".Type")));
            UUID owner = UUID.fromString(cfg.getString("Spawners." + id + ".Owner"));

            CreatureSpawner spawner = (CreatureSpawner) location.getBlock().getState();
            spawner.setSpawnedType(type.getType());
            spawner.update();

            this.spawners.put(location, new CustomSpawner(id, location, type, owner));
        }
    }

    public void addSpawner(UUID uuid, AbstractSpawnerType type, Location location) {
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

    public AbstractSpawnerType getSpawnerType(EntityType entityType) {
        for (AbstractSpawnerType type : this.spawnerTypes) {
            if (type.getType().equals(entityType))
                return type;
        }
        return null;
    }

    public AbstractSpawnerType getSpawnerType(short durability) {
        for (AbstractSpawnerType type : this.spawnerTypes) {
            if (type.getDurability() == durability)
                return type;
        }
        return null;
    }

    public List<AbstractSpawnerType> getSpawnerTypes() {
        return spawnerTypes;
    }

    public HashMap<Player, Location> getPlayersInSpawnerChoosing() {
        return playersInSpawnerChoosing;
    }
}
