/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.CoinFlip;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class CoinFlipManager {
    private final Main plugin;

    private final Set<CoinFlip> activeCoinFlips;
    private final Set<CoinFlip> coinFlips;

    public CoinFlipManager(Main plugin) {
        this.plugin = plugin;

        this.activeCoinFlips = new HashSet<>();
        this.coinFlips = new HashSet<>();
    }

    public void onDisable() {
        this.coinFlips.forEach(this::removeCoinFlip);

        this.activeCoinFlips.forEach(coinFlip -> {
            cancelCoinFlip(coinFlip);
            coinFlip.getEntries().forEach(player -> player.sendMessage(StringDefaults.PREFIX + "§cDer Coinflip wurde beendet."));
        });
    }

    public void addCoinFlip(Player player, long entryPrice) {
        if (getOpenCoinFlip(player) != null) return;

        CoinFlip coinFlip = new CoinFlip(entryPrice, player);
        UserMoney money = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserMoney();
        money.removeMoney(entryPrice);
        this.coinFlips.add(coinFlip);
        //todo: add inventory
    }

    public void startCoinFlip(CoinFlip coinFlip, Player secondPlayer) {
        if (coinFlip.getEntries().size() > 1 || !this.coinFlips.contains(coinFlip)) return;
        //todo: update inventory
        this.coinFlips.remove(coinFlip);
        UserMoney money = this.plugin.getUserManager().getUser(secondPlayer.getUniqueId()).getUserMoney();
        money.removeMoney(coinFlip.getEntryPrice());
        this.activeCoinFlips.add(coinFlip);
        coinFlip.startCoinFlip(secondPlayer);
    }

    public CoinFlip getOpenCoinFlip(Player player) {
        for (CoinFlip coinflip : this.coinFlips)
            if (!coinflip.getEntries().isEmpty() && coinflip.getEntries().get(0).equals(player))
                return coinflip;

        return null;
    }

    public void cancelCoinFlip(CoinFlip coinFlip) {
        if (coinFlip.getGameTask() != null) return;
        this.activeCoinFlips.remove(coinFlip);
        coinFlip.cancelTask();
        for (Player players : coinFlip.getEntries()) {
            UserMoney money = this.plugin.getUserManager().getUser(players.getUniqueId()).getUserMoney();
            money.addMoney(coinFlip.getEntryPrice());
        }
    }

    public void removeCoinFlip(CoinFlip coinFlip) {
        if (!this.coinFlips.contains(coinFlip))
            return;

        // update(false, coinFlip);
        this.coinFlips.remove(coinFlip);
        UserMoney money = this.plugin.getUserManager().getUser(coinFlip.getEntries().get(0).getUniqueId()).getUserMoney();
        money.addMoney(coinFlip.getEntryPrice());
    }

    public Set<CoinFlip> getActiveCoinFlips() {
        return activeCoinFlips;
    }

    public Set<CoinFlip> getCoinFlips() {
        return coinFlips;
    }

    public Main getPlugin() {
        return plugin;
    }
}
