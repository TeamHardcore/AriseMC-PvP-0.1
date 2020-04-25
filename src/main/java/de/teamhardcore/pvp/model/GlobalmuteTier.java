/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

public enum GlobalmuteTier {

    NONE(0),
    WITHOUT_VERIFICATION(1),
    ALL_PLAYERS(2),
    COMPLETE(3);

    private int id;

    GlobalmuteTier(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GlobalmuteTier getTierByID(int id) {
        for (GlobalmuteTier tier : values())
            if (tier.getId() == id)
                return tier;
        return null;
    }

}
