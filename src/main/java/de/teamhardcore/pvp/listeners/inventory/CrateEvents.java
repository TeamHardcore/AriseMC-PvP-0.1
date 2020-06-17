/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.inventory;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.managers.CrateManager;
import de.teamhardcore.pvp.model.gambling.crates.CrateOpening;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CrateEvents implements Listener {
    private final Main plugin;

    public CrateEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();

        int slot = event.getRawSlot();

        if (event.getHotbarButton() != -1) {
            slot = event.getHotbarButton();
            itemStack = event.getView().getBottomInventory().getItem(event.getHotbarButton());
            if (itemStack == null)
                itemStack = event.getCurrentItem();
        }

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        if (inventory.getTitle().equalsIgnoreCase("§c§lCrates")) {
            event.setCancelled(true);

            int crateSlot = convertToCrateSlot(slot);
            if (crateSlot == -1) {
                System.out.println("slot is -1");
                return;
            }

            int page = Integer.parseInt(inventory.getItem(49).getItemMeta().getDisplayName().substring(12));
            int cratePosition = (page - 1) * CrateManager.CRATE_SLOTS.length + crateSlot;

            UserData userData = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData();
            List<BaseCrate> ownedCrates = userData.getOwnedCrates();

            BaseCrate crate = ownedCrates.get(cratePosition);

            if (crate == null) {
                System.out.println("crate is null");
                return;
            }

            if (event.isLeftClick()) {
                userData.removeCrate(cratePosition);
                CrateOpening opening = new CrateOpening(player, crate);
                System.out.println("slot : " + slot);
                System.out.println("position: " + cratePosition);
                System.out.println("crate slot: " + crateSlot);
                System.out.println(crate.getAddon().getName());
                opening.openInventory(player);
                this.plugin.getCrateManager().getPlayersInCrateOpening().put(player, opening);
            }

            if (event.isRightClick()) {
                player.openInventory(crate.getContentInventory());
                player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
            }
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lCrate-Inhalt")) {
            event.setCancelled(true);
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lCrate öffnen")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        if (inventory.getTitle().equalsIgnoreCase("§c§lCrate-Inhalt")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Main.getInstance().getCrateManager().openCrateInventory(player, 1);
                }
            }.runTaskLater(this.plugin, 1L);
        }

        if (inventory.getTitle().equalsIgnoreCase("§c§lCrate öffnen")) {
            if (this.plugin.getCrateManager().getPlayersInCrateOpening().containsKey(player))
                player.sendMessage(StringDefaults.PREFIX + "§cUm wieder in das Crate-Opening zu gelangen, öffne erneut die Kiste!");
            else {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Main.getInstance().getCrateManager().openCrateInventory(player, 1);
                    }
                }.runTaskLater(this.plugin, 1L);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (this.plugin.getCrateManager().getPlayersInCrateOpening().containsKey(player)) {
            CrateOpening opening = this.plugin.getCrateManager().getPlayersInCrateOpening().remove(player);
            opening.cancelTask();
            player.closeInventory();
        }
    }


    private int convertToCrateSlot(int clickedSlot) {
        if (clickedSlot < 19 || clickedSlot > 34) {
            System.out.println("clicked slot ist over 34 or under 19");
            return -1;
        }
    /*    int line = clickedSlot / 9 - 1;
        int lineSlot = clickedSlot - 10 - line * 9;
        if (lineSlot < 0 || lineSlot > 6)
            return -1;
        return lineSlot + line * 6 + line;*/

        //   int line = clickedSlot / 19 - 1;
        int line = clickedSlot / 27;
        int lineSlot = clickedSlot - 19 - line * 9;
        if (lineSlot < 0 || lineSlot > 6) {
            System.out.println("line slot ist bigger than six or lower than 0");
            return -1;
        }
        return lineSlot + line * 6 + line;
    }

}
