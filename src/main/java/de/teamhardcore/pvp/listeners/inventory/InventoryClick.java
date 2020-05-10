/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.inventory;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.ClanShopInventory;
import de.teamhardcore.pvp.inventories.SpawnerInventory;
import de.teamhardcore.pvp.model.Report;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.ClanMember;
import de.teamhardcore.pvp.model.clan.ClanRank;
import de.teamhardcore.pvp.model.clan.shop.upgrades.EnumUpgrade;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.AbstractRequirement;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.RequirementType;
import de.teamhardcore.pvp.model.customspawner.AbstractSpawnerType;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.*;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    private final Main plugin;

    public InventoryClick(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        ItemStack itemStack = event.getCurrentItem();
        if (inventory == null || itemStack == null || itemStack.getType() == Material.AIR) return;

        if (inventory.getTitle().startsWith("§9§lReporte ")) {
            event.setCancelled(true);

            if (!this.plugin.getReportManager().getReportConfirmation().containsKey(player)) {
                player.closeInventory();
                return;
            }

            Player target = this.plugin.getReportManager().getReportConfirmation().get(player);

            if (target == null || !target.isOnline()) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                player.closeInventory();
                return;
            }

            Report.ReportReason reportReason = null;


            if (slot != 11 && slot != 13 && slot != 15)
                return;

            switch (slot) {
                case 11:
                    reportReason = Report.ReportReason.CHEATING;
                    break;
                case 13:
                    reportReason = Report.ReportReason.INSULT;
                    break;
                case 15:
                    reportReason = Report.ReportReason.SPAM;
                    break;
            }

            this.plugin.getReportManager().addReport(player, target, reportReason);
            player.sendMessage(StringDefaults.REPORT_PREFIX + "§eDu hast den Spieler §7" + target.getName() + " §eerfolgreich für den Grund §c§l" + reportReason.getName() + " §egemeldet.");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

            this.plugin.getReportManager().getReportConfirmation().remove(player);
            player.closeInventory();

        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lClan-Shop")) {
            event.setCancelled(true);

            User user = this.plugin.getUserManager().getUser(player.getUniqueId());
            Clan clan = this.plugin.getClanManager().getClan(player.getUniqueId());

            if (clan == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                player.closeInventory();
                return;
            }

            ClanMember member = this.plugin.getClanManager().getMember(player.getUniqueId());

            if (member.getRank() == ClanRank.MEMBER || member.getRank() == ClanRank.TRUSTED) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu musst mindestens §5§lMOD §csein, um den Clan-Shop benutzen zu können.");
                return;
            }

            if (slot == 4) {
                EnumUpgrade upgrade = EnumUpgrade.getNextUpgrade(clan.getLevel(), StringDefaults.LEVEL_UPGRADE);

                if (upgrade == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat bereits das maximale Level erreicht.");
                    return;
                }

                if (!this.plugin.getClanManager().getClanShopManager().canPurchaseUpgrade(clan, player, upgrade.getUpgrade())) {
                    if (upgrade.getUpgrade().hasMoreRequirements()) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat noch nicht alle Anforderungen erfüllt.");
                        return;
                    }


                    for (AbstractRequirement requirement : upgrade.getUpgrade().getRequirements()) {
                        if (requirement.getType() != RequirementType.MONEY) continue;
                        player.sendMessage(StringDefaults.PREFIX + "§cDu benötigst noch §7" + Util.formatNumber((requirement.getNeeded() - user.getMoney())) + "$ §cum dieses Level zu erwerben.");
                    }
                    return;
                }

                this.plugin.getClanManager().getClanShopManager().purchaseUpgrade(clan, player, upgrade.getUpgrade());

                this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> ClanShopInventory.openClanShop(player, clan), 1L);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast erfolgreich ein Level für deinen Clan gekauft.");

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player) continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat ein neues Level gekauft..");
                }

                return;
            }

            if (slot != 20 && slot != 22 && slot != 24) return;

            EnumUpgrade upgrade = null;

            switch (slot) {
                case 20:
                    upgrade = EnumUpgrade.getNextUpgrade(clan.getUpgradeLevel(StringDefaults.SLOT_UPGRADE), StringDefaults.SLOT_UPGRADE);
                    break;
                case 22:
                    upgrade = EnumUpgrade.getNextUpgrade(clan.getUpgradeLevel(StringDefaults.WARP_UPGRADE), StringDefaults.WARP_UPGRADE);
                    break;
                case 24:
                    upgrade = EnumUpgrade.getNextUpgrade(clan.getUpgradeLevel(StringDefaults.CHEST_UPGRADE), StringDefaults.CHEST_UPGRADE);
                    break;
            }


            if (upgrade == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat bereits die maximale Stufe erreicht.");
                return;
            }

            if (!this.plugin.getClanManager().getClanShopManager().canPurchaseUpgrade(clan, player, upgrade.getUpgrade())) {
                if (upgrade.getUpgrade().hasMoreRequirements()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat noch nicht alle Anforderungen erfüllt.");
                    return;
                }

                for (AbstractRequirement requirement : upgrade.getUpgrade().getRequirements()) {
                    if (requirement.getType() != RequirementType.COINS) continue;
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan benötigt noch §7" + Util.formatNumber((requirement.getNeeded() - clan.getMoney())) + " Clan-Coins §cum diese Stufe zu erwerben.");
                }
                return;
            }

            this.plugin.getClanManager().getClanShopManager().purchaseUpgrade(clan, player, upgrade.getUpgrade());

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> ClanShopInventory.openClanShop(player, clan), 1L);
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast erfolgreich ein Upgrade für deinen Clan aufgewertet.");

            for (ClanMember members : clan.getMemberList().getMembers().values()) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player) continue;
                offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat erfolgreich ein Upgrade aufgewertet.");
            }

        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lWähle einen Typ")) {
            event.setCancelled(true);

            if (itemStack.getType() == Material.STAINED_GLASS_PANE) return;

            Location location = this.plugin.getSpawnerManager().getPlayersInSpawnerChoosing().get(player);

            if (location == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cEin Fehler ist aufgetreten, bitte versuche es erneut.");
                player.closeInventory();
                return;
            }

            if (location.getBlock() == null || location.getBlock().getType() != Material.MOB_SPAWNER) {
                player.sendMessage(StringDefaults.PREFIX + "§cEin Fehler ist aufgetreten, bitte versuche es erneut.");
                player.closeInventory();
                return;
            }

            CustomSpawner customSpawner = this.plugin.getSpawnerManager().getCustomSpawner(location);

            if (customSpawner == null || customSpawner.getOwner() != player.getUniqueId() && !player.hasPermission("arisemc.spawner.admin")) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst nur deine eigenen Spawner ändern.");
                return;
            }

            CreatureSpawner spawner = (CreatureSpawner) location.getBlock().getState();
            AbstractSpawnerType type = this.plugin.getSpawnerManager().getSpawnerType(itemStack.getDurability());

            if (type == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cDieser Typ existiert nicht.");
                return;
            }

            if (type.isPremium()) {
                boolean hasUnlocked = type.hasUnlocked(this.plugin.getUserManager().getUser(player.getUniqueId()));

                if (!hasUnlocked) {
                    User user = this.plugin.getUserManager().getUser(player.getUniqueId());

                    if (user.getMoney() < type.getPrice()) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDu besitzt zu wenig Münzen, um diesen Typ freizuschalten.");
                        return;
                    }

                    //todo: unlock type
                    user.removeMoney(type.getPrice());
                    player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Typen §7" + type.getType().name() + " §efreigeschaltet.");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    SpawnerInventory.openInventory(player, customSpawner);
                    return;
                }
            }

            customSpawner.setType(type);
            customSpawner.saveData();

            spawner.setSpawnedType(type.getType());
            spawner.update();
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Typ erfolgreich zu §7" + type.getType().name() + " §egewechselt.");
            player.closeInventory();
        }
    }
}
