/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class CasinoInventory {

    private static Inventory casinoInventory;

    static {
        registerCasinoInventory();
    }

    private static void registerCasinoInventory() {
        casinoInventory = Bukkit.createInventory(null, 9 * 3, "§c§lCasino");

        for (int i = 0; i < casinoInventory.getSize(); i++)
            casinoInventory.setItem(i,
                    new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        casinoInventory.setItem(11,
                new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lCoinflip").setLore("",
                        "§8● §e§lOffene Räume§8: §7" + Main.getInstance().getCoinFlipManager().getCoinFlips().size(),
                        "",
                        "§eKlicke§7, §eum das Menü zu öffnen.").build());

        casinoInventory.setItem(15,
                new ItemBuilder(Material.BOOK).setDisplayName("§6§lJackpot").setLore("",
                        "§8● §e§lStatus§8: §7Geschlossen",
                        "",
                        "§eKlicke§7, §eum das Menü zu öffnen.").build());
    }

    public static Inventory getCasinoInventory() {
        return casinoInventory;
    }
}
