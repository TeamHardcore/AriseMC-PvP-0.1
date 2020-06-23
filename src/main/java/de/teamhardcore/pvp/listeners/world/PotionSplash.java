/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.world;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.duel.Duel;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionSplash implements Listener {

    /**
     * TODO: CANCEL GOLDEN APPLE WHEN IS NOT ACTIVATED
     * TODO: GIVE REWARDS TO WINNING PERSON
     */

    private final Main plugin;

    public PotionSplash(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSplash(PotionSplashEvent event) {
        ThrownPotion potion = event.getPotion();

        Player player = (Player) potion.getShooter();

        for (PotionEffect effect : potion.getEffects()) {
            if (effect.getType().equals(PotionEffectType.HEAL) || effect.getType().equals(PotionEffectType.HEALTH_BOOST)) {
                if (this.plugin.getDuelManager().getDuelCache().containsKey(player.getUniqueId())) {
                    Duel duel = this.plugin.getDuelManager().getDuelCache().get(player.getUniqueId());

                    int remainingPotions = duel.getRemainingPotions(player);

                    if (duel.hasRemainingPotions(player))
                        duel.setRemainingPotions(player, 1);

                    if (remainingPotions == 64 || remainingPotions == 32 || remainingPotions == 10)
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cAchtung, du kannst nur noch §7" + remainingPotions + " §cHeiltränke benutzen.");

                    if (!duel.hasRemainingPotions(player)) {
                        event.setCancelled(true);
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu kannst keine Heiltränke mehr verwenden.");
                        return;
                    }
                }
            }
        }

    }

}
