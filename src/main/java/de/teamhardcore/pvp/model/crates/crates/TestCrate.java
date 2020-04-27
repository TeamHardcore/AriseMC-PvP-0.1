/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.crates;

import de.teamhardcore.pvp.model.crates.AbstractCrate;
import de.teamhardcore.pvp.model.crates.CrateItem;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TestCrate extends AbstractCrate {

    private final ArrayList<CrateItem> crateItems = new ArrayList<CrateItem>() {{
        add(new CrateItem(new ItemBuilder(Material.BEDROCK).setDisplayName("§e§l12x Bedrock").setAmount(12).build(), 85, player -> {
            player.getInventory().addItem(new ItemBuilder(Material.BEDROCK).setAmount(12).build());
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast 12x Bedrock erhalten.");
        }));
        add(new CrateItem(new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§l250,000$").setAmount(25).build(), 10, player -> {
            player.getInventory().addItem(new ItemBuilder(Material.BEDROCK).setAmount(12).build());
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7250,000$ §eerhalten.");
        }));
        add(new CrateItem(new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§l500,000").setAmount(50).build(), 5, player -> {
            player.getInventory().addItem(new ItemBuilder(Material.BEDROCK).setAmount(12).build());
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7500,000$ §eerhalten.");
        }));
    }};

    public TestCrate() {
        super("Test");
    }

    @Override
    public List<CrateItem> getCrateItems() {
        return this.crateItems;
    }
}
