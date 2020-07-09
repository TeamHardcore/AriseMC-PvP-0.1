/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras.effects;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.extras.EnumKilleffect;
import de.teamhardcore.pvp.utils.particle.ParticleEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EffectExplosion extends AbstractKillEffect {

    public EffectExplosion(Player player) {
        super(player);
    }

    @Override
    public void playEffect() {
        ParticleEffect.EXPLOSION_HUGE.display(0.0F, 0.0F, 0.0F, 0.0F, 1, getPlayer().getEyeLocation(), 50.0D);
        getPlayer().getWorld().playSound(getPlayer().getEyeLocation(), Sound.EXPLODE, 1.0F, 0.75F);
        Main.getInstance().getManager().stopKillEffect(this);
    }

    @Override
    public void stopEffect() {

    }

    @Override
    public EnumKilleffect getEffectType() {
        return null;
    }
}
