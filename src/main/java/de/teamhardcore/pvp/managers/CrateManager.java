/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.crates.AbstractCrate;
import de.teamhardcore.pvp.model.crates.CrateOpening;
import de.teamhardcore.pvp.model.crates.crates.TestCrate;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CrateManager {

    private final Main plugin;

    private final HashMap<String, AbstractCrate> availableCrates = new HashMap<String, AbstractCrate>() {{
        put("Test", new TestCrate());
    }};
    private final HashMap<Player, CrateOpening> activeOpenings = new HashMap<>();

    public CrateManager(Main plugin) {
        this.plugin = plugin;
    }

    public AbstractCrate getCrate(String name) {
        if (!this.availableCrates.containsKey(name)) return null;
        return this.availableCrates.get(name);
    }

    public HashMap<Player, CrateOpening> getActiveOpenings() {
        return activeOpenings;
    }

    public HashMap<String, AbstractCrate> getAvailableCrates() {
        return availableCrates;
    }
}
