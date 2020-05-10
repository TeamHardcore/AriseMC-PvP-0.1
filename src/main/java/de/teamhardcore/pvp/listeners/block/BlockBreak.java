/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.block;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {

    private final Main plugin;

    public BlockBreak(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block == null) return;

        if (block.getType() == Material.MOB_SPAWNER) {
            event.setCancelled(true);

            CustomSpawner customSpawner = this.plugin.getSpawnerManager().getCustomSpawner(block.getLocation());
            if (customSpawner == null || !customSpawner.getOwner().equals(player.getUniqueId()) && !player.hasPermission("arisemc.spawner.admin")) {
                block.setType(Material.AIR);
                return;
            }

            block.setType(Material.AIR);
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Spawner erfolgreich abgebaut.");

            ItemStack toAdd = new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§e§lSpawner").setLore("", "§c§lBesitzer§8: §7" + customSpawner.getOwner().toString(), "§c§lSpawner Typ§8: §7" + customSpawner.getType().getType().name()).build();
            NBTItem item = new NBTItem(toAdd);
            item.setString("owner", customSpawner.getOwner().toString());
            item.setString("spawnerType", customSpawner.getType().getType().name());
            toAdd = item.getItem();

            if (player.getInventory().firstEmpty() == -1)
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), toAdd);
            else player.getInventory().addItem(toAdd);

            this.plugin.getSpawnerManager().removeSpawner(block.getLocation());
        }
    }
}
