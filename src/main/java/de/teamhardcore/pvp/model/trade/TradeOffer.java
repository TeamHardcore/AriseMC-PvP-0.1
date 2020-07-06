/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.trade;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TradeOffer {

    private final Player player;
    private final Trade trade;
    private boolean ready;
    private long offeredMoney;
    private List<ItemStack> offeredItems;

    public TradeOffer(Player player, Trade trade) {
        this.player = player;
        this.trade = trade;
        this.ready = false;
        this.offeredMoney = 0L;
        this.offeredItems = new ArrayList<>();
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        this.trade.updateTradeInventory(this.player);
        this.trade.checkTradeStatus(this.player);
    }

    public void setOfferedMoney(long offeredMoney) {
        this.offeredMoney = offeredMoney;
        this.trade.updateTradeInventory(this.player);
        this.trade.unreadyForChanges(this.player);
    }

    public void setOfferedItems(List<ItemStack> offeredItems) {
        this.offeredItems = offeredItems;
        this.trade.updateTradeInventory(this.player);
        this.trade.unreadyForChanges(this.player);
    }

    public void addOfferedItem(ItemStack itemStack) {
        if (this.offeredItems.size() >= 12) return;
        this.offeredItems.add(itemStack);
        this.trade.updateTradeInventory(this.player);
        this.trade.unreadyForChanges(this.player);
    }

    public void removeOfferedItem(int index) {
        if (this.offeredItems.get(index) == null) return;
        this.offeredItems.remove(index);
        this.trade.updateTradeInventory(this.player);
        this.trade.unreadyForChanges(this.player);
    }

    public Player getPlayer() {
        return player;
    }

    public Trade getTrade() {
        return trade;
    }

    public boolean isReady() {
        return ready;
    }

    public long getOfferedMoney() {
        return offeredMoney;
    }

    public List<ItemStack> getOfferedItems() {
        return offeredItems;
    }
}
