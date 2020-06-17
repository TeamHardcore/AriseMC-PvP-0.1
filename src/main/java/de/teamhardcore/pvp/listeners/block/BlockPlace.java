/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.block;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.EnumSpawnerType;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BlockPlace implements Listener {

    private final Main plugin;

    public BlockPlace(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        if (itemStack.getType().equals(Material.MOB_SPAWNER)) {
            EnumSpawnerType type = this.plugin.getSpawnerManager().getSpawnerType(EntityType.PIG);
            UUID ownerUUID = player.getUniqueId();

            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§e§lSpawner")) {
                NBTItem item = new NBTItem(itemStack);
                if (item.hasKey("spawnerType"))
                    type = this.plugin.getSpawnerManager().getSpawnerType(EntityType.valueOf(item.getString("spawnerType")));

                if (item.hasKey("owner"))
                    ownerUUID = UUID.fromString(item.getString("owner"));
            }

            CreatureSpawner spawner = (CreatureSpawner) event.getBlockPlaced().getState();
            spawner.setSpawnedType(type.getType());
            spawner.update();

            this.plugin.getSpawnerManager().addSpawner(ownerUUID, type, event.getBlock().getLocation());
        }

    }

}
