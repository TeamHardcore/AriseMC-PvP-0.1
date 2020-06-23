/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.duel.request;

import de.teamhardcore.pvp.duel.map.DuelMap;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelRequest {

    private final List<Player> players;

    private final DuelSettings settings;
    private final DuelDeployment deployment;
    private final DuelMap map;

    private final long expire;

    public DuelRequest(DuelMap map, DuelSettings settings, DuelDeployment deployment) {
        this.players = new ArrayList<>();
        this.settings = settings;
        this.deployment = deployment;
        this.map = map;

        this.expire = System.currentTimeMillis() + 120000;
    }

    public DuelMap getMap() {
        return map;
    }

    public long getExpire() {
        return expire;
    }

    public DuelSettings getSettings() {
        return settings;
    }

    public DuelDeployment getDeployment() {
        return deployment;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
