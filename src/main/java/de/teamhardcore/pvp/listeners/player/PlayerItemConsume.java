/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsume implements Listener {

    private final Main plugin;

    public PlayerItemConsume(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack.getType().equals(Material.GOLDEN_APPLE) && itemStack.getDurability() == 1) {

            if (this.plugin.getManager().getGoldenAppleCooldown().containsKey(player)) {
                long diff = this.plugin.getManager().getGoldenAppleCooldown().get(player) - System.currentTimeMillis();

                if (diff / 1000L > 0L) {
                    event.setCancelled(true);
                    player.sendMessage(StringDefaults.PREFIX + "§cDu musst noch §7" + TimeUtil.timeToString(diff) + " §cwarten.");
                    return;
                }

                this.plugin.getManager().getGoldenAppleCooldown().remove(player);
            }

            event.setCancelled(true);
            Util.removeItems(player.getInventory(), player.getItemInHand(), 1);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 3, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300 * 20, 0, true, true));
            this.plugin.getManager().getGoldenAppleCooldown().put(player, System.currentTimeMillis() + 120000L);
            System.out.println("put " + player.getName() + " in hashmap");
        }


    }

}
