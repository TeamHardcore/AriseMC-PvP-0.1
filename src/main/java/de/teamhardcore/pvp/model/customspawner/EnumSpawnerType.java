/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.customspawner;

import de.teamhardcore.pvp.user.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public enum EnumSpawnerType {

    SPIDER("Spinne", EntityType.SPIDER, Material.MONSTER_EGG, 52, 0, false),
    SKELETON("Skelett", EntityType.SKELETON, Material.MONSTER_EGG, 51, 0, false),
    CHICKEN("Huhn", EntityType.CHICKEN, Material.MONSTER_EGG, 93, 0, false),
    PIG("Schwein", EntityType.PIG, Material.MONSTER_EGG, 90, 0, false),
    VILLAGER("Dorfbewohner", EntityType.VILLAGER, Material.MONSTER_EGG, 120, 0, false),
    SHEEP("Schaf", EntityType.SHEEP, Material.MONSTER_EGG, 91, 0, false),
    RABBIT("Hase", EntityType.RABBIT, Material.MONSTER_EGG, 101, 0, false),
    ZOMBIE("Zombie", EntityType.ZOMBIE, Material.MONSTER_EGG, 54, 0, false),

    COW("Kuh", EntityType.COW, Material.MONSTER_EGG, 92, 25000, true),
    MUSHROOM_COW("Pilzkuh", EntityType.MUSHROOM_COW, Material.MONSTER_EGG, 96, 100000, true),
    PIG_ZOMBIE("Zombie Pigman", EntityType.PIG_ZOMBIE, Material.MONSTER_EGG, 57, 200000, true),
    SLIME("Schleim", EntityType.SLIME, Material.MONSTER_EGG, 55, 280000, true),
    BLAZE("Blaze", EntityType.BLAZE, Material.MONSTER_EGG, 61, 350000, true),
    CREEPER("Creeper", EntityType.CREEPER, Material.MONSTER_EGG, 50, 450000, true),
    MAGMA_CUBE("Magmaschleim", EntityType.MAGMA_CUBE, Material.MONSTER_EGG, 62, 550000, true),
    WITCH("Hexe", EntityType.WITCH, Material.MONSTER_EGG, 66, 600000, true);

    private final String name;
    private final EntityType type;
    private final Material displayItem;
    private final int durability;
    private final long price;
    private final boolean premium;

    EnumSpawnerType(String name, EntityType type, Material displayItem, int durability, long price, boolean premium) {
        this.name = name;
        this.type = type;
        this.displayItem = displayItem;
        this.durability = durability;
        this.price = price;
        this.premium = premium;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public Material getDisplayItem() {
        return displayItem;
    }

    public int getDurability() {
        return durability;
    }

    public long getPrice() {
        return price;
    }

    public boolean isPremium() {
        return premium;
    }

    public static boolean hasUnlocked(User user, EnumSpawnerType type) {
        if (!type.isPremium()) return true;
        return user.getUserData().getSpawnerTypes().contains(type.getType());
    }

}
