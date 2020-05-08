/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan;

import de.teamhardcore.pvp.database.TimedDatabaseUpdate;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClanChest extends TimedDatabaseUpdate {

    private Clan clan;

    private Inventory inventory;
    private int availableSlots;

    private boolean successfullyLoaded = true;

    public ClanChest(Clan clan, boolean asyncLoad) {
        super("ClanChest", true);
        this.clan = clan;

        initDefaultChest();
       /* if (asyncLoad)
            loadDataAsync();
        else
            loadData();*/
    }

    public void addSlot(int amount) {
        for (int i = this.availableSlots; i < this.availableSlots + amount && i <= this.inventory.getSize(); i++) {
            this.inventory.setItem(i, new ItemBuilder(Material.AIR).build());
        }

        if (this.availableSlots + amount >= 54)
            this.availableSlots = 54;
        else this.availableSlots += amount;

        setUpdate(true);
    }

    private void initDefaultChest() {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, 9 * 6, "§c§lClan-Chest");
            this.availableSlots = 9;

            for (int i = this.availableSlots; i < this.inventory.getSize(); i++) {
                this.inventory.setItem(i, new ItemBuilder(Material.IRON_FENCE).setDisplayName("§c§lGesperrt").setLore("§7Dein Clan hat diesen Slot", "§7noch nicht freigeschaltet.").build());
            }
        }
    }

    private void loadClanChest(String base64String) {
        try {
            byte[] decoded = Base64Coder.decodeLines(base64String);
            this.inventory = Bukkit.createInventory(null, 9 * 6, "§c§lClan-Chest");

            ByteArrayInputStream bain = new ByteArrayInputStream(decoded);
            BukkitObjectInputStream bin = new BukkitObjectInputStream(bain);

            this.availableSlots = bin.readInt();

            for (int i = 0; i < this.availableSlots; i++) {
                this.inventory.setItem(bin.readInt(), (ItemStack) bin.readObject());
            }

            bin.close();
            bain.close();

            for (int locked = this.availableSlots; locked < this.inventory.getSize(); locked++) {
                this.inventory.setItem(locked, null);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            this.successfullyLoaded = false;
            Bukkit.getLogger().severe("Clanchest vom " + this.clan.getName() + " Clan konnte nicht geladen werden!");
            return;
        }
    }

    private String saveClanChest() {
        initDefaultChest();

        try {
            ByteArrayOutputStream baout = new ByteArrayOutputStream();
            BukkitObjectOutputStream bout = new BukkitObjectOutputStream(baout);

            bout.writeInt(this.availableSlots);

            for (int i = 0; i < this.availableSlots; i++) {
                ItemStack item = this.inventory.getItem(i);
                bout.writeInt(i);
                bout.writeObject(item);
            }

            String encoded = Base64Coder.encodeLines(baout.toByteArray());
            bout.close();
            baout.close();

            return encoded;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.successfullyLoaded = false;
            Bukkit.getLogger().severe("Clanchest vom " + this.clan.getName() + " Clan konnte nicht gespeichert werden!");
            return null;
        }
    }

    private void kickViewers() {
        if (this.inventory != null) {
            List<HumanEntity> entities = new ArrayList<>(this.inventory.getViewers());
            for (HumanEntity entity : entities) {
                entity.closeInventory();
                if (entity instanceof Player) {
                    //todo: remove player from clanchest viewer cache
                }
            }
        }
    }

    private void clearClanChest() {
        for (int i = 0; i < this.availableSlots; i++) {
            this.inventory.setItem(i, new ItemBuilder(Material.AIR).build());
        }
        setUpdate(true);
    }

    public Inventory getClanChest() {
        if (!this.successfullyLoaded)
            return null;
        return this.inventory;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void deleteData() {
        getHandlerGroup().removeHandler(this);
        kickViewers();
    }
}
