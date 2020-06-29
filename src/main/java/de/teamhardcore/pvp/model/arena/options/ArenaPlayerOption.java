/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena.options;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.arena.Arena;
import de.teamhardcore.pvp.model.arena.ArenaOptionBase;
import de.teamhardcore.pvp.model.arena.ArenaOptionImpl;
import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public class ArenaPlayerOption extends ArenaOptionImpl {

    public ArenaPlayerOption(ArenaOptionBase optionBase, Arena arena, String... params) {
        super(optionBase, arena, params);
    }

    @Override
    public void executeOnKill(Player player) {
        getArena().getArenaStatistics().addPlayerKill(player);
        getArena().refreshHologram();
    }
}