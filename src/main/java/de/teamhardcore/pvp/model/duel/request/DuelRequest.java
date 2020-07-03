/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.request;

import org.bukkit.entity.Player;

public class DuelRequest {

    private final Player player, target;
    private final DuelConfiguration configuration;

    public DuelRequest(Player player, Player target, DuelConfiguration configuration) {
        this.player = player;
        this.target = target;
        this.configuration = configuration;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public DuelConfiguration getConfiguration() {
        return configuration;
    }
}
