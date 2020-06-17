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
import de.teamhardcore.pvp.utils.SkullCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DivineCrate extends CrateAddon {

    public DivineCrate() {
        super("DivineCrate", "§a§lDivine Crate", CrateValue.EPIC);
        setDisplayItem(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJkZDdkODE4Y2JhNjUyYjAxY2YzNTBkMmIyYTFjZWVkZDRmNDZhY2FkMDViMmNlODFjM2Y4NzdlYWI3MTcifX19"));

        addContent(new ContentPiece(ContentValue.LEGENDARY, 5, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§9§lSchutz IV Rüstung").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast eine Schutz IV Rüstung gewonnen!");
            }
        });
        addContent(new ContentPiece(ContentValue.LEGENDARY, 1, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§9§l3x Schutz IV Rüstung").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast eine 3x eine Schutz IV Rüstung gewonnen!");
            }
        });

        addContent(new ContentPiece(ContentValue.MYSTIC, 120, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l1 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });
        addContent(new ContentPiece(ContentValue.MYSTIC, 100, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l2 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });
        addContent(new ContentPiece(ContentValue.MYSTIC, 70, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l4 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });
        addContent(new ContentPiece(ContentValue.MYSTIC, 50, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l6 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });
        addContent(new ContentPiece(ContentValue.MYSTIC, 30, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l16 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });
        addContent(new ContentPiece(ContentValue.MYSTIC, 15, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l32 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });
        addContent(new ContentPiece(ContentValue.MYSTIC, 4, new ItemBuilder(Material.MOB_SPAWNER).setDisplayName("§9§l64 Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast 5 Spawner erhalten!");
            }
        });

        addContent(new ContentPiece(ContentValue.RARE, 120, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§9§l2 OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen OP-APFEL gewonnen");
            }
        });
        addContent(new ContentPiece(ContentValue.RARE, 80, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§9§l4 OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen OP-APFEL gewonnen");
            }
        });
        addContent(new ContentPiece(ContentValue.RARE, 50, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§9§l6 OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen OP-APFEL gewonnen");
            }
        });
        addContent(new ContentPiece(ContentValue.RARE, 20, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§9§l12 OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen OP-APFEL gewonnen");
            }
        });
        addContent(new ContentPiece(ContentValue.RARE, 10, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§9§l24 OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen OP-APFEL gewonnen");
            }
        });
        addContent(new ContentPiece(ContentValue.RARE, 5, new ItemBuilder(Material.GOLDEN_APPLE).setDisplayName("§9§l84 OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                player.sendMessage("Du hast einen OP-APFEL gewonnen");
            }
        });

        addContent(new ContentPiece(ContentValue.COMMON, 150, new ItemBuilder(Material.EMERALD).setDisplayName("§9§l4 Emeralds").build(), false) {
            @Override
            public void onWin(Player player) {

            }
        });
        addContent(new ContentPiece(ContentValue.COMMON, 20, new ItemBuilder(Material.EMERALD).setDisplayName("§9§l8 Emeralds").build(), false) {
            @Override
            public void onWin(Player player) {

            }
        });
        addContent(new ContentPiece(ContentValue.COMMON, 30, new ItemBuilder(Material.EMERALD).setDisplayName("§9§l16 Emeralds").build(), false) {
            @Override
            public void onWin(Player player) {

            }
        });
        addContent(new ContentPiece(ContentValue.COMMON, 100, new ItemBuilder(Material.EMERALD).setDisplayName("§9§l32 Emeralds").build(), false) {
            @Override
            public void onWin(Player player) {

            }
        });
        addContent(new ContentPiece(ContentValue.COMMON, 5, new ItemBuilder(Material.EMERALD).setDisplayName("§9§l64 Emeralds").build(), false) {
            @Override
            public void onWin(Player player) {

            }
        });
        addContent(new ContentPiece(ContentValue.COMMON, 2, new ItemBuilder(Material.EMERALD).setDisplayName("§9§l128 Emeralds").build(), false) {
            @Override
            public void onWin(Player player) {

            }
        });


    }
}
