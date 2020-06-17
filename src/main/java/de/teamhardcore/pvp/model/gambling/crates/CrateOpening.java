/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentPiece;
import de.teamhardcore.pvp.model.gambling.crates.utils.ContentSortingComparator;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CrateOpening {

    private final List<ContentPiece> rewards = new ArrayList<>();
    private final int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};
    private final Player player;
    private final BaseCrate baseCrate;

    private Inventory inventory;
    private BukkitTask task;

    private long count;

    public CrateOpening(Player player, BaseCrate crate) {
        this.player = player;
        this.baseCrate = crate;
        this.count = 0;
        this.task = null;

        loadInventory();
        loadRewards();

        startOpening();
    }

    private void loadInventory() {
        this.inventory = Bukkit.createInventory(null, 9 * 3, "§c§lCrate öffnen");

        IntStream.range(0, 9).forEach((value -> {
            this.inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
            this.inventory.setItem(value + 18, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        }));

        this.inventory.setItem(4, new ItemBuilder(Material.HOPPER).setDisplayName(" ").build());
    }

    private void loadRewards() {
        for (ContentPiece content : this.baseCrate.getAddon().getCrateContent()) {
            double chance = this.baseCrate.getAddon().getPercentChance(content);
            for (int i = 0; i < chance; i++) {
                this.rewards.add(content);
            }
        }

        rewards.sort(ContentSortingComparator.$);
    }

    public void openInventory(Player player) {
        if (this.inventory == null) loadInventory();
        player.openInventory(this.inventory);
    }

    public void startOpening() {
        this.task = new BukkitRunnable() {

            @Override
            public void run() {
                if (count == 39) {
                    if (CrateOpening.this.task != null)
                        CrateOpening.this.task.cancel();

                    if (CrateOpening.this.player == null || !CrateOpening.this.player.isOnline()) return;

                    if (CrateOpening.this.inventory.getItem(13) == null) {
                        CrateOpening.this.player.closeInventory();
                        return;
                    }

                    ContentPiece piece = CrateOpening.this.rewards.get(CrateOpening.this.getRewards().size() - 5);
                    piece.onWin(CrateOpening.this.player);

                    Main.getInstance().getCrateManager().getPlayersInCrateOpening().remove(CrateOpening.this.getPlayer());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (CrateOpening.this.player.getOpenInventory().getTopInventory().equals(CrateOpening.this.inventory))
                                CrateOpening.this.player.closeInventory();
                        }
                    }.runTaskLater(Main.getInstance(), 40L);
                    return;
                }

                count++;

                IntStream.range(1, slots.length).forEach(value -> {
                    if (CrateOpening.this.inventory.getItem(slots[value]) != null)
                        CrateOpening.this.inventory.setItem((slots[value] - 1), CrateOpening.this.inventory.getItem(slots[value]));
                });

                ContentPiece piece = CrateOpening.this.rewards.get(new Random().nextInt(CrateOpening.this.rewards.size() - 1));
                CrateOpening.this.rewards.add(piece);

                CrateOpening.this.inventory.setItem(17, piece.getDisplayItem());
                CrateOpening.this.player.playSound(CrateOpening.this.player.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);

                long newTime = (CrateOpening.this.count == 20
                        ? 2L
                        : (CrateOpening.this.count == 30
                        ? 4L
                        : (CrateOpening.this.count == 35
                        ? 10L
                        : (CrateOpening.this.count >= 38
                        ? 20L
                        : 0L))));

                if (CrateOpening.this.count == 20 || CrateOpening.this.count == 30 || CrateOpening.this.count == 35
                        || CrateOpening.this.count == 38) {
                    if (CrateOpening.this.task != null) {
                        CrateOpening.this.task.cancel();
                        CrateOpening.this.task = null;
                    }
                    task = Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), this, newTime, newTime);
                }

            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }

    public void cancelTask() {
        this.task.cancel();
        this.task = null;
    }

    public List<ContentPiece> getRewards() {
        return rewards;
    }

    public BukkitTask getTask() {
        return task;
    }

    public long getCount() {
        return count;
    }

    public int[] getSlots() {
        return slots;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public BaseCrate getBaseCrate() {
        return baseCrate;
    }

    public Player getPlayer() {
        return player;
    }
}
