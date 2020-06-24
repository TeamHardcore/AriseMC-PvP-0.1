/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.model.duel.request.DuelRequest;
import de.teamhardcore.pvp.model.duel.request.DuelSettings;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DuelInventory {

    public static void openRequestInventory(Player player, boolean create, DuelRequest request) {
        if (create) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lDuell");
            IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build()));


            List<String> lore = new ArrayList<>();
            lore.add(" ");
            for (String category : request.getCategories())
                lore.add("§8● " + (request.getCurrentCategory().equals(category) ? "§a§l" : "§7") + category.replace("random", "Zufällig"));
            lore.add(" ");
            lore.add("§eKlicke§7, §eum die Map zu wechseln.");

            inventory.setItem(11, new ItemBuilder(Material.GRASS).setDisplayName("§6§lMap wählen").setLore(lore).build());

            lore.clear();
            lore.add(" ");
            lore.add("§eKlicke§7, §eum die Einstellungen zu ändern.");

            inventory.setItem(13, new ItemBuilder(Material.REDSTONE_COMPARATOR).setDisplayName("§6§lEinstellungen ändern").setLore(lore).build());

            lore.clear();
            lore.add(" ");
            lore.add("§eKlicke§7, §eum den Einsatz zu ändern.");

            inventory.setItem(15, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lEinsatz festlegen").setLore(lore).build());

            lore.clear();
            lore.add("§7Schließe die Konfiguration ab,");
            lore.add("§7um einen Spieler herauszufordern.");
            lore.add("");
            lore.add("§eKlicke§7, §eum die Konfiguration abzuschließen.");
            inventory.setItem(31, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§a§lKonfiguration abschließen").setDurability((short) 5).setLore(lore).build());
            player.openInventory(inventory);
            return;
        }
    }

    public static void openSettingsInventory(Player player, DuelRequest request) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§c§lDuelleinstellungen");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));


        List<String> lore = new ArrayList<>();
        lore.add("");
        for (String setting : request.getGoldenAppleOptions())
            lore.add("§8● " + (request.getOptionIndex(1, setting) == request.getGoldenAppleOption() ? "§a§l" : "§7") + setting);
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(11, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§6§lGoldene Äpfel nutzbar").setLore(lore).build());

        lore.clear();
        lore.add(" ");
        for (Integer limitation : request.getPotionLimitations())
            lore.add("§8● " + (request.getCurrentPotionLimitation() == limitation ? "§a§l" : "§7") + (limitation == -1 ? "Unbegrenzt" : limitation == 1 ? limitation + " Stack" : limitation + "Stacks"));
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(13, new ItemBuilder(Material.POTION).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).setDisplayName("§6§lHeiltrank Limitation").setDurability(16421).setLore(lore).build());

        lore.clear();
        lore.add("");
        for (String setting : request.getDebuffOptions())
            lore.add("§8● " + (request.getOptionIndex(0, setting) == request.getDebuffOption() ? "§a§l" : "§7") + setting);
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(15, new ItemBuilder(Material.POTION).setDurability(16420).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).setDisplayName("§6§lDebuff Tränke nutzbar").setLore(lore).build());
        lore.clear();

        inventory.setItem(18, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        player.openInventory(inventory);
    }

    public static void openDeploymentInventory(Player player, DuelRequest request) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§c§lDuelleinsatz");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));


        List<String> lore = new ArrayList<>();
        lore.add("");
        for (String setting : request.getArmorOptions())
            lore.add("§8● " + (request.getOptionIndex(2, setting) == request.getArmorOption() ? "§a§l" : "§7") + setting);
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(11, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("§6§lEigenes Inventar").setLore(lore).build());

        lore.clear();
        lore.add(" ");
        lore.add("§8● §e§lMünzen§8: §7" + Util.formatNumber(request.getCoins()));
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(13, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lMünzen").setLore(lore).build());

        inventory.setItem(18, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        player.openInventory(inventory);
    }

     /*public static void openDuelRequestInventory(Player player, boolean create, DuelConfiguration configuration) {
        if (create) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§c§lDuell erstellen");
            IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build()));

            DuelSettings settings = configuration.getSettings();
            DuelDeployment deployment = configuration.getDeployment();

            inventory.setItem(4, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lDuell").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).build());

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
                            "§8● " + (settings.canUseGoldenApple() ? "§a§l" : "§7") + "aktiviert", "§8● " + (settings.canUseGoldenApple() ? "§7" : "§c§l") + "deaktiviert", "", "§eKlicke§7, §eum die Einstellung zu ändern.").build());

            inventory.setItem(24, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lWetteinsatz")
                    .setLore("", "§8● §eHöhe§8: §7" + Util.formatNumber(deployment.getCoins()) + "$", "", "§eKlicke§7, §eum den Einsatz zu ändern.").build())
            ;
            inventory.setItem(40, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§a§lKonfiguration abschließen").setDurability((short) 5).setLore("", "§eKlicke§7, §eum die Konfiguration abzuschließen.").build());

            player.openInventory(inventory);
            return;
        }
    }*/

     /* public static void openDuelSettingsInventory(Player player, DuelConfiguration creator) {
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
                        "§8● " + (settings.canUseGoldenApple() ? "§a§l" : "§7") + "aktiviert", "§8● " + (settings.canUseGoldenApple() ? "§7" : "§c§l") + "deaktiviert", "", "§eKlicke§7, §eum die Einstellung zu ändern.").build());

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

        inventory.setItem(36, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());

        player.openInventory(inventory);
    }*/

}
