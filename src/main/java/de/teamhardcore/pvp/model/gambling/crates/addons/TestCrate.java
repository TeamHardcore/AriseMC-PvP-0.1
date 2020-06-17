/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.addons;

import de.teamhardcore.pvp.model.gambling.crates.CrateValue;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentPiece;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentValue;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestCrate extends CrateAddon {

    public TestCrate() {
        super("TestCrate", "§e§lTest Crate", CrateValue.EPIC);

        addContent(new ContentPiece(ContentValue.COMMON, 100, new ItemBuilder(Material.STONE).setDisplayName("§7Toller Stein").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen Stein erhalten.");
            }
        });

        addContent(new ContentPiece(ContentValue.MYSTIC, 100, new ItemBuilder(Material.SULPHUR).setDisplayName("§cNichts").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Vielleicht hast du ja beim nächtes Mal mehr Glück!");
            }
        });

    }
}
