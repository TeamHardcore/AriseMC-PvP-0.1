/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.inventory;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Report;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.Sound;
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


    }
}
