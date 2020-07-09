/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras;

import de.teamhardcore.pvp.model.extras.effects.AbstractKillEffect;
import de.teamhardcore.pvp.model.extras.effects.EffectBlood;
import de.teamhardcore.pvp.model.extras.effects.EffectExplosion;
import org.bukkit.Material;

public enum EnumKilleffect {

    BLOOD("§c§lBlutspritzer", EffectBlood.class, Material.REDSTONE, 0, 200000),
    EXPLOSION("§8§lExplosion", EffectExplosion.class, Material.TNT, 0, 200000),
    ;

    private final String displayName;
    private final Class<? extends AbstractKillEffect> effectClass;
    private final Material material;
    private final int durability;
    private final long price;

    EnumKilleffect(String displayName, Class<? extends AbstractKillEffect> effectClass, Material material, int durability, long price) {
        this.displayName = displayName;
        this.effectClass = effectClass;
        this.material = material;
        this.durability = durability;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<? extends AbstractKillEffect> getEffectClass() {
        return effectClass;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDurability() {
        return durability;
    }

    public long getPrice() {
        return price;
    }

    public static EnumKilleffect getByName(String name) {
        for (EnumKilleffect effect : values())
            if (effect.name().equalsIgnoreCase(name))
                return effect;
        return null;
    }
}
