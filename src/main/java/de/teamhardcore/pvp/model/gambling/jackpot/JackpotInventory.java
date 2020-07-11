/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class JackpotInventory {

    private static final int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};

    private final Jackpot parent;
    private final List<ItemStack> skulls;
    private final List<Player> playersInInventory;

    private final List<Player> players;

    private Inventory inventory;
    private BukkitTask task;

    private int count;

    public JackpotInventory(Jackpot parent) {
        this.parent = parent;
        this.skulls = new ArrayList<>();
        this.playersInInventory = new ArrayList<>();
        this.players = new ArrayList<>();

        this.count = 0;
    }

    public void loadInventory() {
        Inventory inventory;

        if (this.parent.getPhase().getState() == JackpotState.ROLLING) {
            inventory = Bukkit.createInventory(null, 9 * 3, "§c§lJackpot §7(" + this.parent.getId() + ")");
            IntStream.range(0, 9).forEach((value -> {
                inventory.setItem(value,
                        new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
                inventory.setItem(value + 18,
                        new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
            }));
            inventory.setItem(4, new ItemBuilder(Material.HOPPER).setDisplayName(" ").build());
        } else {
            inventory = Bukkit.createInventory(null, 9 * 6, "§c§lJackpot §7(" + this.parent.getId() + ")");
            inventory.setItem(49,
                    new ItemBuilder(Material.HOPPER).setDisplayName("§6§lJackpot Informationen")
                            .setLore("§e§lMax. Teilnahmebetrag§8: §7" + Util.formatNumber(
                                    this.parent.getStatistics().getMaxAmount()) + "$",
                                    "§e§lInsgesamt§8: §7" + Util.formatNumber(
                                            this.parent.getStatistics().getCollectedMoney()) + "$").build());
        }

        this.inventory = inventory;
    }

    public void refreshInventoryChances() {
        if (this.parent.getPhase().getState() == JackpotState.ROLLING) return;
        Map<Player, Double> show = new HashMap<>();

        for (Player player : this.players) {
            JackpotMember member = this.parent.getMemberList().getMember(player.getUniqueId());
            double chance = this.parent.getMemberList().getPercentChange(member);
            show.put(player, chance);
        }
        Util.sortMapByValues(show);

        this.inventory.clear();
        this.inventory.setItem(49,
                new ItemBuilder(Material.HOPPER).setDisplayName("§6§lJackpot Informationen")
                        .setLore("§e§lMax. Teilnahmebetrag§8: §7" + Util.formatNumber(
                                this.parent.getStatistics().getMaxAmount()) + "$",
                                "§e§lInsgesamt§8: §7" + Util.formatNumber(
                                        this.parent.getStatistics().getCollectedMoney()) + "$").build());

        for (Map.Entry<Player, Double> entry : show.entrySet()) {
            JackpotMember member = this.parent.getMemberList().getMember(entry.getKey().getUniqueId());

            ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).setDisplayName(
                    "§e§l" + entry.getKey().getName()).setDurability(3).setSkullOwner(entry.getKey().getName()).setLore(
                    "",
                    "§e§lChance§8: §7" + this.parent.getMemberList().getPercentChange(member) + "%",
                    "§e§lTeilgenommen mit§8: §7" + Util.formatNumber(member.getAmount()) + "$").build();
            this.inventory.addItem(itemStack);
        }

        this.playersInInventory.forEach(Player::updateInventory);
    }

    public void addMemberToInventory(Player player) {
        if (this.players.contains(player)) return;
        this.players.add(player);
        refreshInventoryChances();
    }

    public void removeMemberFromInventory(Player player) {
        if (!this.players.contains(player)) return;
        this.players.remove(player);
        refreshInventoryChances();
    }

    private void loadSkulls() {
        for (JackpotMember member : this.parent.getMemberList().getMembers().values()) {
            double chance = this.parent.getMemberList().getPercentChange(member);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(member.getUuid());
            ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setDisplayName(
                    "§7" + offlinePlayer.getName()).setSkullOwner(offlinePlayer.getName()).setLore(
                    "",
                    "§e§lChance§8: §7" + chance + "%",
                    "§e§lTeilgenommen mit§8: §7" + Util.formatNumber(member.getAmount()) + "$").build();
            for (int i = 0; i < chance; i++) {
                this.skulls.add(itemStack);
            }
        }
    }

    public void startDrawing() {
        loadSkulls();
        loadInventory();

        for (Player player : this.playersInInventory) {
            player.closeInventory();
            player.openInventory(this.inventory);
        }

        this.inventory.setItem(17, this.skulls.get(0));

        this.task = new BukkitRunnable() {

            @Override
            public void run() {

                IntStream.range(1, slots.length).filter(
                        value -> JackpotInventory.this.inventory.getItem(slots[value]) != null).forEach(
                        value -> JackpotInventory.this.inventory.setItem((slots[value] - 1),
                                JackpotInventory.this.inventory.getItem(slots[value])));

                for (Player player : JackpotInventory.this.playersInInventory) {
                    player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);
                    player.updateInventory();
                }

                if (JackpotInventory.this.count == 20 || JackpotInventory.this.count == 30 || JackpotInventory.this.count == 35
                        || JackpotInventory.this.count == 40 || JackpotInventory.this.count == 45 || JackpotInventory.this.count == 50) {
                    long newTime;
                    if (JackpotInventory.this.count == 20)
                        newTime = 2L;
                    else if (JackpotInventory.this.count == 30)
                        newTime = 4L;
                    else if (JackpotInventory.this.count == 35)
                        newTime = 8;
                    else if (JackpotInventory.this.count == 40) {
                        newTime = 12;
                    } else if (JackpotInventory.this.count == 45) {
                        newTime = 15;
                    } else {
                        newTime = 20;
                    }

                    if (JackpotInventory.this.task != null) {
                        JackpotInventory.this.task.cancel();
                        JackpotInventory.this.task = null;
                    }

                    JackpotInventory.this.task = Main.getInstance().getServer().getScheduler().runTaskTimer(
                            Main.getInstance(), this, newTime, newTime);
                }

                if (count == 51) {
                    String winner = ChatColor.stripColor(
                            JackpotInventory.this.inventory.getItem(13).getItemMeta().getDisplayName());

                    OfflinePlayer opWinner = Bukkit.getOfflinePlayer(UUIDFetcher.getUUID(winner));

                    if (opWinner == null) {
                        System.out.println(
                                StringDefaults.JACKPOT_PREFIX + "§cEin Fehler im Jackpot ist aufgetreten, es konnte kein Gewinner ermittelt werden.");
                        return;
                    }

                    User opUser = opWinner.isOnline() ? Main.getInstance().getUserManager().getUser(
                            opWinner.getUniqueId()) : new User(opWinner.getUniqueId());
                    opUser.getUserMoney().addMoney(JackpotInventory.this.parent.getStatistics().getCollectedMoney());

                    Bukkit.broadcastMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lJACKPOT §8§l§m*-*-*-*-*-*-*-*-*");
                    Bukkit.broadcastMessage(
                            StringDefaults.PREFIX + "§7" + opWinner.getName() + " §e§lhat den Jackpot gewonnen!");
                    Bukkit.broadcastMessage(StringDefaults.PREFIX + "§e§lGewinn§8: §7" + Util.formatNumber(
                            JackpotInventory.this.parent.getStatistics().getCollectedMoney()) + "$ §e§lChance§8: §7"
                            + JackpotInventory.this.parent.getMemberList().getPercentChange(
                            JackpotInventory.this.parent.getMemberList().getMember(opWinner.getUniqueId())) + "%");
                    Bukkit.broadcastMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lJACKPOT §8§l§m*-*-*-*-*-*-*-*-*");

                    if (opWinner.isOnline()) {
                        opWinner.getPlayer().sendMessage(
                                StringDefaults.JACKPOT_PREFIX + "§eDu hast den Jackpot in Höhe von §7" + Util.formatNumber(
                                        JackpotInventory.this.parent.getStatistics().getCollectedMoney()) + "$ §egewonnen! §7(§e§l" + JackpotInventory.this.parent.getMemberList().getPercentChange(
                                        JackpotInventory.this.parent.getMemberList().getMember(
                                                opWinner.getUniqueId())) + "%§7)");
                    }

                    JackpotInventory.this.task.cancel();
                    JackpotInventory.this.task = null;
                    JackpotInventory.this.parent.getPhase().stop();
                    Main.getInstance().getJackpotManager().stopJackpot();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (Player player : JackpotInventory.this.playersInInventory) {
                                player.closeInventory();
                            }
                        }
                    }.runTaskLater(Main.getInstance(), 20L);
                    return;
                }

                JackpotInventory.this.count++;
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);

    }

    public void openInventory(Player player) {
        this.playersInInventory.add(player);
        player.openInventory(this.inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Jackpot getParent() {
        return parent;
    }
}
