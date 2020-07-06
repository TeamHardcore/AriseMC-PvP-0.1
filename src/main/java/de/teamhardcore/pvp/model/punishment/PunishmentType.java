/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.punishment;

public enum PunishmentType {

    BAN,
    UNBAN,
    MUTE,
    WARN;

    public static PunishmentType getTypeByName(String name) {
        for (PunishmentType type : values())
            if (type.name().equalsIgnoreCase(name))
                return type;
        return null;
    }

}
