/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.fakeentity;

import org.bukkit.entity.EntityType;

public enum FakeEntityType {

    BLAZE("Blaze", EntityType.BLAZE, 2.2D),
    WITCH("Witch", EntityType.WITCH, 2.7D),
    SPIDER("Spider", EntityType.SPIDER, 2.0D),
    CREEPER("Creeper", EntityType.CREEPER, 2.0D),
    SKELETON("Skeleton", EntityType.SKELETON, 2.2D),
    ZOMBIE("Zombie", EntityType.ZOMBIE, 2.2D),
    SLIME("Slime", EntityType.SLIME, 2.0D),
    GHAST("Ghast", EntityType.GHAST, 2.0D),
    ZOMBIE_PIGMAN("ZombiePigman", EntityType.PIG_ZOMBIE, 2.2D),
    ENDERMAN("Enderman", EntityType.ENDERMAN, 3.0D),
    CAVE_SPIDER("CaveSpider", EntityType.CAVE_SPIDER, 2.0D),
    SILVERFISH("Silverfish", EntityType.SILVERFISH, 2.0D),
    MAGMA_CUBE("MagmaCube", EntityType.MAGMA_CUBE, 2.0D),
    BAT("Bat", EntityType.BAT, 2.0D),
    ENDERMITE("Endermite", EntityType.ENDERMITE, 2.0D),
    GUARDIAN("Guardian", EntityType.GUARDIAN, 2.0D),
    PIG("Pig", EntityType.PIG, 2.0D),
    SHEEP("Sheep", EntityType.SHEEP, 2.0D),
    COW("Cow", EntityType.COW, 2.0D),
    CHICKEN("Chicken", EntityType.CHICKEN, 2.0D),
    SQUID("Squid", EntityType.SQUID, 2.0D),
    WOLF("Wolf", EntityType.WOLF, 2.0D),
    MUSHROOM_COW("MushroomCow", EntityType.MUSHROOM_COW, 2.0D),
    OCELOT("Ocelot", EntityType.OCELOT, 2.0D),
    HORSE("Horse", EntityType.HORSE, 2.0D),
    VILLAGER("Villager", EntityType.VILLAGER, 2.15D),
    ENDERCRYSTAL("EnderCrystal", EntityType.ENDER_CRYSTAL, 2.2D);

    private final String name;
    private final EntityType type;
    private final double nameTagHeight;

    FakeEntityType(String name, EntityType type, double nameTagHeight) {
        this.name = name;
        this.type = type;
        this.nameTagHeight = nameTagHeight;
    }


    public String getName() {
        return this.name;
    }


    public EntityType getType() {
        return this.type;
    }


    public double getNameTagHeight() {
        return this.nameTagHeight;
    }


    public static FakeEntityType getTypeByName(String name) {
        for (FakeEntityType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
