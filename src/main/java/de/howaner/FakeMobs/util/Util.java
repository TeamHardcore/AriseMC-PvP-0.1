package de.howaner.FakeMobs.util;

import org.bukkit.entity.EntityType;

public class Util {

    public static int getIdForEntity(EntityType type) {
        switch (type) {
            case BOAT:
                return 1;
            case MINECART:
                return 10;
            case ENDER_CRYSTAL:
                return 51;
            case FIREBALL:
                return 63;
            case SMALL_FIREBALL:
                return 64;
            case WITHER_SKULL:
                return 66;
            case ARMOR_STAND:
                return 78;
        }
        return -1;
    }

}
