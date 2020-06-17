/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.content;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ContentPiece implements Cloneable {

    private final ContentValue contentValue;
    private final int chanceWeight;
    private final ItemStack displayItem;
    private final boolean multipleRewardAbility;

    public ContentPiece(ContentValue contentValue, int chanceWeight, ItemStack displayItem, boolean multipleRewardAbility) {
        this.contentValue = contentValue;
        this.chanceWeight = chanceWeight;
        this.displayItem = displayItem;
        this.multipleRewardAbility = multipleRewardAbility;
    }

    public ContentValue getContentValue() {
        return contentValue;
    }

    public int getChanceWeight() {
        return chanceWeight;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public boolean isMultipleRewardAbility() {
        return multipleRewardAbility;
    }

    public abstract void onWin(Player player);
}
