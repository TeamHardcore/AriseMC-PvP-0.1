/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.model.duel.request.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.request.DuelRequest;
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

    public static void openRequestInventory(Player player, boolean create, DuelConfiguration configuration) {
        if (create) {
            Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lDuell");
            IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build()));


            List<String> lore = new ArrayList<>();
            lore.add(" ");
            for (String category : configuration.getCategories())
                lore.add("§8● " + (configuration.getCurrentCategory().equals(category) ? "§a§l" : "§7") + category.replace("random", "Zufällig"));
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

        Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lDuell annehmen");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build()));

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("§8● §eAusgewählt§8: §7" + configuration.getCurrentCategory().replace("random", "Zufällig"));
        lore.add(" ");

        inventory.setItem(11, new ItemBuilder(Material.GRASS).setDisplayName("§6§lMap").setLore(lore).build());

        lore.clear();
        lore.add(" ");
        lore.add("§8● §eMaximale Heiltränke§8: §7" + (configuration.getCurrentPotionLimitation() == -1 ? "Unbegrenzt" : configuration.getCurrentPotionLimitation() == 1 ? configuration.getCurrentPotionLimitation() + " Stack" : configuration.getCurrentPotionLimitation() + " Stacks"));
        lore.add("§8● §eGoldene Äpfel§8: §7" + configuration.getGoldenAppleOptions().get(configuration.getGoldenAppleOption()).replace("aus", "§caus").replace("an", "§aan"));
        lore.add("§8● §eDebuff Tränke§8: §7" + configuration.getDebuffOptions().get(configuration.getDebuffOption()).replace("aus", "§caus").replace("an", "§aan"));
        lore.add(" ");

        inventory.setItem(13, new ItemBuilder(Material.REDSTONE_COMPARATOR).setDisplayName("§6§lEinstellungen").setLore(lore).build());

        lore.clear();
        lore.add(" ");
        lore.add("§8● §eMünzen§8: §7" + Util.formatNumber(configuration.getCoins()) + "$");
        lore.add("§8● §eEigenes Inventar§8: §7" + configuration.getArmorOptions().get(configuration.getArmorOption()).replace("aus", "§caus").replace("an", "§aan"));
        lore.add(" ");

        inventory.setItem(15, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lEinsatz").setLore(lore).build());

        lore.clear();
        lore.add("§7Lehne die Herausforderung ab,");
        lore.add("§7um das Duell nicht zu starten.");
        lore.add("");
        lore.add("§eKlicke§7, §eum die Herausforderung abzulehnen.");
        inventory.setItem(30, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§c§lHerausforderung ablehnen").setDurability((short) 14).setLore(lore).build());

        lore.clear();
        lore.add("§7Nehme die Herausforderung an,");
        lore.add("§7um das Duell zu starten.");
        lore.add("");
        lore.add("§eKlicke§7, §eum die Herausforderung anzunehmen.");
        inventory.setItem(32, new ItemBuilder(Material.STAINED_CLAY).setDisplayName("§a§lHerausforderung annehmen").setDurability((short) 5).setLore(lore).build());
        player.openInventory(inventory);

    }

    public static void openSettingsInventory(Player player, DuelConfiguration configuration) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§c§lDuelleinstellungen");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));


        List<String> lore = new ArrayList<>();
        lore.add("");
        for (String setting : configuration.getGoldenAppleOptions())
            lore.add("§8● " + (configuration.getOptionIndex(1, setting) == configuration.getGoldenAppleOption() ? "§a§l" : "§7") + setting);
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(11, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§6§lGoldene Äpfel nutzbar").setLore(lore).build());

        lore.clear();
        lore.add(" ");
        for (Integer limitation : configuration.getPotionLimitations())
            lore.add("§8● " + (configuration.getCurrentPotionLimitation() == limitation ? "§a§l" : "§7") + (limitation == -1 ? "Unbegrenzt" : limitation == 1 ? limitation + " Stack" : limitation + " Stacks"));
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(13, new ItemBuilder(Material.POTION).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).setDisplayName("§6§lHeiltrank Limitation").setDurability(16421).setLore(lore).build());

        lore.clear();
        lore.add("");
        for (String setting : configuration.getDebuffOptions())
            lore.add("§8● " + (configuration.getOptionIndex(0, setting) == configuration.getDebuffOption() ? "§a§l" : "§7") + setting);
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(15, new ItemBuilder(Material.POTION).setDurability(16420).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).setDisplayName("§6§lDebuff Tränke nutzbar").setLore(lore).build());
        lore.clear();

        inventory.setItem(18, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        player.openInventory(inventory);
    }

    public static void openDeploymentInventory(Player player, DuelConfiguration configuration) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§c§lDuelleinsatz");
        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build()));


        List<String> lore = new ArrayList<>();
        lore.add("");
        for (String setting : configuration.getArmorOptions())
            lore.add("§8● " + (configuration.getOptionIndex(2, setting) == configuration.getArmorOption() ? "§a§l" : "§7") + setting);
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(11, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setDisplayName("§6§lEigenes Inventar").setLore(lore).build());

        lore.clear();
        lore.add(" ");
        lore.add("§8● §e§lMünzen§8: §7" + Util.formatNumber(configuration.getCoins()) + "$");
        lore.add(" ");

        lore.add("§eKlicke§7, §eum die Einstellung zu ändern.");
        inventory.setItem(13, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§6§lMünzen").setLore(lore).build());

        inventory.setItem(18, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        player.openInventory(inventory);
    }

}
