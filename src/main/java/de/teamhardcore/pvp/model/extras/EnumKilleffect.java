/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras;

import de.teamhardcore.pvp.model.extras.effects.AbstractKillEffect;
import de.teamhardcore.pvp.model.extras.effects.EffectBlood;
import de.teamhardcore.pvp.model.extras.effects.EffectExplosion;

public enum EnumKilleffect {

    BLOOD("§c§lBlutspritzer", EffectBlood.class),
    EXPLOSION("§8§lExplosion", EffectExplosion.class),
    ;

    private final String displayName;
    Class<? extends AbstractKillEffect> effectClass;

    EnumKilleffect(String displayName, Class<? extends AbstractKillEffect> effectClass) {
        this.displayName = displayName;
        this.effectClass = effectClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<? extends AbstractKillEffect> getEffectClass() {
        return effectClass;
    }

    public static EnumKilleffect getByName(String name) {
        for (EnumKilleffect effect : values())
            if (effect.name().equalsIgnoreCase(name))
                return effect;
        return null;
    }
}
