/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.custom;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.trade.Trade;
import de.teamhardcore.pvp.model.trade.TradeDefaults;
import de.teamhardcore.pvp.model.trade.TradeOffer;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import de.teamhardcore.pvp.utils.VirtualAnvil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeEvents implements Listener {

    private final Main plugin;

    public TradeEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();

        if (inventory.getTitle().startsWith("§c§lHandel mit")) {
            if (!this.plugin.getTradeManager().getPlayerTrades().containsKey(player)) return;

            event.setCancelled(true);

            if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD)
                return;

            if (itemStack == null || itemStack.getType() == Material.AIR) return;

            Trade trade = this.plugin.getTradeManager().getPlayerTrades().get(player);
            TradeOffer offer = trade.getTradeOffer(player);

            if (event.getRawSlot() >= 0 && event.getRawSlot() <= 53) {
                if (event.getRawSlot() == 0) {
                    if (offer.getOfferedMoney() > 0L && event.isRightClick()) {
                        offer.setOfferedMoney(0);
                        player.playSound(player.getLocation(), Sound.FIZZ, 1.0F, 0.5F);
                        return;
                    }

                    UserMoney userMoney = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserMoney();
                    if (userMoney.getMoney() <= 0L) {
                        player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu besitzt zu wenig Geld.");
                        player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                        return;
                    }

                    trade.getInAnvil().add(player);
                    new VirtualAnvil(player, (offer.getOfferedMoney() > 0L) ? ("" + offer.getOfferedMoney()) : null) {

                        @Override
                        public void onConfirm(String paramString) {
                            int toAdd;

                            if (paramString == null) {
                                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cBitte gebe einen gültigen Betrag an.");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                                return;
                            }

                            paramString = paramString.trim();

                            try {
                                toAdd = Integer.parseInt(paramString);
                                if (toAdd <= 0)
                                    throw new NumberFormatException();
                            } catch (NumberFormatException exe) {
                                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cBitte gebe einen gültigen Betrag an.");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                                return;
                            }

                            UserMoney userMoney = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney();

                            if (toAdd > userMoney.getMoney()) {
                                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu besitzt zu wenig Geld!");
                                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDein derzeitiges Geld§8: §7" + Util.formatNumber(userMoney.getMoney()));
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                                return;
                            }

                            TradeOffer offer = trade.getTradeOffer(player);
                            offer.setOfferedMoney(toAdd);
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                        }

                        @Override
                        public void onCancel() {
                            trade.getInAnvil().remove(player);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.openInventory(Main.getInstance().getTradeManager().getPlayerTrades().get(player).getTradeInv(player));
                                }
                            }.runTaskLater(Main.getInstance(), 1L);
                        }
                    };
                }

                if (event.getRawSlot() == 45 || event.getRawSlot() == 46) {
                    if (!trade.getTradeOffer(trade.getOpposite(player)).isReady())
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 2.0F);
                    offer.setReady(!offer.isReady());
                    return;
                }

                if (event.getRawSlot() == 49 && !offer.isReady()) {
                    Player target = trade.getOpposite(player);
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDer Handel wurde abgebrochen.");
                    target.sendMessage(StringDefaults.TRADE_PREFIX + "§7" + player.getName() + " §chat den Handel abgebrochen.");
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                    target.playSound(target.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                    this.plugin.getTradeManager().stopTrade(trade);
                    return;
                }

                if (TradeDefaults.OWN_SLOTS.contains(event.getRawSlot())) {
                    int index = getOfferIndexBySlot(event.getRawSlot());
                    if (index != -1) {
                        Util.addItem(player, itemStack);
                        offer.removeOfferedItem(index);
                        player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
                    }
                }
            } else if (event.getRawSlot() >= 54) {
                int addedItems = offer.getOfferedItems().size();
                if (addedItems >= TradeDefaults.OWN_SLOTS.size())
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                else {
                    offer.addOfferedItem(itemStack);
                    event.setCurrentItem(new ItemBuilder(Material.AIR).build());
                    player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
                }
            }
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;

        Player player = (Player) event.getPlayer();
        if (this.plugin.getTradeManager().getPlayerTrades().containsKey(player)) {
            Trade trade = this.plugin.getTradeManager().getPlayerTrades().get(player);

            if (trade.getInAnvil().contains(player)) return;

            Player target = trade.getOpposite(player);
            player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDer Handel wurde abgebrochen");
            target.sendMessage(StringDefaults.TRADE_PREFIX + "§7" + player.getName() + " §chat den Handel abgebrochen.");

            player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
            target.playSound(target.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
            this.plugin.getTradeManager().stopTrade(trade);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = (Player) event.getPlayer();
        if (this.plugin.getTradeManager().getPlayerTrades().containsKey(player)) {
            Trade trade = this.plugin.getTradeManager().getPlayerTrades().get(player);
            Player target = trade.getOpposite(player);
            player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDer Handel wurde abgebrochen");
            target.sendMessage(StringDefaults.TRADE_PREFIX + "§7" + player.getName() + " §chat den Handel abgebrochen.");

            player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
            target.playSound(target.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
            this.plugin.getTradeManager().stopTrade(trade);
        }
    }

    private int getOfferIndexBySlot(int slot) {
        for (int i = 0; i < TradeDefaults.OWN_SLOTS.size(); i++) {
            int current = TradeDefaults.OWN_SLOTS.get(i);
            if (current == slot)
                return i;
        }
        return -1;
    }

    public Main getPlugin() {
        return plugin;
    }
}
