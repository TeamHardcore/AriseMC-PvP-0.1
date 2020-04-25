/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.league.informations;

import de.teamhardcore.pvp.model.league.LeagueInformation;

import java.util.Map;

public class BronzeLeagueInformation extends LeagueInformation {

    public BronzeLeagueInformation(String name, int minTrophies, Map<String, Integer> levels) {
        super(name, minTrophies, levels);
    }
}
