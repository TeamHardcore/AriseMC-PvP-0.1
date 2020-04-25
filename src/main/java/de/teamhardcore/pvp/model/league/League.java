/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.league;

import java.util.Map;

public class League {

    private final LeagueInformation information;

    public League(LeagueInformation information) {
        this.information = information;
    }

    public LeagueInformation getInformation() {
        return information;
    }

    public String getLeagueName(int trophies) {
        String name = this.information.getDisplayName();
        String level = "default";

        if (this.information.getLevels().isEmpty()) {
            return name;
        }

        for (Map.Entry<String, Integer> entry : this.getInformation().getLevels().entrySet()) {
            if (trophies < entry.getValue())
                level = entry.getKey();
        }

        return name + level;
    }

}
