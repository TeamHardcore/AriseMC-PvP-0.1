/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.league.League;
import de.teamhardcore.pvp.model.league.informations.BronzeLeagueInformation;
import de.teamhardcore.pvp.model.league.informations.UnrankedLeagueInformation;

import java.util.ArrayList;
import java.util.HashMap;

public class LeagueManager {

    private final Main plugin;

    private final ArrayList<League> leagues = new ArrayList<League>() {{
        add(new League(new UnrankedLeagueInformation("Unranked ", 0, new HashMap<>())));
        add(new League(new BronzeLeagueInformation("Bronze ", 500, new HashMap<String, Integer>() {{
            put("1", 550);
            put("2", 600);
            put("3", 650);
        }})));
    }};

    public LeagueManager(Main plugin) {
        this.plugin = plugin;
    }

    public League getLeagueByTrophies(int trophies) {
        League chosen = null;

        for (League league : this.leagues) {
            if (league.getInformation().getMinTrophies() <= trophies)
                if (chosen == null || chosen.getInformation().getMinTrophies() < league.getInformation().getMinTrophies())
                    chosen = league;
        }
        return chosen;
    }

}
