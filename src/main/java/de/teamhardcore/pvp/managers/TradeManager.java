/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.listeners.custom.TradeEvents;
import de.teamhardcore.pvp.model.trade.Trade;
import de.teamhardcore.pvp.model.trade.TradeOffer;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TradeManager {

    private final Main plugin;

    private final Map<Player, List<UUID>> requests;
    private final Map<Player, Trade> playerTrades;
    private final List<Trade> trades;

    public TradeManager(Main plugin) {
        this.plugin = plugin;
        this.requests = new HashMap<>();
        this.playerTrades = new HashMap<>();
        this.trades = new ArrayList<>();

        this.plugin.getServer().getPluginManager().registerEvents(new TradeEvents(this.plugin), this.plugin);

    }

    public void onDisable() {
        for (Trade trade : this.trades) {
            for (Player all : trade.getPlayers().keySet()) {
                all.sendMessage(StringDefaults.TRADE_PREFIX + "§cDer Handel wurde abgebrochen.");
                all.playSound(all.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
            }
            stopTrade(trade);
        }
    }

    public void createTrade(Player player, Player target) {
        if (this.playerTrades.containsKey(player) || this.playerTrades.containsKey(target)) return;
        player.closeInventory();
        player.updateInventory();
        target.closeInventory();
        target.updateInventory();

        Trade trade = new Trade(player, target);
        this.trades.add(trade);
        this.playerTrades.put(player, trade);
        this.playerTrades.put(target, trade);
    }

    public void stopTrade(Trade trade) {
        if (!this.trades.contains(trade)) return;

        for (Map.Entry<Player, TradeOffer> entry : trade.getPlayers().entrySet()) {
            Player target = entry.getKey();
            TradeOffer offer = entry.getValue();

            for (ItemStack itemStack : offer.getOfferedItems()) {
                if (itemStack == null || itemStack.getType() == Material.AIR) continue;
                Util.addItem(target, itemStack);
            }
            trade.getInAnvil().remove(target);
            this.playerTrades.remove(target);
            target.closeInventory();
            target.updateInventory();
        }
        this.trades.remove(trade);
    }

    public Main getPlugin() {
        return plugin;
    }

    public Map<Player, List<UUID>> getRequests() {
        return requests;
    }

    public Map<Player, Trade> getPlayerTrades() {
        return playerTrades;
    }

    public List<Trade> getTrades() {
        return trades;
    }
}
