/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.league;

import java.util.Map;

public class LeagueInformation {

    private Map<String, Integer> levels;
    private String name;
    private int minTrophies;

    public LeagueInformation(String name, int minTrophies, Map<String, Integer> levels) {
        this.name = name;
        this.minTrophies = minTrophies;
        this.levels = levels;
    }

    public String getDisplayName() {
        return this.name;
    }

    public int getMinTrophies() {
        return this.minTrophies;
    }

    public Map<String, Integer> getLevels() {
        return levels;
    }

}
