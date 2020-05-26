/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.*;

public class MarketItem {

    private final HashMap<UUID, Long> bets = new HashMap<>();

    private final ItemStack original;
    private final long price;
    private final long time;

    private final String id;
    private final UUID owner;

    private ItemStack auctionItem;
    private ItemStack profileItem;

    private boolean leaked;

    public MarketItem(ItemStack itemStack, UUID owner, String id, long price, long time) {
        this.original = itemStack;
        this.price = price;
        this.time = time + System.currentTimeMillis();

        this.leaked = false;

        this.id = id;
        this.owner = owner;
        update();
    }

    public void addBet(UUID uuid, long bet) {
        long current = this.bets.containsKey(uuid) ? this.bets.get(uuid) : 0;
        long newBet = current + bet;

        this.bets.put(uuid, newBet);
    }

    public HashMap<UUID, Long> getBets() {
        return bets;
    }

    public ItemStack getOriginal() {
        return original;
    }

    public long getPrice() {
        return price;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public ItemStack getAuctionItem() {
        return auctionItem;
    }

    public ItemStack getProfileItem() {
        return profileItem;
    }

    public boolean isLeaked() {
        return leaked;
    }

    public void setLeaked(boolean leaked) {
        this.leaked = leaked;
    }

    public void update() {
        List<String> auctionLore = this.original.hasItemMeta() ? this.original.getItemMeta().hasLore() ? this.original.getItemMeta().getLore() : new ArrayList<>() : new ArrayList<>();

        auctionLore.add(" ");
        auctionLore.add("§c§lPreis§8: §7" + NumberFormat.getInstance(Locale.GERMAN).format(this.price) + "$");
        auctionLore.add("§c§lVon§8: §7" + Bukkit.getOfflinePlayer(this.owner).getName());
        auctionLore.add("§c§lVerbleibende Zeit§8: §7" + TimeUtil.timeToString((this.time - System.currentTimeMillis())));
        auctionLore.add(" ");
        auctionLore.add("§eKlicke§7, §eum das Item zu kaufen.");

        this.auctionItem = new ItemBuilder(this.original.clone()).setLore(auctionLore).build();
        NBTItem aItem = new NBTItem(this.auctionItem);
        aItem.setString("marketId", this.id);
        aItem.setString("marketOwner", this.owner.toString());
        this.auctionItem = aItem.getItem();


        List<String> profileLore = this.original.hasItemMeta() ? this.original.getItemMeta().hasLore() ? this.original.getItemMeta().getLore() : new ArrayList<>() : new ArrayList<>();
        profileLore.add(" ");
        profileLore.add("§c§lPreis§8: §7" + NumberFormat.getInstance(Locale.GERMAN).format(this.price) + "$");
        profileLore.add("§c§lVerbleibende Zeit§8: §7" + (isLeaked() ? "Ausgelaufen" : TimeUtil.timeToString((this.time - System.currentTimeMillis()))));
        profileLore.add(" ");
        if (isLeaked()) {
            profileLore.add("§eRechtsklicke§7, §eum die Auktion zu verlängern.");
            profileLore.add("§eLinksklicke§7, §eum die Auktion zu entfernen.");
        } else {
            profileLore.add("§eKlicke§7, §eum die Auktion zu stoppen.");
        }


        this.profileItem = new ItemBuilder(this.original.clone()).setLore(profileLore).build();
        NBTItem pItem = new NBTItem(this.profileItem);
        pItem.setString("marketId", this.id);
        pItem.setString("marketOwner", this.owner.toString());
        this.profileItem = pItem.getItem();

    }

}
