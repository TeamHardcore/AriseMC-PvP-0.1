/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.kits;

import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {

    private String name;
    private List<ItemStack> content;

    private Inventory preview;

    public Kit(String name, List<ItemStack> content) {
        this.name = name;
        this.content = content;

        registerPreview();
    }

    private void registerPreview() {
        int invSize = (int) (Math.ceil(this.content.size() / 9.0D) + 2.0D);
        if (invSize > 6) invSize = 6;

        this.preview = Bukkit.createInventory(null, invSize * 6, "§c§lKit-Vorschau");

        for (ItemStack itemStack : this.content)
            this.preview.addItem(itemStack);

        this.preview.setItem(this.preview.getSize() - 9, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
    }

    public String getName() {
        return name;
    }

    public List<ItemStack> getContent() {
        return content;
    }

    public Inventory getPreview() {
        return preview;
    }

    public void giveItems(Player player) {
        for (ItemStack itemStack : this.content)
            if (player.getInventory().firstEmpty() == -1)
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
            else player.getInventory().addItem(itemStack);
    }

    public boolean giveKit(Player player) {
        player.sendMessage(StringDefaults.PREFIX + "§eDu hast das Kit §7" + this.name + " §eerfolgreich abgeholt.");
        giveItems(player);
        return true;
    }

}
