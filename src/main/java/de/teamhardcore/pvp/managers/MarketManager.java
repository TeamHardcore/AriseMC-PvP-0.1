/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.MarketItem;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserMarket;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.teamhardcore.pvp.utils.Util;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.*;

public class MarketManager {

    private final Main plugin;
    private final List<String> idCache;

    private final ArrayList<UUID> auctionViewers;
    private final ArrayList<UUID> profileViewers;

    private final ArrayList<Inventory> inventories;
    private final ArrayList<MarketItem> items;

    public MarketManager(Main plugin) {
        this.plugin = plugin;
        this.idCache = new ArrayList<>();

        this.auctionViewers = new ArrayList<>();
        this.profileViewers = new ArrayList<>();
        this.inventories = new ArrayList<>();
        this.items = new ArrayList<>();

        createInventory();
        startTimeCheckTask();
    }

    private void checkTime() {
        for (User user : this.plugin.getUserManager().getUsers().values()) {
            UserMarket userMarket = user.getUserMarket();

            if (userMarket.getItems().isEmpty()) continue;

            for (MarketItem item : userMarket.getItems()) {
                if (item.isLeaked()) continue;

                long diff = item.getTime() - System.currentTimeMillis();

                if (diff / 1000L < 0L) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(item.getOwner());
                    if (offlinePlayer.isOnline()) {
                        offlinePlayer.getPlayer().sendMessage(StringDefaults.PREFIX + "§cEin Angebot von dir ist ausgelaufen.");
                    }
                    item.setLeaked(true);
                    removeItemFromMarket(item);
                }
                item.update();
            }
        }
        updateInventory();
    }

    private void startTimeCheckTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::checkTime, 20L, 20L);
    }

    public void createOffer(UUID uuid, ItemStack itemStack, long price) {
        UserMarket userMarket = this.plugin.getUserManager().getUser(uuid).getUserMarket();

        if (userMarket.getItems().size() >= 5)
            return;

        String randomId = null;
        while (randomId == null || this.idCache.contains(randomId)) randomId = generateRandomId();
        this.idCache.add(randomId);

        MarketItem item = new MarketItem(itemStack, uuid, randomId, price, 1800000L);
        userMarket.addItem(item);
        addItemToMarket(item);
    }

    public void removeOffer(UUID uuid, MarketItem item) {
        UserMarket userMarket = this.plugin.getUserManager().getUser(uuid).getUserMarket();
        userMarket.removeItem(item);
        removeItemFromMarket(item);
    }

    private void createInventory() {
        int page = this.inventories.size() + 1;
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lAuktionshaus §8- Seite " + page);

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

        inventory.setItem(2, new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setDisplayName("§e§lProfil").build());
        inventory.setItem(6, new ItemBuilder(Material.STORAGE_MINECART).setDisplayName("§e§lAlle Auktionen").build());

        for (int line = 0; line < 3; line++)
            for (int column = 0; column < 7; column++) {
                int slot = 19 + line * 9 + column;
                inventory.setItem(slot, new ItemBuilder(Material.AIR).build());
            }
        this.inventories.add(inventory);
    }

    public void openInventory(Player player, int state, int page) {
        if (state == 0) {
            UserMarket userMarket = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMarket();

            Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lAuktionshaus §8- Dein Profil");

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
            }

            inventory.setItem(2, new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setDisplayName("§e§lProfil").build());
            inventory.setItem(6, new ItemBuilder(Material.STORAGE_MINECART).setDisplayName("§e§lAlle Auktionen").build());

            inventory.setItem(22, new ItemBuilder(Material.BOOK).setDisplayName("§c§lStatistiken").setLore("", "§c§lGesamten Einnahmen§8: §7" + Util.formatNumber(userMarket.getTotalSales()) + "$").build());

            for (int i = 0; i < 5; i++)
                inventory.setItem(38 + i, new ItemBuilder(Material.AIR).build());

            for (int i = 0; i < userMarket.getItems().size(); i++) {
                if (i >= 5) break;

                inventory.setItem(38 + i, userMarket.getItems().get(i).getProfileItem());
            }

            this.profileViewers.add(player.getUniqueId());
            player.openInventory(inventory);
            return;
        }

        if (state == 1) {
            if (page <= 0 || page > this.inventories.size())
                return;
            this.auctionViewers.add(player.getUniqueId());
            player.openInventory(this.inventories.get(page - 1));
        }
    }

    public void addItemToMarket(MarketItem item) {
        this.items.add(item);
        this.items.sort(Comparator.comparingLong(MarketItem::getTime));
        //   Collections.reverse(items);
        updateInventory();
    }

    public void removeItemFromMarket(MarketItem item) {
        if (!this.items.contains(item))
            return;
        this.items.remove(item);
        updateInventory();
    }

    public void updateInventory() {
        if ((this.inventories.size() * 28 < this.items.size())) createInventory();

        for (int page = 0; page < this.inventories.size(); page++) {
            Inventory inventory = this.inventories.get(page);

            for (int line = 0; line < 4; line++)
                for (int column = 0; column < 7; column++) {
                    int slot = 10 + line * 9 + column;
                    inventory.setItem(slot, new ItemBuilder(Material.AIR).build());
                }

            int row = 1;
            int column = 0;

            for (int i = (page) * 28; i < (page) * 28 + 28; i++) {
                int slot = column + row * 10 - (row - 1);

                if (this.items.size() <= i) break;
                MarketItem item = this.items.get(i);
                this.inventories.get(page).setItem(slot, item.getAuctionItem());

                if (++column < 7) continue;
                column = 0;
                ++row;
            }
        }

        if ((this.inventories.size() - 1) * 28 >= this.items.size() && this.inventories.size() > 1) {
            int removePage = this.inventories.size();
            Inventory toRemove = this.inventories.get(removePage - 1);
            Inventory toSend = this.inventories.get(removePage - 2);
            List<HumanEntity> viewers = new ArrayList<>(toRemove.getViewers());

            for (HumanEntity viewer : viewers)
                viewer.openInventory(toSend);

            this.inventories.remove(toRemove);
        }

        for (int page = 0; page < this.inventories.size(); page++) {
            Inventory inventory = this.inventories.get(page);

            if (page > 0)
                inventory.setItem(47, new ItemBuilder(Material.ARROW).setDisplayName("§7Zu §c§lSeite " + page).build());
            else
                inventory.setItem(47, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

            if (page < this.inventories.size() - 1)
                inventory.setItem(51, new ItemBuilder(Material.ARROW).setDisplayName("§7Zu §c§lSeite " + (page + 2)).build());
            else
                inventory.setItem(51, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        }
    }

    public boolean existsPage(int page) {
        return (page >= 1 && page <= this.inventories.size());
    }

    private String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public ArrayList<Inventory> getInventories() {
        return inventories;
    }

    public ArrayList<MarketItem> getItems() {
        return items;
    }

    public ArrayList<UUID> getAuctionViewers() {
        return auctionViewers;
    }

    public ArrayList<UUID> getProfileViewers() {
        return profileViewers;
    }

}
