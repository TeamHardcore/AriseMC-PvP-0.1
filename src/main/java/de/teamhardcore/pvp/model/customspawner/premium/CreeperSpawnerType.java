/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.customspawner.premium;

import de.teamhardcore.pvp.model.customspawner.AbstractSpawnerType;
import de.teamhardcore.pvp.user.User;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class CreeperSpawnerType extends AbstractSpawnerType {
    @Override
    public String getName() {
        return "Creeper";
    }

    @Override
    public EntityType getType() {
        return EntityType.CREEPER;
    }

    @Override
    public Material getDisplayItem() {
        return Material.MONSTER_EGG;
    }

    @Override
    public short getDurability() {
        return 50;
    }

    @Override
    public long getPrice() {
        return 250000;
    }

    @Override
    public boolean isPremium() {
        return true;
    }
}
