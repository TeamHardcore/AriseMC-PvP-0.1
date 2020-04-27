/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates;

import de.teamhardcore.pvp.files.FileBase;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCrate extends FileBase {

    private long price = 0;
    private long limitation = 0;
    private boolean buyable = false;

    public AbstractCrate(String name) {
        super(File.separator + "crates", name);

        writeDefaults();
        loadCrateData();
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();

        cfg.addDefault("Buyable", false);
        cfg.addDefault("Price", -1);
        cfg.addDefault("Limitation", -1);

        cfg.options().copyDefaults(true);
        saveConfig();
    }

    private void loadCrateData() {
        FileConfiguration cfg = getConfig();
        this.buyable = cfg.getBoolean("Buyable");
        this.price = cfg.getLong("Price");
        this.limitation = cfg.getLong("Limitation");
    }

    public void saveCrateData() {
        FileConfiguration cfg = getConfig();

        cfg.set("Buyable", this.buyable);
        cfg.set("Limitation", this.limitation);
        cfg.set("Price", this.price);
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setLimitation(long limitation) {
        this.limitation = limitation;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }

    public long getPrice() {
        return price;
    }

    public long getLimitation() {
        return limitation;
    }

    public boolean isBuyable() {
        return buyable;
    }


    public abstract List<CrateItem> getCrateItems();
}
