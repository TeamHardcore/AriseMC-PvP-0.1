/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.base;

import de.teamhardcore.pvp.files.FileBase;
import de.teamhardcore.pvp.model.gambling.crates.addons.CrateAddon;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentPiece;
import de.teamhardcore.pvp.model.gambling.crates.utils.ContentSortingComparator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaseCrate extends FileBase {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm 'Uhr'", Locale.GERMAN);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.###");

    private final CrateAddon addon;

    private boolean buyable = false;
    private boolean limited = false;
    private boolean disabled = false;
    private boolean timed = false;

    private int limitation;
    private int amountPurchased;

    private long price;
    private long startTime;
    private long endTime;

    private Inventory contentInventory;

    public BaseCrate(CrateAddon addon) {
        super(File.separator + "crates" + File.separator + "data", addon.getName());
        this.addon = addon;

        writeDefaults();
        loadCrateData();
        loadContentInventory();
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();
        cfg.addDefault("Buying.Enabled", Boolean.FALSE);
        cfg.addDefault("Buying.Price", 0);
        cfg.addDefault("Limitation.Enabled", Boolean.FALSE);
        cfg.addDefault("Limitation.Global", -1);
        cfg.addDefault("Timelimit.Enabled", Boolean.FALSE);
        cfg.addDefault("Timelimit.Start", 0);
        cfg.addDefault("Timelimit.End", 0);
        cfg.addDefault("Disabled", Boolean.FALSE);
        cfg.options().copyDefaults(true);
        saveConfig();
    }

    private void loadCrateData() {
        FileConfiguration cfg = getConfig();
        if (cfg.get("") == null)
            return;
        this.buyable = cfg.getBoolean("Buying.Enabled");
        this.price = cfg.getLong("Buying.Price");
        this.limited = cfg.getBoolean("Limitation.Enabled");
        this.limitation = cfg.getInt("Limitation.Global");
        this.timed = cfg.getBoolean("Timelimit.Enabled");
        this.startTime = cfg.getLong("Timelimit.Start");
        this.endTime = cfg.getLong("Timelimit.End");
        this.disabled = cfg.getBoolean("Disabled");

        if (this.limited) {
            if (this.limitation > -1 && cfg.get("Limitation.AmountPurchased") != null) {
                this.amountPurchased = cfg.getInt("Limitation.AmountPurchased");
            }
        }
    }

    private void loadContentInventory() {
        int lines = Math.max(Math.min((int) Math.ceil(this.addon.getCrateContent().size() / 9.0D), 6), 1);
        this.contentInventory = Bukkit.createInventory(null, lines * 9, "§c§lCrate-Inhalt");

        List<ContentPiece> content = new ArrayList<>(this.addon.getCrateContent());
        content.sort(ContentSortingComparator.$);

        int count = 0;
        for (ContentPiece piece : content) {
            if (count >= this.contentInventory.getSize())
                break;

            ItemStack displayItem = piece.getDisplayItem().clone();
            ItemMeta meta = displayItem.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            lore.add("");
            lore.add("§8● §7Wert§8: " + piece.getContentValue().getDisplayName());
            lore.add("§8● §7Chance§8: §c" + DECIMAL_FORMAT.format(this.addon.getPercentChance(piece)).replace(".", ",") + "%");
            meta.setLore(lore);
            displayItem.setItemMeta(meta);
            this.contentInventory.addItem(displayItem);
            count++;
        }
    }

    private void resetAmountPurchased() {
        this.amountPurchased = 0;
        FileConfiguration cfg = getConfig();
        if (this.limited)
            cfg.set("Limitation.AmountPurchased", 0);
        else
            cfg.set("Limitation.AmountPurchased", null);
        saveConfig();
    }

    public void setBuyable(boolean buyable, long price) {
        this.buyable = buyable;
        this.price = price;
        FileConfiguration cfg = getConfig();
        cfg.addDefault("Buying.Enabled", buyable);
        cfg.addDefault("Buying.Price", price);
        saveConfig();
        //todo: update shop here
    }

    public void setLimited(boolean limited, int limitation) {
        this.limited = limited;
        this.limitation = limitation;

        FileConfiguration cfg = getConfig();
        cfg.addDefault("Limitation.Enabled", limited);
        cfg.addDefault("Limitation.Global", limitation);
        saveConfig();
        resetAmountPurchased();
        //todo: ipdate shop here
    }

    public void setAmountPurchased(int amountPurchased) {
        this.amountPurchased = amountPurchased;

        FileConfiguration cfg = getConfig();
        cfg.set("Limitation.AmountPurchased", amountPurchased);
        saveConfig();
        //todo: update shop here
    }

    public void setTimed(boolean timed, long startTime, long endTime) {
        this.timed = timed;
        this.startTime = startTime;
        this.endTime = endTime;

        FileConfiguration cfg = getConfig();
        cfg.set("Timelimit.Enabled", timed);
        cfg.set("Timelimit.Start", startTime);
        cfg.set("Timelimit.End", endTime);
        saveConfig();
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        FileConfiguration cfg = getConfig();
        cfg.set("Disabled", disabled);
        saveConfig();
    }

    public boolean isInTime() {
        if (!this.timed)
            return true;

        long timestamp = System.currentTimeMillis();
        return (this.startTime <= timestamp || this.endTime >= timestamp);
    }

    public boolean isBuyable() {
        return buyable;
    }

    public boolean isLimited() {
        return limited;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isTimed() {
        return timed;
    }

    public int getLimitation() {
        return limitation;
    }

    public int getAmountPurchased() {
        return amountPurchased;
    }

    public long getPrice() {
        return price;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public ItemStack getMenuItem() {
        ItemStack itemStack = this.addon.getDisplayItem().clone();
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(this.addon.getDisplayName());

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§8● §7Wert§8: " + this.addon.getValue().getDisplayName());
        lore.add(" ");
        lore.add("§eLinksklicke§8, §eum die Crate zu öffnen");
        lore.add("§eRechtsklicke§8, §eum den Inhalt anzusehen");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Inventory getContentInventory() {
        return contentInventory;
    }

    public CrateAddon getAddon() {
        return addon;
    }
}
