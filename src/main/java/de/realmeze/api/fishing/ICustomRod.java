package de.realmeze.api.fishing;

import org.bukkit.entity.FishHook;
import org.bukkit.inventory.ItemStack;

public interface ICustomRod {
    boolean isCustom();
    ItemStack getRod();
    int[] parseTime();
    IFishingTime getTime();
}
