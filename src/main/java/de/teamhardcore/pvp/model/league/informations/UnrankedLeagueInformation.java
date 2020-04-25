/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.league.informations;

import de.teamhardcore.pvp.model.league.LeagueInformation;

import java.util.Map;

public class UnrankedLeagueInformation extends LeagueInformation {

    public UnrankedLeagueInformation(String name, int minTrophies, Map<String, Integer> levels) {
        super(name, minTrophies, levels);
    }
}
