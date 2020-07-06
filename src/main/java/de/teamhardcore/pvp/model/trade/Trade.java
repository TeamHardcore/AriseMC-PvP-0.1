/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.trade;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trade {

    private final Map<Player, TradeOffer> players;
    private final Map<Player, Inventory> inventories;
    private final List<Player> inAnvil;

    public Trade(Player player, Player target) {
        this.players = new HashMap<>();
        this.inventories = new HashMap<>();
        this.inAnvil = new ArrayList<>();

        this.players.put(player, new TradeOffer(player, this));
        this.players.put(target, new TradeOffer(target, this));
        this.inventories.put(player, createTradeInventory(player));
        this.inventories.put(target, createTradeInventory(target));

        updateTradeInventory(player);
        updateTradeInventory(target);
    }

    public void updateTradeInventory(Player player) {
        if (!this.inventories.containsKey(player)) return;
        Inventory ownInventory = getTradeInv(player);
        Inventory targetInventory = getTradeInv(getOpposite(player));

        for (int slot : TradeDefaults.OWN_SLOTS)
            ownInventory.setItem(slot, new ItemBuilder(Material.AIR).build());

        for (int slot : TradeDefaults.TARGET_SLOTS)
            targetInventory.setItem(slot, new ItemBuilder(Material.AIR).build());

        TradeOffer offer = getTradeOffer(player);

        for (int i = 0; i < offer.getOfferedItems().size(); i++) {
            ownInventory.setItem(TradeDefaults.OWN_SLOTS.get(i), offer.getOfferedItems().get(i));
            targetInventory.setItem(TradeDefaults.TARGET_SLOTS.get(i), offer.getOfferedItems().get(i));
        }

        if (offer.getOfferedMoney() <= 0L) {
            ownInventory.setItem(0, TradeDefaults.ADD_MONEY);
            targetInventory.setItem(8, TradeDefaults.NO_MONEY_ADD);
        } else {
            ownInventory.setItem(0, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§lHinzugefügtes Geld§8: §7" + Util.formatNumber(offer.getOfferedMoney()) + "$")
                    .setLore("", "§eLinsklicke§7, §eum den Betrag zu ändern.", "§eRechtsklicke§7, §eum den Betrag zu löschen.").addItemFlags(ItemFlag.HIDE_ENCHANTS).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
            targetInventory.setItem(8, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§lHinzugefügtes Geld§8: §7" + Util.formatNumber(offer.getOfferedMoney()) + "$").addItemFlags(ItemFlag.HIDE_ENCHANTS).addEnchantment(Enchantment.ARROW_DAMAGE, 1).build());
        }

        if (!offer.isReady()) {
            ownInventory.setItem(45, TradeDefaults.CONFIRM);
            ownInventory.setItem(46, TradeDefaults.CONFIRM);
            ownInventory.setItem(49, TradeDefaults.CANCEL);
            targetInventory.setItem(52, TradeDefaults.NOT_CONFIRMED);
            targetInventory.setItem(53, TradeDefaults.NOT_CONFIRMED);
        } else {
            ownInventory.setItem(45, TradeDefaults.NOT_CONFIRM);
            ownInventory.setItem(46, TradeDefaults.NOT_CONFIRM);
            ownInventory.setItem(49, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());
            targetInventory.setItem(52, TradeDefaults.CONFIRMED);
            targetInventory.setItem(53, TradeDefaults.CONFIRMED);
        }

    }

    public void unreadyForChanges(Player player) {
        Player opposite = getOpposite(player);
        TradeOffer playerOffer = getTradeOffer(player);
        TradeOffer oppositeOffer = getTradeOffer(opposite);

        if (oppositeOffer.isReady()) {
            oppositeOffer.setReady(false);
            opposite.sendMessage(StringDefaults.TRADE_PREFIX + "§cDas Angebot hat sich von §6" + player.getName() + " §cgeändert.");
        }

        if (playerOffer.isReady()) {
            playerOffer.setReady(false);
            player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDein Angebot hat sich geändert.");
        }

    }

    public void checkTradeStatus(Player who) {
        Player target = getOpposite(who);
        TradeOffer whoOffer = getTradeOffer(who);
        TradeOffer targetOffer = getTradeOffer(target);

        if (whoOffer.isReady() && targetOffer.isReady()) {

            if (whoOffer.getOfferedMoney() > 0L) {
                UserMoney userMoney = Main.getInstance().getUserManager().getUser(who.getUniqueId()).getUserMoney();
                if (userMoney.getMoney() < whoOffer.getOfferedMoney()) {
                    whoOffer.setReady(false);
                    targetOffer.setReady(false);
                    who.sendMessage("§c§lTRADE §8§l►§7§l► §r§cDein hinzugefügtes Geld wurde zurückgesetzt, da sich dein Kontostand geändert hat.");
                    target.sendMessage("§c§lTRADE §8§l►§7§l► §r§cDas hinzugefügte Geld von §6" + who.getName() + " §cwurde zurückgesetzt, da sich sein Kontostand geändert hat.");

                    return;
                }
            }
            if (targetOffer.getOfferedMoney() > 0L) {
                UserMoney userMoney = Main.getInstance().getUserManager().getUser(target.getUniqueId()).getUserMoney();
                if (userMoney.getMoney() < targetOffer.getOfferedMoney()) {
                    targetOffer.setReady(false);
                    whoOffer.setReady(false);
                    target.sendMessage(StringDefaults.TRADE_PREFIX + "§cDein hinzugefügtes Geld wurde zurückgesetzt, da sich dein Kontostand geändert hat.");
                    who.sendMessage(StringDefaults.TRADE_PREFIX + "§cDas hinzugefügte Geld von §7" + who.getName() + " §cwurde zurückgesetzt, da sich sein Kontostand geändert hat.");
                    return;
                }
            }

            for (Player all : getPlayers().keySet()) {
                Player opposite = getOpposite(all);
                TradeOffer offer = getTradeOffer(opposite);

                if (offer.getOfferedMoney() > 0L) {
                    UserMoney userMoneyAll = Main.getInstance().getUserManager().getUser(all.getUniqueId()).getUserMoney();
                    UserMoney userMoneyOpposite = Main.getInstance().getUserManager().getUser(opposite.getUniqueId()).getUserMoney();

                    userMoneyAll.addMoney(offer.getOfferedMoney());
                    userMoneyOpposite.removeMoney(offer.getOfferedMoney());
                }

                if (offer.getOfferedItems().size() > 0) {
                    for (ItemStack item : offer.getOfferedItems()) {
                        if (item == null || item.getType() == Material.AIR)
                            continue;
                        Util.addItem(all, item);
                    }
                }

                Main.getInstance().getTradeManager().getPlayerTrades().remove(all);
                all.closeInventory();
                all.updateInventory();
                all.sendMessage(StringDefaults.TRADE_PREFIX + "§eDer Handel war erfolgreich.");
                all.playSound(all.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);
            }

            Main.getInstance().getTradeManager().getTrades().remove(this);
        }
    }

    public Player getOpposite(Player player) {
        for (Player entry : this.players.keySet())
            if (entry != player) return entry;
        return null;
    }

    public TradeOffer getTradeOffer(Player player) {
        if (!this.players.containsKey(player))
            return null;
        return this.players.get(player);
    }

    public Inventory getTradeInv(Player player) {
        if (!this.inventories.containsKey(player))
            return null;
        return this.inventories.get(player);
    }

    private Inventory createTradeInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lHandel mit §7" + getOpposite(player).getName());

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

        List<Integer> clearSlots = new ArrayList<Integer>() {{
            addAll(TradeDefaults.OWN_SLOTS);
            addAll(TradeDefaults.TARGET_SLOTS);
        }};

        for (int slot : clearSlots)
            inventory.setItem(slot, new ItemBuilder(Material.AIR).build());

        inventory.setItem(49, TradeDefaults.CANCEL);
        player.openInventory(inventory);
        return inventory;
    }

    public Map<Player, TradeOffer> getPlayers() {
        return players;
    }

    public Map<Player, Inventory> getInventories() {
        return inventories;
    }

    public List<Player> getInAnvil() {
        return inAnvil;
    }
}
