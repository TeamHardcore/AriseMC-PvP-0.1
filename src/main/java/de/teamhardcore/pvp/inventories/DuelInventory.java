/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.configuration.DuelDeployment;
import de.teamhardcore.pvp.model.duel.configuration.DuelSettings;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.stream.IntStream;

public class DuelInventory {

    public static void openDuelRequestInventory(Player player, boolean create, DuelConfiguration configuration) {
        if (create) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§c§lDuell erstellen");
            IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build()));

            DuelSettings settings = configuration.getSettings();

            inventory.setItem(4, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lDuell").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

            inventory.setItem(22, new ItemBuilder(Material.REDSTONE_COMPARATOR).setDisplayName("§6§lEinstellungen").setLore("",
                    "§eMaximale Heiltränke§8: §7" + (settings.getMaxHealStacks() == -1 ? "Unbegrenzt" : settings.getMaxHealStacks() == 1 ? "1 Stack" : settings.getMaxHealStacks() + " Stacks") + "",
                    "§eOP-Äpfel§8: " + (settings.isUseGoldenApple() ? "§a§laktiviert" : "§c§ldeaktiviert"),
                    "§eGift-Tränke§8: " + (settings.isUsePoison() ? "§a§laktiviert" : "§c§ldeaktiviert"), "", "§eKlicke§7, §eum die Einstellungen zu ändern.").build());

            inventory.setItem(24, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lEinsatz")
                    .setLore("",
                            "§eMünzen§8: §7" + Util.formatNumber(configuration.getDeployment().getCoins()) + "$",
                            "§eInventar§8: " + (configuration.getDeployment().isInventory() ? "§a§laktiviert" : "§c§ldeaktiviert"),
                            "",
                            "§eKlicke§7, §eum den Einsatz zu ändern.").build());

            inventory.setItem(40, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§a§lKonfiguration abschließen").setDurability((short) 5).setLore("", "§eKlicke§7, §eum die Konfiguration abzuschließen.").build());

            player.openInventory(inventory);
            return;
        }

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lDuell annehmen");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));
        inventory.setItem(4, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lDuell").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

        DuelSettings settings = configuration.getSettings();
        DuelDeployment rewards = configuration.getDeployment();

        inventory.setItem(20, new ItemBuilder(Material.REDSTONE_COMPARATOR).setDisplayName("§6§lEinstellungen").setLore(
                "§eMaximale Heiltränke§8: §7" + (settings.getMaxHealStacks() == -1 ? "Unbegrenzt" : settings.getMaxHealStacks() == 1 ? "1 Stack" : settings.getMaxHealStacks() + " Stacks") + "",
                "§eOP-Äpfel§8: " + (settings.isUseGoldenApple() ? "§a§laktiviert" : "§c§ldeaktiviert"),
                "§eGift-Tränke§8: " + (settings.isUsePoison() ? "§a§laktiviert" : "§c§ldeaktiviert")).build());


        inventory.setItem(22, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lEinsatz").setLore(
                "§eMünzen§8: §7" + Util.formatNumber(rewards.getCoins()) + "$",
                "§eInventar§8: §7" + (rewards.isInventory() ? "§a§laktiviert" : "§c§ldeaktiviert")).build());

        inventory.setItem(24, new ItemBuilder(Material.GRASS).setDisplayName("§6§lMap").setLore("§eAusgewählt§8: §7" + configuration.getArenaType().getDisplayName()).build());

        inventory.setItem(39, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 14).setDisplayName("§c§lDuell ablehnen").setLore("§7§oLehne die Duellanfrage ab").build());
        inventory.setItem(41, new ItemBuilder(Material.STAINED_CLAY).setDurability((short) 5).setDisplayName("§a§lDuell annehmen").setLore("§7§oNehme die Duellanfrage an").build());

        player.openInventory(inventory);
    }

    public static void openDuelSettingsInventory(Player player, DuelConfiguration creator) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§c§lDuelleinstellungen");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));
        inventory.setItem(4, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lDuell").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

        DuelSettings settings = creator.getSettings();

        inventory.setItem(20, new ItemBuilder(Material.POTION).setDurability((short) 16421).setDisplayName("§6§lMaximale Heiltränke")
                .setLore("",
                        "§8● " + (settings.getMaxHealStacks() == -1 ? "§a§l" : "§7") + "Unbegrenzt",
                        "§8● " + (settings.getMaxHealStacks() == 1 ? "§a§l" : "§7") + "1 Stack",
                        "§8● " + (settings.getMaxHealStacks() == 2 ? "§a§l" : "§7") + "2 Stacks",
                        "§8● " + (settings.getMaxHealStacks() == 4 ? "§a§l" : "§7") + "4 Stacks",
                        "§8● " + (settings.getMaxHealStacks() == 6 ? "§a§l" : "§7") + "6 Stacks",
                        "§8● " + (settings.getMaxHealStacks() == 10 ? "§a§l" : "§7") + "10 Stacks", "", "§eKlicke§7, §eum die Einstellung zu ändern.").build());

        inventory.setItem(22, new ItemBuilder(Material.GOLDEN_APPLE).setDurability((short) 1).setDisplayName("§6§lGoldene Äpfel")
                .setLore("",
                        "§8● " + (settings.isUseGoldenApple() ? "§a§l" : "§7") + "aktiviert", "§8● " + (settings.isUseGoldenApple() ? "§7" : "§c§l") + "deaktiviert", "", "§eKlicke§7, §eum die Einstellung zu ändern.").build());

        inventory.setItem(24, new ItemBuilder(Material.POTION).setDurability((short) 16420).setDisplayName("§6§lGift Tränke")
                .setLore("",
                        "§8● " + (settings.isUsePoison() ? "§a§l" : "§7") + "aktiviert", "§8● " + (settings.isUsePoison() ? "§7" : "§c§l") + "deaktiviert", "", "§eKlicke§7, §eum die Einstellung zu ändern.").build());
        inventory.setItem(36, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());

        player.openInventory(inventory);
    }

    public static void openDuelRewardsInventory(Player player, DuelConfiguration creator) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§c§lDuelleinsatz");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));
        inventory.setItem(4, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lDuell").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

        inventory.setItem(20, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lMünzen")
                .setLore("", "§8● §eHöhe§8: §7" + Util.formatNumber(creator.getDeployment().getCoins()) + "$", "", "§eKlicke§7, §eum den Einsatz zu ändern.").build());

        inventory.setItem(24, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("§6§lEigenes Inventar")
                .setLore("", "§8● " + (creator.getDeployment().isInventory() ? "§a§l" : "§7") + "aktiviert",
                        "§8● " + (creator.getDeployment().isInventory() ? "§7" : "§c§l") + "deaktiviert",
                        "", "§eKlicke§7, §eum den Einsatz zu ändern.").build());

        inventory.setItem(36, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());

        player.openInventory(inventory);
    }

}
