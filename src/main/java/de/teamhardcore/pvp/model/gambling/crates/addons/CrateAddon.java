/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.addons;

import de.teamhardcore.pvp.model.gambling.crates.CrateValue;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentPiece;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrateAddon {

    private List<ContentPiece> crateContent;
    private Map<ContentPiece, Double> cachedChances;

    private final String name;
    private final String displayName;
    private final CrateValue value;

    private ItemStack displayItem;

    public CrateAddon(String name, String displayName, CrateValue value) {
        this.name = name;
        this.displayName = displayName;
        this.value = value;
        this.displayItem = new ItemStack(Material.CHEST);

        this.crateContent = new ArrayList<>();
        this.cachedChances = new HashMap<>();
    }

    private void refreshCrateChances() {
        this.cachedChances.clear();
        for (ContentPiece piece : this.crateContent)
            this.cachedChances.put(piece, getPercentChance(piece));
    }

    public void addContent(ContentPiece piece) {
        if (this.crateContent.contains(piece)) return;
        this.crateContent.add(piece);
        refreshCrateChances();
    }

    public void removeContent(ContentPiece piece) {
        if (!this.crateContent.contains(piece))
            return;
        this.crateContent.remove(piece);
        refreshCrateChances();
    }

    public Double getPercentChance(ContentPiece piece) {
        if (this.cachedChances.containsKey(piece))
            return this.cachedChances.get(piece);
        int fullWeight = 0;
        for (ContentPiece contentPiece : this.crateContent)
            fullWeight += contentPiece.getChanceWeight();
        return (double) piece.getChanceWeight() / fullWeight * 100.0D;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public CrateValue getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ContentPiece> getCrateContent() {
        return crateContent;
    }

    public Map<ContentPiece, Double> getCachedChances() {
        return cachedChances;
    }
}
