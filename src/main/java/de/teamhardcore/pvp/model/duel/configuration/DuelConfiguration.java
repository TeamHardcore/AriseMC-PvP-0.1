/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.configuration;

import de.teamhardcore.pvp.model.duel.arena.DuelArenaType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DuelConfiguration {

    private List<Player> players;
    private List<Player> invites;

    private long created;

    private DuelArenaType arenaType;
    private DuelSettings settings;
    private DuelDeployment deployment;

    public DuelConfiguration(DuelArenaType arenaType, DuelSettings settings, DuelDeployment deployment) {
        this.players = new ArrayList<>();
        this.invites = new ArrayList<>();
        this.created = System.currentTimeMillis();

        this.arenaType = arenaType;
        this.settings = settings;
        this.deployment = deployment;
    }

    public long getCreated() {
        return created;
    }

    public List<Player> getInvites() {
        return invites;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public DuelArenaType getArenaType() {
        return arenaType;
    }

    public DuelSettings getSettings() {
        return settings;
    }

    public DuelDeployment getDeployment() {
        return deployment;
    }
}
