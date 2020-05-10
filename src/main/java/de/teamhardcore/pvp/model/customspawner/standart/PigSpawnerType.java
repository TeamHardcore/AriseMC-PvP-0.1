/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.customspawner.standart;

import de.teamhardcore.pvp.model.customspawner.AbstractSpawnerType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class PigSpawnerType extends AbstractSpawnerType {
    @Override
    public String getName() {
        return "Schwein";
    }

    @Override
    public EntityType getType() {
        return EntityType.PIG;
    }

    @Override
    public Material getDisplayItem() {
        return Material.MONSTER_EGG;
    }

    @Override
    public short getDurability() {
        return 90;
    }

    @Override
    public long getPrice() {
        return 0L;
    }

    @Override
    public boolean isPremium() {
        return false;
    }
}
