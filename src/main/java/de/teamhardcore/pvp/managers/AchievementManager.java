/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.AchievementGroup;
import de.teamhardcore.pvp.model.achievements.AchievementGroups;
import de.teamhardcore.pvp.model.achievements.type.Category;

public class AchievementManager {

    private final Main plugin;

    public AchievementManager(Main plugin) {
        this.plugin = plugin;
        registerGroups();
    }

    private void registerGroups() {
        for (Category value : Category.values())
            AchievementGroups.$().addGroup(value, new AchievementGroup(value));
    }

    public Main getPlugin() {
        return plugin;
    }
}
