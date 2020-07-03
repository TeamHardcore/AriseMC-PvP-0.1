/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class CoinFlip {

    public static final SecureRandom RANDOM = new SecureRandom();

    private final long entryPrice;
    private final List<Player> entries;
    private BukkitTask gameTask;
    private Inventory inventory;

    private ItemStack middle;

    public CoinFlip(long entryPrice, Player firstEntry) {
        this.entries = new ArrayList<>();
        this.entries.add(firstEntry);
        this.entryPrice = entryPrice;

        registerInventory();
    }

    private void registerInventory() {
        this.inventory = Bukkit.createInventory(null, 9 * 6, "§c§lCoinflip");
        this.inventory.setItem(10, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(5).setDisplayName(" ").build());
        this.inventory.setItem(16, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(6).setDisplayName(" ").build());
    }

    public void startCoinFlip(Player secondEntry) {
        if (this.gameTask != null) return;
        this.entries.add(secondEntry);

        this.inventory.setItem(9, new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(this.entries.get(0).getName()).setDisplayName("§a§l" + this.entries.get(0).getName()).build());
        this.inventory.setItem(17, new ItemBuilder(Material.SKULL_ITEM).setSkullOwner(this.entries.get(1).getName()).setDisplayName("§d§l" + this.entries.get(1).getName()).build());

        for (Player entry : this.entries) entry.openInventory(this.inventory);

        Player winner = this.entries.get(RANDOM.nextInt(2));
        long winningPrice = this.entryPrice * 2;
        long winnerDurability = RANDOM.nextInt(2) == 1 ? 5 : 6;

        Bukkit.broadcastMessage("[DEBUG] CF winner: " + winner.getName());

        this.gameTask = new BukkitRunnable() {

            int count = 0;

            @Override
            public void run() {

                int newTime = 0;

                if (this.count == 20) {
                    newTime = 2;
                } else if (this.count == 30) {
                    newTime = 4;
                } else if (this.count == 35) {
                    newTime = 8;
                }

                if (this.count == 20 || this.count == 30 || this.count == 35) {
                    if (CoinFlip.this.gameTask != null) {
                        CoinFlip.this.gameTask.cancel();
                        CoinFlip.this.gameTask = null;
                    }
                    CoinFlip.this.gameTask = Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), this, 0L, newTime);
                }

                if (CoinFlip.this.inventory.getItem(13).getDurability() == winnerDurability && this.count >= 38) {
                    UserMoney userMoney = Main.getInstance().getUserManager().getUser(winner.getUniqueId()).getUserMoney();
                    userMoney.addMoney(winningPrice);

                    for (int i = 0; i < CoinFlip.this.entries.size(); i++) {
                        Player self = CoinFlip.this.entries.get(i);
                        Player opposite = CoinFlip.this.entries.get((i == 0) ? 1 : 0);

                        if (winner == self) {
                            self.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lCoinflip §8§l§m*-*-*-*-*-*-*-*-*");
                            self.sendMessage("");
                            self.sendMessage(StringDefaults.PREFIX + "§a§lDu hast gegen §7§l" + opposite.getName() + " §a§lgewonnen!");
                            self.sendMessage(StringDefaults.PREFIX + "§a§lDein Gewinn: §6§l" + Util.formatNumber(winningPrice) + "$");
                            self.sendMessage("");
                            self.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lCoinflip §8§l§m*-*-*-*-*-*-*-*-*");
                            self.playSound(self.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                        } else {
                            self.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lCoinflip §8§l§m*-*-*-*-*-*-*-*-*");
                            self.sendMessage("");
                            self.sendMessage(" §8§l►§7§l► §c§lDu hast gegen §7§l" + opposite.getName() + " §c§lverloren.");
                            self.sendMessage(" §8§l►§7§l► §c§lDein Verlust: §6§l" + Util.formatNumber(CoinFlip.this.entryPrice) + "$");
                            self.sendMessage("");
                            self.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lCoinflip §8§l§m*-*-*-*-*-*-*-*-*");
                            self.playSound(self.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        }
                    }

                    for (HumanEntity viewer : inventory.getViewers())
                        viewer.closeInventory();

                    cancel();
                    return;
                }

                CoinFlip.this.inventory.setItem(13, CoinFlip.this.middle);
                CoinFlip.this.changeColorItem();
                count++;
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
    }

    public void cancelTask() {
        if (this.gameTask == null)
            return;
        this.gameTask.cancel();
        this.gameTask = null;
    }

    private void changeColorItem() {
        if (this.middle == null)
            this.middle = new ItemBuilder(Material.WOOL).setDurability(5).build();
        if (this.middle.getDurability() == 5)
            this.middle = new ItemBuilder(Material.WOOL).setDurability(6).build();
        else this.middle = new ItemBuilder(Material.WOOL).setDurability(5).build();
    }

    public BukkitTask getGameTask() {
        return gameTask;
    }

    public List<Player> getEntries() {
        return entries;
    }

    public long getEntryPrice() {
        return entryPrice;
    }
}
