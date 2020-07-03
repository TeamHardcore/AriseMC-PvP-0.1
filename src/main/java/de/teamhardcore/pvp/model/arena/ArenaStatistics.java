/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena;

import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ArenaStatistics {

    private final Arena arena;

    private Map<Clan, Integer> clanStatistics;
    private Map<UUID, Integer> playerStatistics;

    public ArenaStatistics(Arena arena) {
        this.arena = arena;
        this.clanStatistics = new HashMap<>();
        this.playerStatistics = new HashMap<>();
    }

    private void resortStatistics(boolean clan, boolean player) {
        if (clan) {
            this.clanStatistics = this.clanStatistics.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }

        if (player) {
            this.playerStatistics = this.playerStatistics.entrySet().stream()
                    .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }
    }

    public void addClanKill(Clan clan) {
        int currentKills = this.clanStatistics.getOrDefault(clan, 0);
        this.clanStatistics.put(clan, currentKills + 1);
        resortStatistics(true, false);
    }

    public void removeClanKill(Clan clan, int toRemove) {
        int currentKills = this.clanStatistics.getOrDefault(clan, 0);
        this.clanStatistics.put(clan, Math.max(currentKills - toRemove, 0));
        resortStatistics(true, false);
    }

    public void addPlayerKill(Player player) {
        int currentKills = this.playerStatistics.getOrDefault(player.getUniqueId(), 0);
        this.playerStatistics.put(player.getUniqueId(), currentKills + 1);
        resortStatistics(false, true);
    }

    public void removePlayerKill(Player player, int toRemove) {
        int currentKills = this.playerStatistics.getOrDefault(player.getUniqueId(), 0);
        this.playerStatistics.put(player.getUniqueId(), Math.max(currentKills - toRemove, 0));
        resortStatistics(false, true);
    }

    public void resetStatistics(boolean clan, boolean player) {
        if (clan)
            this.clanStatistics.clear();
        if (player)
            this.playerStatistics.clear();

        resortStatistics(clan, player);
    }

    public Object[] getClanStatistic() {
        if (this.clanStatistics.isEmpty()) return null;
        return new Object[]{this.clanStatistics.keySet().toArray()[0], this.clanStatistics.values().toArray()[0]};
    }

    public Object[] getPlayerStatistic() {
        if (this.playerStatistics.isEmpty()) return null;
        return new Object[]{this.playerStatistics.keySet().toArray()[0], this.playerStatistics.values().toArray()[0]};
    }

    public Arena getArena() {
        return arena;
    }

    public Map<Clan, Integer> getClanStatistics() {
        return clanStatistics;
    }

    public Map<UUID, Integer> getPlayerStatistics() {
        return playerStatistics;
    }
}
