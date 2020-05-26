/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

import de.teamhardcore.pvp.database.TimedDatabaseUpdate;
import de.teamhardcore.pvp.managers.MarketManager;
import de.teamhardcore.pvp.model.MarketItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserMarket extends TimedDatabaseUpdate {

    private User user;

    private final ArrayList<MarketItem> items;

    private long totalSales;

    public UserMarket(User user) {
        this(user, true, true);
    }

    public UserMarket(User user, boolean timedUpdate, boolean asyncLoad) {
        super("UserMarket", timedUpdate, 30000L);
        this.user = user;

        this.items = new ArrayList<>();
        this.totalSales = 0L;
    }

    public void addItem(MarketItem item) {
        this.items.add(item);
    }

    public void removeItem(MarketItem item) {
        if (!this.items.contains(item)) return;
        this.items.remove(item);
    }

    public void addTotalSale(long amount) {
        this.totalSales += amount;
    }

    public MarketItem getItem(String id) {
        return this.items.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    public List<MarketItem> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }

    public long getTotalSales() {
        return totalSales;
    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void deleteData() {

    }
}
