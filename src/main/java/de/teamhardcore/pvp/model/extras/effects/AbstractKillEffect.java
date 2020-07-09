/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras.effects;

import de.teamhardcore.pvp.model.extras.EnumKilleffect;
import org.bukkit.entity.Player;

public abstract class AbstractKillEffect {

    private final Player player;

    public AbstractKillEffect(Player player) {
        this.player = player;
    }

    public abstract void playEffect();

    public abstract void stopEffect();

    public abstract EnumKilleffect getEffectType();

    public Player getPlayer() {
        return player;
    }
}
