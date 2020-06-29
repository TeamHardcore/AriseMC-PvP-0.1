/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.arena.Arena;
import de.teamhardcore.pvp.model.arena.ArenaOptionBase;
import de.teamhardcore.pvp.model.arena.ArenaSelection;
import de.teamhardcore.pvp.model.arena.options.ArenaClanOption;
import de.teamhardcore.pvp.model.arena.options.ArenaPlayerOption;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaManager {

    private final Main plugin;
    private final List<Arena> arenas;
    private final Map<Player, ArenaSelection> selections;
    private final Map<ArenaOptionBase, Class<?>> options;

    public ArenaManager(Main plugin) {
        this.plugin = plugin;
        this.arenas = new ArrayList<>();
        this.selections = new HashMap<>();
        this.options = new HashMap<>();

        initArenaOptions();
    }

    private void initArenaOptions() {
        this.options.put(new ArenaOptionBase("general", "clan"), ArenaClanOption.class);
        this.options.put(new ArenaOptionBase("general", "player"), ArenaPlayerOption.class);
    }

    public void loadArenas() {
        FileConfiguration cfg = this.plugin.getFileManager().getArenaFile().getConfig();

        if (cfg.get("") == null) return;

        for (String name : cfg.getConfigurationSection("").getKeys(false)) {
            Arena arena = new Arena(name);
            this.arenas.add(arena);
        }
    }

    public void createArena(String name, Location hologramLocation, Location maxLocation, Location minLocation) {
        if (getArenaByName(name) != null) return;

        Arena arena = new Arena(name, hologramLocation, minLocation, maxLocation);
        this.arenas.add(arena);
    }

    public void deleteArena(String name) {
        if (getArenaByName(name) == null) return;
        Arena arena = getArenaByName(name);
        arena.despawnHologram();
        arena.deleteData();
        this.arenas.remove(arena);
    }

    public void onDisable() {
        for (Arena arena : this.arenas) {
            arena.despawnHologram();
        }
    }

    public Arena getArenaByName(String name) {
        return this.arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Arena getArenaByLocation(Location location) {
        return this.arenas.stream().filter(arena -> arena.isInside(location)).findFirst().orElse(null);
    }

    public ArenaSelection getSelection(Player player) {
        return this.selections.getOrDefault(player, null);
    }

    public List<ArenaOptionBase> getOptionsByCategory(String category) {
        List<ArenaOptionBase> options = new ArrayList<>();
        for (ArenaOptionBase option : this.options.keySet()) {
            if (option.getCategory().equalsIgnoreCase(category))
                options.add(option);
        }
        return options;
    }

    public ArenaOptionBase getOption(String category, String optionName) {
        for (ArenaOptionBase option : getOptionsByCategory(category)) {
            if (option.getOptionName().equalsIgnoreCase(optionName))
                return option;
        }
        return null;
    }

    public Class<?> getOptionClass(ArenaOptionBase option) {
        if (!this.options.containsKey(option))
            return null;
        return this.options.get(option);
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public Map<Player, ArenaSelection> getSelections() {
        return selections;
    }

    public Main getPlugin() {
        return plugin;
    }
}
