/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Transaction;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TransactionManager implements Listener {

    private final Main plugin;

    private final HashMap<Player, Transaction> transactions;

    public TransactionManager(Main plugin) {
        this.plugin = plugin;

        this.transactions = new HashMap<>();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void createTransaction(Player player, Transaction transaction) {
        if (this.transactions.containsKey(player)) return;

        this.transactions.put(player, transaction);
    }

    public Transaction getTransaction(Player player) {
        if (!this.transactions.containsKey(player)) return null;
        return this.transactions.get(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();

        if (inventory.getTitle().equalsIgnoreCase("§7§lKaufbestätigung")) {
            event.setCancelled(true);

            Transaction transaction = getTransaction(player);

            if (transaction == null) {
                event.getView().close();
                player.sendMessage(StringDefaults.PREFIX + "§cEs läuft aktuell kein Kauf.");
                return;
            }

            if (slot >= 9 && slot <= 11) {
                User user = this.plugin.getUserManager().getUser(player.getUniqueId());
                UserMoney userMoney = user.getUserMoney();

                if (userMoney.getMoney() < transaction.getPrice()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu besitzt zu wenige Münzen.");
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                    return;
                }

                this.transactions.remove(player);

                boolean success = transaction.onBuy();
                if (success) {
                    userMoney.removeMoney(transaction.getPrice());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                    player.sendMessage(StringDefaults.PREFIX + "§eDer Kauf war erfolgreich.");
                } else
                    this.transactions.put(player, transaction);
            }

            if (slot >= 15 && slot <= 17) {
                this.transactions.remove(player);
                boolean success = transaction.onCancel();

                if (success) {
                    player.playSound(player.getLocation(), Sound.BLAZE_DEATH, 1.0F, 1.0F);
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Kauf wurde abgebrochen.");
                } else this.transactions.put(player, transaction);
            }

        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        if (inventory.getTitle().equalsIgnoreCase("§6§lKaufbestätigung")) {
            Transaction transaction = getTransaction(player);

            if (transaction == null) return;
            this.transactions.remove(player);
            transaction.onCancel();
        }
    }

}
