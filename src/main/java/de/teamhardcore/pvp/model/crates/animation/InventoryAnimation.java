/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.animation;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.crates.CrateItem;
import de.teamhardcore.pvp.model.crates.CrateOpening;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class InventoryAnimation extends AbstractAnimation {

    private Inventory inventory;

    private BukkitTask runnable;
    private long count = 0;

    private int[] slots = {9, 10, 11, 12, 13, 14, 15, 16, 17};

    public InventoryAnimation(CrateOpening opening) {
        super(opening);
        loadInventory();
    }

    private void loadInventory() {
        this.inventory = Bukkit.createInventory(null, 9 * 3, "Crate öffnen");

        IntStream.range(0, 9).forEach((value -> {
            this.inventory.setItem(value, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
            this.inventory.setItem(value + 18, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(7).setDisplayName(" ").build());
        }));

        this.inventory.setItem(4, new ItemBuilder(Material.HOPPER).setDisplayName(" ").build());
    }

    @Override
    public void startAnimation() {
        getOpening().getPlayer().openInventory(inventory);

        if (this.runnable != null) {
            this.runnable.cancel();
            this.runnable = null;
        }

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {

                if (count == 39) {
                    getOpening().stopOpening();
                    return;
                }

                count++;

                IntStream.range(1, slots.length).forEach(value -> moveItem(slots[value]));

                CrateItem item = getOpening().getCrateItems().get(new Random().nextInt(getOpening().getCrateItems().size() - 1));
                getOpening().getCrateItems().add(item);

                inventory.setItem(17, item.getItemStack());
                getOpening().getPlayer().playSound(getOpening().getPlayer().getLocation(), Sound.CLICK, 1.0F, 1.0F);

                long newTime = (count == 20 ? 2L : (count == 30 ? 4L : (count == 35 ? 10L : (count >= 38 ? 20L : 0L))));

                if (count == 20 || count == 30 || count == 35 || count == 38) {
                    if (runnable != null) {
                        runnable.cancel();
                        runnable = null;
                    }
                    runnable = Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), this, newTime, newTime);
                }

            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }

    @Override
    public void stopAnimation() {
        if (this.runnable != null)
            this.runnable.cancel();

        if (getOpening().getPlayer() == null || !getOpening().getPlayer().isOnline()) {
            return;
        }

        if (this.inventory.getItem(13) == null) {
            getOpening().getPlayer().closeInventory();
            return;
        }

        CrateItem item = getOpening().getCrateItems().get(getOpening().getCrateItems().size() - 5);
        getOpening().giveReward(item);

        this.runnable = Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (getOpening().getPlayer().getOpenInventory().getTopInventory().equals(this.inventory)) {

                Main.getInstance().getCrateManager().getActiveOpenings().remove(getOpening().getPlayer());

                getOpening().getPlayer().closeInventory();
                getOpening().getPlayer().playSound(getOpening().getPlayer().getLocation(), Sound.CHEST_CLOSE, 1.0F, 1.0F);
            }

            this.runnable.cancel();
            this.runnable = null;

        }, 40L);
    }


    private void moveItem(int slot) {
        if (this.inventory.getItem(slot) != null)
            this.inventory.setItem((slot - 1), this.inventory.getItem(slot));
    }

}
