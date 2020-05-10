/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.customspawner;

import de.teamhardcore.pvp.user.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public abstract class AbstractSpawnerType {

    public abstract String getName();

    public abstract EntityType getType();

    public abstract Material getDisplayItem();

    public abstract short getDurability();

    public abstract long getPrice();

    public abstract boolean isPremium();

    public boolean hasUnlocked(User user) {
        if (!isPremium()) return true;
        if (!user.getUserData().getSpawnerTypes().contains(getType())) return false;
        return true;
    }

}
