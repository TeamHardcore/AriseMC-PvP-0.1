/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.arena;

import de.teamhardcore.pvp.model.duel.arena.DuelArena;

import java.util.List;

public class DuelArenaPool {

    private String name;
    private List<DuelArena> arenas;

    public DuelArenaPool(String name, List<DuelArena> arenas) {
        this.name = name;
        this.arenas = arenas;
    }

    public String getName() {
        return name;
    }

    public List<DuelArena> getArenas() {
        return arenas;
    }
}
