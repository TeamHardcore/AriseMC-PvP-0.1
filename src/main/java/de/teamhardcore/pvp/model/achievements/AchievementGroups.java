/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements;

import de.teamhardcore.pvp.model.achievements.type.Category;

import java.util.HashMap;

public class AchievementGroups {

    private static final AchievementGroups instance = new AchievementGroups();

    public static AchievementGroups $() {
        return instance;
    }

    private final HashMap<Category, AchievementGroup> groups = new HashMap<>();

    public void addGroup(Category category, AchievementGroup group) {
        if (this.groups.containsKey(category)) {
            return;
        }

        this.groups.put(category, group);
    }

    public void removeGroup(Category category) {
        if (!this.groups.containsKey(category)) return;
        this.groups.remove(category);
    }

    public AchievementGroup getGroup(Category category) {
        return this.groups.values().stream().filter(group -> group.getCategory().equals(category)).findFirst().orElse(null);
    }

    public HashMap<Category, AchievementGroup> getGroups() {
        return groups;
    }
}
