/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public abstract class Transaction implements Listener {

    private Inventory inventory;

    private String name;
    private long price;
    private Player player;

    public Transaction(Player player, String name, long price) {
        this.player = player;
        this.name = name;
        this.price = price;

        this.inventory = Bukkit.createInventory(null, 9 * 3, "§7§lKaufbestätigung");

        for (int i = 0; i < this.inventory.getSize(); i++)
            this.inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

        for (int i = 0; i < 3; i++) {
            this.inventory.setItem(9 + i, new ItemBuilder(Material.STAINED_CLAY).setDurability(5).setDisplayName("§a§lKauf bestätigen").build());
            this.inventory.setItem(15 + i, new ItemBuilder(Material.STAINED_CLAY).setDurability(14).setDisplayName("§c§lKauf abbrechen").build());
        }

        this.inventory.setItem(13, new ItemBuilder(Material.SIGN).setDisplayName("§6§lKaufinformationen")
                .setLore("§c§lName§8: §7" + this.name, "§c§lPreis§8: §7" + Util.formatNumber(this.price) + "$").build());

        player.openInventory(this.inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract boolean onBuy();

    public abstract boolean onCancel();

}
