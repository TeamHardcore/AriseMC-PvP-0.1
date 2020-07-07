/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FriendInventory {

    private final static List<Integer> CLEAR_SLOTS = new ArrayList<>();

    static {
        CLEAR_SLOTS.addAll(Arrays.asList(20, 21, 22, 23, 24, 29, 30, 31, 32, 33));
    }

    public static void openFriendInventory(Player player, int state) {
        Inventory inventory;
        switch (state) {
            default:
                inventory = Bukkit.createInventory(null, 9 * 3, "§c§lFreunde");
                break;
            case 1:
                inventory = Bukkit.createInventory(null, 9 * 5, "§c§lDeine Freunde");
                break;
            case 2:
                inventory = Bukkit.createInventory(null, 9 * 5, "§c§lFreundschaftsanfragen");
                break;
        }

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

        updateFriendInventory(player, inventory, state);
        player.openInventory(inventory);
    }

    private static void updateFriendInventory(Player player, Inventory inventory, int state) {
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        switch (state) {
            default:
                inventory.setItem(11, new ItemBuilder(Material.SKULL_ITEM).setDisplayName("§6§lDeine Freunde").setLore("§7Du hast §6§l" + userData.getFriends().size() + " §7Freunde.", "", "§eKlicke hier§7, §eum deine Freunde zu betrachten.").setDurability(3).setSkullOwner(player.getName()).build());
                inventory.setItem(15, new ItemBuilder(Material.CHEST).setDisplayName("§6§lFreundschaftsanfragen").setLore("§7Du hast §6§l" + userData.getFriendRequests().size() + " §7offene Anfragen.", "", "§eKlicke hier§7, §eum die Anfragen zu betrachten.").build());
                break;

            case 1:
                for (Integer slot : CLEAR_SLOTS)
                    inventory.setItem(slot, new ItemBuilder(Material.AIR).build());

                for (UUID uuid : userData.getFriends()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                    boolean online = offlinePlayer.isOnline();

                    ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).setDisplayName((online ? "§a§l" : "§7") + offlinePlayer.getName()).setSkullOwner(offlinePlayer.getName()).setDurability(3).setLore("", "§eKlicke hier§7, §eum die Freundschaft aufzulösen.").build();
                    inventory.addItem(itemStack);
                }

                inventory.setItem(36, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
                break;

            case 2:
                for (Integer slot : CLEAR_SLOTS)
                    inventory.setItem(slot, new ItemBuilder(Material.AIR).build());

                for (UUID uuid : userData.getFriendRequests()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                    boolean online = offlinePlayer.isOnline();

                    ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).setDisplayName((online ? "§a§l" : "§7") + offlinePlayer.getName()).setSkullOwner(offlinePlayer.getName()).setDurability(3).setLore("", "§eLinksklicke§7, §eum die Anfrage anzunehmen", "§eRechtsklicke§7, §eum die Anfrage abzulehnen.").build();
                    inventory.addItem(itemStack);
                }

                inventory.setItem(36, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
                break;
        }
    }

}
