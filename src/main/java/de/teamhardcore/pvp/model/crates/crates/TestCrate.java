/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.crates;

import de.teamhardcore.pvp.model.crates.AbstractCrate;
import de.teamhardcore.pvp.model.crates.CrateItem;
import de.teamhardcore.pvp.model.crates.actions.CustomItemStackAction;
import de.teamhardcore.pvp.model.crates.actions.CustomMessageAction;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TestCrate extends AbstractCrate {

    private final ArrayList<CrateItem> crateItems = new ArrayList<CrateItem>() {{
        add(new CrateItem(new ItemBuilder(Material.BEDROCK).setDisplayName("§e§l12x Bedrock").setAmount(12).build(),
                100, new CustomItemStackAction(new ItemStack(Material.BEDROCK)), new CustomMessageAction("§eDu hast das §712x Bedrock §eerhalten.")));
    }};

    public TestCrate() {
        super("Test");
    }

    @Override
    public List<CrateItem> getCrateItems() {
        return this.crateItems;
    }
}
