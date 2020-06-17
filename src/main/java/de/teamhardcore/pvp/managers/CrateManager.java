/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.crates.CrateOpening;
import de.teamhardcore.pvp.model.gambling.crates.addons.DivineCrate;
import de.teamhardcore.pvp.model.gambling.crates.addons.TestCrate;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class CrateManager {

    public static final int[] CRATE_SLOTS = new int[14];
    private final Map<String, BaseCrate> availableCrates = new HashMap<String, BaseCrate>() {{
        TestCrate testCrate = new TestCrate();
        put(testCrate.getName(), new BaseCrate(testCrate));
        DivineCrate divineCrate = new DivineCrate();
        put(divineCrate.getName(), new BaseCrate(divineCrate));
    }};
    private final Map<Player, CrateOpening> playersInCrateOpening;

    private final Main plugin;

    static {
        int counter = 0;
        for (int line = 1; line < 3; line++) {
            for (int i = 0; i <= 6; i++) {
                CRATE_SLOTS[counter] = i + 10 + line * 9;
                counter++;
            }
        }
    }

    public CrateManager(Main plugin) {
        this.plugin = plugin;
        this.playersInCrateOpening = new HashMap<>();
    }

    public void openCrateInventory(Player player, int page) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lCrates");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        inventory.setItem(2, new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§c§lTEST CRATE KAUFEN").build());
        inventory.setItem(4, new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§c§lDIVINE CRATE KAUFEN").build());
        inventory.setItem(6, new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§c§lULTRA CRATE KAUFEN").build());
        switchPage(player, inventory, page);
        player.openInventory(inventory);
        player.updateInventory();
    }

    public void updateInventory(Player player, Inventory inventory) {
        UserData userData = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData();
        List<BaseCrate> allCrates = new CopyOnWriteArrayList<>(userData.getOwnedCrates());

        for (int i : CRATE_SLOTS)
            inventory.setItem(i, new ItemBuilder(Material.AIR).build());

        int currentPage = Integer.parseInt(inventory.getItem(49).getItemMeta().getDisplayName().substring(12));

        int totalCrates = allCrates.size();
        int cratesToShow = 0;

        for (int i = 0; i < CRATE_SLOTS.length; i++) {
            int cratePosition = (currentPage - 1) * CRATE_SLOTS.length + i;

            if (totalCrates <= cratePosition)
                break;

            cratesToShow++;
            BaseCrate crate = allCrates.get(cratePosition);
            inventory.setItem(i + 19 + i / 7 * 2, crate.getMenuItem());
        }

        if (cratesToShow == 0 && currentPage > 1) {
            switchPage(player, inventory, currentPage - 1);
            return;
        }

        if (currentPage > 1)
            inventory.setItem(47, new ItemBuilder(Material.ARROW).setDisplayName("§7Zu Seite §al" + (currentPage - 1)).build());
        if (totalCrates - currentPage * CRATE_SLOTS.length > 0)
            inventory.setItem(51, new ItemBuilder(Material.ARROW).setDisplayName("§7Zu Seite §al" + (currentPage + 1)).build());
    }

    public void switchPage(Player player, Inventory inventory, int page) {
        if (page < 1) return;
        for (int i = 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        }
        inventory.setItem(49, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 14).setDisplayName("§7Seite §a§l" + page).build());
        updateInventory(player, inventory);
    }

    public BaseCrate getCrate(String name) {
        if (!this.availableCrates.containsKey(name)) return null;
        return this.availableCrates.get(name);
    }

    public void stopAllOpenings() {
        for (Map.Entry<Player, CrateOpening> entry : this.playersInCrateOpening.entrySet()) {
            Player target = entry.getKey();
            target.closeInventory();
            target.sendMessage(StringDefaults.PREFIX + "§cDas Crate-Opening wurde abgebrochen.");
            entry.getValue().cancelTask();
            this.plugin.getUserManager().getUser(target.getUniqueId()).getUserData().addCrate(entry.getValue().getBaseCrate());
        }
    }

    public Main getPlugin() {
        return plugin;
    }

    public Map<String, BaseCrate> getAvailableCrates() {
        return availableCrates;
    }

    public Map<Player, CrateOpening> getPlayersInCrateOpening() {
        return playersInCrateOpening;
    }
}
