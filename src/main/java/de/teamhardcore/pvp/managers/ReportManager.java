/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Report;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class ReportManager {

    private final Main plugin;

    private final Map<Player, Report> reports = new HashMap<>();
    private final Map<Player, Player> reportConfirmation = new HashMap<>();

    public ReportManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean hasReported(Player player, Player target) {
        if (!this.reports.containsKey(target)) return false;

        Report report = getReport(target);

        if (report.getReporter().containsKey(player)) return true;

        return false;
    }

    public void addReport(Player reporter, Player target, Report.ReportReason reason) {
        Report report = this.reports.getOrDefault(target, new Report(target));

        if (report.getReporter().containsKey(reporter.getUniqueId())) return;

        report.addReport(reporter, reason);

        if (report.getReporter().size() >= 3 || reporter.hasPermission("arisemc.report.direct")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == reporter || !onlinePlayer.hasPermission("arisemc.report.notify")) continue;
                onlinePlayer.sendMessage(StringDefaults.REPORT_PREFIX + "§cDer Spieler §7" + target.getName() + " §cwurde von §7" + reporter.getName() + " §creportet.");
                onlinePlayer.sendMessage(StringDefaults.REPORT_PREFIX + "§cGrund§8: §7" + reason.getName());
                if (report.getReporter().size() > 1)
                    onlinePlayer.sendMessage(StringDefaults.REPORT_PREFIX + "§cVon §7" + report.getReporter().size() + " §cweitere" + (report.getReporter().size() == 1 ? "m" : "n") + " Spieler reportet.");
            }
        }


        if (!this.reports.containsKey(target))
            this.reports.put(target, report);
    }

    public void removeReport(Player target, boolean wasPunished) {
        Validate.notNull(target, "Target cannot be null");

        if (!this.reports.containsKey(target)) return;

        Report report = getReport(target);

        if (wasPunished) {
            for (Map.Entry<UUID, HashMap<Report.ReportReason, Long>> entry : report.getReporter().entrySet()) {
                Player player = Bukkit.getPlayer(entry.getKey());

                if (player == null || !player.isOnline()) continue;

                player.sendMessage(StringDefaults.REPORT_PREFIX + "§eDer Spieler §7" + target.getName() + " §ewurde bestraft.");
                player.sendMessage(StringDefaults.REPORT_PREFIX + "§eDanke für deine Unterstützung.");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                //todo: add Money
            }
        }
        this.reports.remove(target);
    }

    public void openReportConfirmation(Player reporter, Player target) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§9§lReporte " + target.getName());

        IntStream.range(0, inventory.getSize()).forEach(value -> inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).build()));

        inventory.setItem(11, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lCheating/Hack-Client").setLore("§7Klicke hier, wenn du den Spieler", "§7für §c§lCheating/Hack-Client §7melden", "§7möchtest.").build());
        inventory.setItem(13, new ItemBuilder(Material.PAPER).setDisplayName("§c§lBeleidigung").setLore("§7Klicke hier, wenn du den Spieler", "§7für §c§lBeleidigung §7melden", "§7möchtest.").build());
        inventory.setItem(15, new ItemBuilder(Material.BOOK).setDisplayName("§c§lSpam").setLore("§7Klicke hier, wenn du den Spieler", "§7für §c§lSpam §7melden", "§7möchtest.").build());

        reporter.openInventory(inventory);
        this.reportConfirmation.put(reporter, target);
    }

    public Report getReport(Player target) {
        if (!this.reports.containsKey(target)) return null;
        return this.reports.get(target);
    }

    public Main getPlugin() {
        return plugin;
    }

    public Map<Player, Report> getReports() {
        return reports;
    }

    public Map<Player, Player> getReportConfirmation() {
        return reportConfirmation;
    }
}
