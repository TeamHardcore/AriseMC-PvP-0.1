/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.arena.DuelArena;
import de.teamhardcore.pvp.model.duel.arena.DuelArenaType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class DuelManager {

    private final Main plugin;

    private final Map<UUID, DuelConfiguration> configurationCache;
    private final Map<UUID, Duel> duelCache;

    private Map<DuelArenaType, List<DuelArena>> arenas;

    public DuelManager(Main plugin) {
        this.plugin = plugin;
        this.configurationCache = new HashMap<>();
        this.arenas = new HashMap<>();
        this.duelCache = new HashMap<>();

        this.arenas.put(DuelArenaType.PRISON, new ArrayList<DuelArena>() {{
            add(new DuelArena(DuelArenaType.PRISON, new Location(Bukkit.getWorld("world"), -281, 67, 373)));
            add(new DuelArena(DuelArenaType.PRISON, new Location(Bukkit.getWorld("world"), -281, 67, 373)));
            add(new DuelArena(DuelArenaType.PRISON, new Location(Bukkit.getWorld("world"), -281, 67, 373)));
            add(new DuelArena(DuelArenaType.PRISON, new Location(Bukkit.getWorld("world"), -281, 67, 373)));
            add(new DuelArena(DuelArenaType.PRISON, new Location(Bukkit.getWorld("world"), -281, 67, 373)));
            add(new DuelArena(DuelArenaType.PRISON, new Location(Bukkit.getWorld("world"), -281, 67, 373)));
        }});

    }

    public void createDuel(DuelConfiguration configuration) {
        if (configuration.getPlayers().size() <= 1) return;

        DuelArena arena = generateArena(configuration.getArenaType());
        Duel duel = new Duel(configuration, arena);

        this.duelCache.put(duel.getPlayer().getUniqueId(), duel);
        this.duelCache.put(duel.getTarget().getUniqueId(), duel);
    }

    public void stopDuel(Duel duel) {
        for (Player players : duel.getConfiguration().getPlayers()) {
            this.duelCache.remove(players.getUniqueId());
        }
    }

    private DuelArena generateArena(DuelArenaType type) {
        Location parentArena = new Location(Bukkit.getWorld("world"), -281, 67, 373);
        List<DuelArena> used = (!this.arenas.containsKey(type) ? new ArrayList<>() : this.arenas.get(type));

        int xAdd = 0;
        for (int i = 0; i < used.size(); i++) {
            xAdd += 50;
        }

        Location arenaLocation = parentArena.clone().add(xAdd, 0, 0);

        DuelArena arena = new DuelArena(type, arenaLocation);
        used.add(arena);
        this.arenas.put(type, used);
        return arena;
    }

    public Map<UUID, DuelConfiguration> getConfigurationCache() {
        return configurationCache;
    }

    public Map<UUID, Duel> getDuelCache() {
        return duelCache;
    }
}
