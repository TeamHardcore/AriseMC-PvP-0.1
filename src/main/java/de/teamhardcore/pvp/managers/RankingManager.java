/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.EnumLeague;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class RankingManager {

    private final Main plugin;


    private final ArrayList<AbstractMap.SimpleEntry<UUID, Long>> pvpRanking = new ArrayList<>();
    private final ArrayList<AbstractMap.SimpleEntry<UUID, Long>> trophyRanking = new ArrayList<>();
    private final ArrayList<AbstractMap.SimpleEntry<UUID, Long>> moneyRanking = new ArrayList<>();
    private final ArrayList<AbstractMap.SimpleEntry<UUID, Long>> playtimeRanking = new ArrayList<>();
    private final ArrayList<Clan> clanRanking = new ArrayList<>();

    public static ItemStack notOccupiedItem;

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public RankingManager(Main plugin) {
        this.plugin = plugin;

        notOccupiedItem = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lNicht vergeben").build();
        startRankingUpdater();
    }

    private void startRankingUpdater() {
        service.scheduleAtFixedRate(() -> {
            this.pvpRanking.clear();
            this.moneyRanking.clear();

        }, 1L, 30L, TimeUnit.SECONDS);
    }

    public void openRankingInventory(Player player, RankingType type) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lRanking");
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        inventory.setItem(1, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§a§lKill-Ranking").build());
        inventory.setItem(2, new ItemBuilder(Material.ARMOR_STAND).setDisplayName("§c§lTrophäen-Ranking").build());

        inventory.setItem(4, new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName("§c§lClan-Ranking").build());

        inventory.setItem(6, new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§lGeld-Ranking").build());
        inventory.setItem(7, new ItemBuilder(Material.WATCH).setDisplayName("§f§lSpielzeit-Ranking").build());

        updateRankingInventory(inventory, type);
        player.openInventory(inventory);
    }

    public void updateRankingInventory(Inventory inventory, RankingType type) {
        if (!inventory.getTitle().equals("§c§lRanking")) return;

        for (int i = 11; i < 16; i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        for (int i = 29; i < 42; i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());

        for (int i = 0; i < 8; i++)
            inventory.getItem(i).removeEnchantment(Enchantment.ARROW_DAMAGE);


        int counter = 0;

        if (type == RankingType.PVP) {
            inventory.getItem(1).addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            inventory.getItem(1).getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

            for (AbstractMap.SimpleEntry<UUID, Long> entry : this.pvpRanking) {
                UUID uuid = entry.getKey();
                long kills = entry.getValue();

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

                ItemStack item = new ItemBuilder(Material.SKULL_ITEM).setDurability((short) 3).setDisplayName("§6§l#" + (counter + 1) + " §8§l- §c§l" + offlinePlayer.getName())
                        .setLore(Arrays.asList("", "§7Kills§8: §e" + kills, "", "§eKlicke§7, §eum die Statistiken zu öffnen.")).setSkullOwner(offlinePlayer.getName()).build();

                if (counter >= 5)
                    inventory.setItem(29 + counter + 4, item);
                else inventory.setItem(29 + counter, item);

                counter++;
            }
        }

        if (type == RankingType.TROPHIES) {
            inventory.getItem(2).addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            inventory.getItem(2).getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

            for (AbstractMap.SimpleEntry<UUID, Long> entry : this.trophyRanking) {
                UUID uuid = entry.getKey();
                long trophies = entry.getValue();

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

                ItemStack item = new ItemBuilder(Material.SKULL_ITEM).setDurability((short) 3).setDisplayName("§6§l#" + (counter + 1) + " §7§l● §c§l" + offlinePlayer.getName())
                        .setLore(Arrays.asList("", "§7Liga§8: §e" + EnumLeague.getLeagueByTrophies((int) trophies).getDisplayName(), "§7Pokale§8: §e" + Util.formatNumber(trophies), "", "§eKlicke§7, §eum die Statistiken zu öffnen.")).setSkullOwner(offlinePlayer.getName()).build();

                if (counter >= 5)
                    inventory.setItem(29 + counter + 4, item);
                else inventory.setItem(29 + counter, item);

                counter++;
            }
        }

        if (type == RankingType.CLAN) {
            inventory.getItem(4).addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            inventory.getItem(4).getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

            for (Clan clan : this.clanRanking) {
                Material material = (counter > 2) ? Material.LEATHER_HELMET : ((counter > 1) ? Material.GOLD_HELMET : ((counter > 0) ? Material.IRON_HELMET : Material.DIAMOND_HELMET));
                ItemStack item = new ItemBuilder(material).setDisplayName("§6§l#" + (counter + 1) + " §7§l● §c§l" + clan.getName())
                        .setLore(Arrays.asList("§7Kills: §6" + clan.getKills(), "", "§eKlicke§7, §eum den Clan anzuzeigen.")).build();


                if (counter >= 5)
                    inventory.setItem(29 + counter + 4, item);
                else inventory.setItem(29 + counter, item);

                counter++;
            }
        }

        if (type == RankingType.MONEY) {
            inventory.getItem(2).addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            inventory.getItem(2).getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

            for (AbstractMap.SimpleEntry<UUID, Long> entry : this.moneyRanking) {
                UUID uuid = entry.getKey();
                long money = entry.getValue();

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

                ItemStack item = new ItemBuilder(Material.SKULL_ITEM).setDurability((short) 3).setDisplayName("§6§l#" + (counter + 1) + " §7§l● §c§l" + offlinePlayer.getName())
                        .setLore(Arrays.asList("", "§7Geld§8: §e" + Util.formatNumber(money) + "$", "", "§eKlicke§7, §eum die Statistiken zu öffnen.")).setSkullOwner(offlinePlayer.getName()).build();

                if (counter >= 5)
                    inventory.setItem(29 + counter + 4, item);
                else inventory.setItem(29 + counter, item);

                counter++;
            }
        }

        if (type == RankingType.PLAYTIME) {
            inventory.getItem(2).addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            inventory.getItem(2).getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

            for (AbstractMap.SimpleEntry<UUID, Long> entry : this.playtimeRanking) {
                UUID uuid = entry.getKey();
                long playtime = entry.getValue();

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

                ItemStack item = new ItemBuilder(Material.SKULL_ITEM).setDurability((short) 3).setDisplayName("§6§l#" + (counter + 1) + " §7§l● §c§l" + offlinePlayer.getName())
                        .setLore(Arrays.asList("", "§7Spielzeit§8: §e" + TimeUtil.timeToString(playtime), "", "§eKlicke§7, §eum die Statistiken zu öffnen.")).setSkullOwner(offlinePlayer.getName()).build();

                if (counter >= 5)
                    inventory.setItem(29 + counter + 4, item);
                else inventory.setItem(29 + counter, item);

                counter++;
            }
        }

        if (counter < 10) {
            for (int i = counter; i < 10; i++) {
                if (counter >= 5)
                    inventory.setItem(29 + counter + 4, notOccupiedItem);
                else inventory.setItem(29 + counter, notOccupiedItem);
                counter++;
            }
        }

    }

    public Main getPlugin() {
        return plugin;
    }

    public ArrayList<AbstractMap.SimpleEntry<UUID, Long>> getPvpRanking() {
        return pvpRanking;
    }

    public ArrayList<AbstractMap.SimpleEntry<UUID, Long>> getTrophyRanking() {
        return trophyRanking;
    }

    public ArrayList<AbstractMap.SimpleEntry<UUID, Long>> getMoneyRanking() {
        return moneyRanking;
    }

    public ArrayList<AbstractMap.SimpleEntry<UUID, Long>> getPlaytimeRanking() {
        return playtimeRanking;
    }

    public List<Clan> getClanRanking() {
        return clanRanking;
    }

    public ScheduledExecutorService getService() {
        return service;
    }

    public enum RankingType {
        PVP, TROPHIES, MONEY, PLAYTIME, CLAN;
    }

}
