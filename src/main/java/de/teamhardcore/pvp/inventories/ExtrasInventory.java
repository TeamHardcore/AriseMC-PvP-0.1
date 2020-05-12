/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.extras.EnumChatColor;
import de.teamhardcore.pvp.model.extras.EnumCommand;
import de.teamhardcore.pvp.model.extras.EnumPerk;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;

public class ExtrasInventory {

    private static Integer[] perkSlots = new Integer[]{11, 12, 13, 14, 15, 21, 22, 23};
    private static Integer[] commandSlots = new Integer[]{11, 12, 13, 14, 15, 21, 22, 23};
    private static Integer[] chatColorSlots = new Integer[]{11, 12, 13, 14, 15, 21, 22, 23};

    public static void openInventory(Player player, int type) {
        /*Main menu*/
        if (type == 1) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§c§lExtras");

            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

            User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
            UserData data = user.getUserData();

            inventory.setItem(11, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lPerks").setLore("", "§c§lDu besitzt§8: §7" + data.getUnlockedPerks().size() + "§8/§7" + EnumPerk.values().length).build());
            inventory.setItem(13, new ItemBuilder(Material.PAPER).setDisplayName("§c§lChat Farben").setLore("", "§c§lDu besitzt§8: §7" + data.getUnlockedChatColors().size() + "§8/§7" + EnumChatColor.values().length).build());
            inventory.setItem(15, new ItemBuilder(Material.COMMAND).setDisplayName("§c§lExtra-Befehle").setLore("", "§c§lDu besitzt§8: §7" + data.getUnlockedCommands().size() + "§8/§7" + EnumCommand.values().length).build());

            player.openInventory(inventory);
        }

        /*Perks*/
        if (type == 2) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lExtras - Perks");

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());
            }

            inventory.setItem(27, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());

            updateInventory(player, inventory, 1);
            player.openInventory(inventory);
        }

        /*Extra Befehle*/
        if (type == 3) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lExtras - Befehle");

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());
            }

            inventory.setItem(27, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());

            updateInventory(player, inventory, 2);
            player.openInventory(inventory);
        }

        if (type == 4) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lExtras - Chat Farben");

            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

            inventory.setItem(27, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());

            updateInventory(player, inventory, 3);
            player.openInventory(inventory);
        }
    }

    private static void updateInventory(Player player, Inventory inventory, int type) {
        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
        UserData data = user.getUserData();

        /*Perks*/
        if (type == 1) {
            int i = 0;
            for (EnumPerk perk : EnumPerk.values()) {
                if (i >= perkSlots.length) break;
                int slot = perkSlots[i];

                boolean hasPerk = data.getUnlockedPerks().contains(perk);
                boolean activatedPerk = data.getActivatedPerks().contains(perk);

                List<String> lore = new ArrayList<>();
                lore.add(" ");
                if (!hasPerk) {
                    lore.add("§c§lPreis§8: §7" + Util.formatNumber(perk.getPrize()) + "$");
                    lore.add(" ");
                    lore.add("§cDu hast dieses Perk noch nicht freigeschaltet.");
                    lore.add("§cKlicke§7, §cum das Perk freizuschalten.");
                } else {
                    lore.add("§8● " + (activatedPerk ? "§a§lAktiviert" : "§c§lDeaktiviert"));
                    lore.add(" ");
                    lore.add("§eKlicke§7, §eum das Perk zu " + (activatedPerk ? "de" : "") + "aktivieren.");
                }

                inventory.setItem(slot, new ItemBuilder(perk.getDisplayItem()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS).setDurability(perk.getDurability()).setDisplayName(perk.getName()).setLore(lore).build());
                i++;
            }
        }

        /*Commands*/
        if (type == 2) {
            int i = 0;
            for (EnumCommand command : EnumCommand.values()) {
                if (i >= commandSlots.length) break;
                int slot = commandSlots[i];

                boolean hasCommand = data.getUnlockedCommands().contains(command);

                List<String> lore = new ArrayList<>();
                lore.add("§7§o" + command.getDescription());
                lore.add(" ");
                if (!hasCommand) {
                    lore.add("§c§lPreis§8: §7" + Util.formatNumber(command.getPrice()) + "$");
                    lore.add(" ");
                    lore.add("§cDu hast diesen Befehl noch nicht freigeschaltet.");
                    lore.add("§cKlicke§7, §cum den Befehl freizuschalten.");
                } else {
                    lore.add("§c§lVerwendung§8: §7" + ChatColor.stripColor(command.getDisplayName()));
                }

                inventory.setItem(slot, new ItemBuilder(command.getMaterial()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS).setDisplayName(command.getDisplayName()).setLore(lore).build());
                i++;
            }
        }

        /*ChatColors*/
        if (type == 3) {
            int i = 0;
            for (EnumChatColor color : EnumChatColor.values()) {
                if (i >= chatColorSlots.length) break;
                int slot = chatColorSlots[i];

                boolean hasColor = data.getUnlockedChatColors().contains(color);
                boolean isActive = data.getActiveColor() == color;

                List<String> lore = new ArrayList<>();
                lore.add(" ");
                if (!hasColor) {
                    lore.add("§c§lPreis§8: §7" + Util.formatNumber(color.getPrice()) + "$");
                    lore.add(" ");
                    lore.add("§cDu hast diesen Befehl noch nicht freigeschaltet.");
                    lore.add("§cKlicke§7, §cum die Farbe freizuschalten.");
                } else {

                    if (isActive) {
                        lore.add("§8● §a§lAusgewählt");
                        lore.add(" ");
                    } else {
                        lore.add(" ");
                        lore.add("§eKlicke§7, §eum die Farbe auszuwählen.");
                    }
                }
                inventory.setItem(slot, new ItemBuilder(color.getMaterial()).setDurability(color.getDurability()).addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS).setDisplayName(color.getName()).setLore(lore).build());
                i++;
            }

            inventory.setItem(35, (data.getActiveColor() == null ? new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()
                    : new ItemBuilder(Material.BOWL).setDisplayName("§c§lFarbe zurücksetzen").build()));
        }

    }

}
