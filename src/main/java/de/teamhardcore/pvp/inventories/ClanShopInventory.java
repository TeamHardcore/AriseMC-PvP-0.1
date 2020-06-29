/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.shop.ClanShopManager;
import de.teamhardcore.pvp.model.clan.shop.upgrades.EnumUpgrade;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.AbstractRequirement;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.RequirementType;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ClanShopInventory {

    public static void openClanShop(Player player, Clan clan) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 5, "§c§lClan-Shop");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        }


      /*  inventory.setItem(20, new ItemBuilder(Material.PAINTING).setDisplayName("§6§lClan Designs").build());
        inventory.setItem(22, new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§6§lClan Upgrades").build());
        inventory.setItem(24, new ItemBuilder(Material.BARRIER).setDisplayName("§f§kIIIIIIII").build());*/

        updateInventory(player, clan, inventory);
        player.openInventory(inventory);
    }

    private static void updateInventory(Player player, Clan clan, Inventory inventory) {
        int clanLevel = clan.getLevel();
        int maxLevel = 4;
        EnumUpgrade nextLevel = EnumUpgrade.getNextUpgrade(clanLevel, StringDefaults.LEVEL_UPGRADE);

        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add("§cLevel§8: §7" + clanLevel + "§8/§7" + maxLevel);
        lore.add(" ");

        if (nextLevel == null) {
            lore.add("§cDein Clan hat das maximale Level erreicht..");
        } else {
            boolean canBuyWarp = Main.getInstance().getClanManager().getClanShopManager().canPurchaseUpgrade(clan, player, nextLevel.getUpgrade());
            long slotPrice = 0;

            if (nextLevel.getUpgrade().hasMoreRequirements()) {
                lore.add("§cAnforderungen§8: ");
                for (AbstractRequirement requirement : nextLevel.getUpgrade().getRequirements()) {
                    if (requirement.getType() == RequirementType.MONEY) continue;
                    lore.add("§8- §r" + requirement.getType().getDisplayName().replace("%requirement%", String.valueOf(Util.formatNumber(requirement.getNeeded()))));
                }
                lore.add(" ");
            }

            for (AbstractRequirement requirement : nextLevel.getUpgrade().getRequirements()) {
                if (requirement.getType() != RequirementType.MONEY) continue;

                slotPrice = requirement.getNeeded();
            }

            lore.add("§cPreis für das Level§8: §7" + Util.formatNumber(slotPrice) + "$");
            lore.add(" ");
            lore.add(((canBuyWarp ? "§eKlicke§7, §eum das Level zu erwerben." : "§cDein Clan kann dieses Level noch nicht erwerben.")));

        }
        inventory.setItem(4, new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName("§6§lLevel").setLore(lore).build());
        lore.clear();

        int clanSlotLevel = clan.getUpgradeLevel(StringDefaults.SLOT_UPGRADE);
        int maxSlotLevel = EnumUpgrade.getUpgrades(StringDefaults.SLOT_UPGRADE);
        EnumUpgrade nextSlot = EnumUpgrade.getNextUpgrade(clanSlotLevel, StringDefaults.SLOT_UPGRADE);

        lore.add(" ");
        lore.add("§cStufe§8: §7" + clanSlotLevel + "§8/§7" + maxSlotLevel);
        lore.add(" ");

        if (nextSlot == null) {
            lore.add("§cDein Clan hat die maximale Stufe erreicht.");
        } else {
            boolean canBuySlot = Main.getInstance().getClanManager().getClanShopManager().canPurchaseUpgrade(clan, player, nextSlot.getUpgrade());
            long slotPrice = 0;

            if (nextSlot.getUpgrade().hasMoreRequirements()) {
                lore.add("§cAnforderungen§8: ");
                for (AbstractRequirement requirement : nextSlot.getUpgrade().getRequirements()) {
                    if (requirement.getType() == RequirementType.COINS) continue;
                    lore.add("§8- §r" + requirement.getType().getDisplayName().replace("%requirement%", String.valueOf(Util.formatNumber(requirement.getNeeded()))));
                }
                lore.add(" ");
            }

            for (AbstractRequirement requirement : nextSlot.getUpgrade().getRequirements()) {
                if (requirement.getType() != RequirementType.COINS) continue;

                slotPrice = requirement.getNeeded();
            }

            lore.add("§cPreis für die Stufe§8: §7" + Util.formatNumber(slotPrice) + " Clan-Coins");
            lore.add(" ");
            lore.add(((canBuySlot ? "§eKlicke§7, §eum die Stufe freizuschalten." : "§cDein Clan kann diese Stufe noch nicht freischalten.")));
        }

        inventory.setItem(20, new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setDisplayName("§6§lMitglieder").setLore(lore).build());
        lore.clear();


        int clanWarpLevel = clan.getUpgradeLevel(StringDefaults.WARP_UPGRADE);
        int maxWarpLevel = EnumUpgrade.getUpgrades(StringDefaults.WARP_UPGRADE);
        EnumUpgrade nextWarp = EnumUpgrade.getNextUpgrade(clanWarpLevel, StringDefaults.WARP_UPGRADE);

        lore.add(" ");
        lore.add("§cStufe§8: §7" + clanWarpLevel + "§8/§7" + maxWarpLevel);
        lore.add(" ");

        if (nextWarp == null) {
            lore.add("§cDein Clan hat die maximale Stufe erreicht.");
        } else {
            boolean canBuyWarp = Main.getInstance().getClanManager().getClanShopManager().canPurchaseUpgrade(clan, player, nextWarp.getUpgrade());
            long slotPrice = 0;

            if (nextWarp.getUpgrade().hasMoreRequirements()) {
                lore.add("§cAnforderungen§8: ");
                for (AbstractRequirement requirement : nextWarp.getUpgrade().getRequirements()) {
                    if (requirement.getType() == RequirementType.COINS) continue;
                    lore.add("§8- §r" + requirement.getType().getDisplayName().replace("%requirement%", String.valueOf(Util.formatNumber(requirement.getNeeded()))));
                }
                lore.add(" ");
            }

            for (AbstractRequirement requirement : nextWarp.getUpgrade().getRequirements()) {
                if (requirement.getType() != RequirementType.COINS) continue;

                slotPrice = requirement.getNeeded();
            }

            lore.add("§cPreis für die Stufe§8: §7" + Util.formatNumber(slotPrice) + " Clan-Coins");
            lore.add(" ");
            lore.add(((canBuyWarp ? "§eKlicke§7, §eum die Stufe freizuschalten." : "§cDein Clan kann diese Stufe noch nicht freischalten.")));
        }

        inventory.setItem(22, new ItemBuilder(Material.WORKBENCH).setDisplayName("§6§lWarps").setLore(lore).build());
        lore.clear();

        int clanChestLevel = clan.getUpgradeLevel(StringDefaults.CHEST_UPGRADE);
        int maxChestLevel = EnumUpgrade.getUpgrades(StringDefaults.CHEST_UPGRADE);
        EnumUpgrade nextChest = EnumUpgrade.getNextUpgrade(clanChestLevel, StringDefaults.CHEST_UPGRADE);

        lore.add(" ");
        lore.add("§cStufe§8: §7" + clanChestLevel + "§8/§7" + maxChestLevel);
        lore.add(" ");

        if (nextChest == null) {
            lore.add("§cDein Clan hat die maximale Stufe erreicht.");
        } else {
            boolean canBuyChest = Main.getInstance().getClanManager().getClanShopManager().canPurchaseUpgrade(clan, player, nextChest.getUpgrade());
            long slotPrice = 0;

            if (nextChest.getUpgrade().hasMoreRequirements()) {
                lore.add("§cAnforderungen§8: ");
                for (AbstractRequirement requirement : nextChest.getUpgrade().getRequirements()) {
                    if (requirement.getType() == RequirementType.COINS) continue;
                    lore.add("§8- §r" + requirement.getType().getDisplayName().replace("%requirement%", String.valueOf(Util.formatNumber(requirement.getNeeded()))));
                }
                lore.add(" ");
            }

            for (AbstractRequirement requirement : nextChest.getUpgrade().getRequirements()) {
                if (requirement.getType() != RequirementType.COINS) continue;

                slotPrice = requirement.getNeeded();
            }

            lore.add("§cPreis für die Stufe§8: §7" + Util.formatNumber(slotPrice) + " Clan-Coins");
            lore.add(" ");
            lore.add(((canBuyChest ? "§eKlicke§7, §eum die Stufe freizuschalten." : "§cDein Clan kann diese Stufe noch nicht freischalten.")));
        }

        inventory.setItem(24, new ItemBuilder(Material.ENDER_CHEST).setDisplayName("§6§lClan Kiste").setLore(lore).build());
        lore.clear();

    }
}
