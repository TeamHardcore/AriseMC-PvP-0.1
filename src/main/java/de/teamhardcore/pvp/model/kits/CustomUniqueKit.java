/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.kits;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class CustomUniqueKit extends UniqueKit {

    public CustomUniqueKit(String name, List<ItemStack> content) {
        super(name, content);
    }

    public void giveKit(Player player) {
        super.giveKit(player);
    }

}
