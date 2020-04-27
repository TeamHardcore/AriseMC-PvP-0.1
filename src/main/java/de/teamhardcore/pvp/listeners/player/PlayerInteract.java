/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.crates.AbstractCrate;
import de.teamhardcore.pvp.model.crates.CrateOpening;
import de.teamhardcore.pvp.model.crates.animation.SkipAnimation;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {

    private final Main plugin;

    public PlayerInteract(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;

        if (itemStack.getType().equals(Material.CHEST) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§a§lTest Crate")) {
            event.setCancelled(true);

            AbstractCrate crate = this.plugin.getCrateManager().getCrate("Test");
            CrateOpening opening = new CrateOpening(player, crate);
            opening.startOpening(new SkipAnimation(opening));

            //TODO: CHECK SETTINGS


        }

    }

}