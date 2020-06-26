/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.addons;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.crates.CrateValue;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentPiece;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentValue;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.SkullCreator;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FirstTieredCrate extends CrateAddon {

    public FirstTieredCrate() {
        super("FirstTieredCrate", "§e§lTier 1 Crate", CrateValue.COMMON);

        setDisplayItem(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ2ZjE1MjYxMWVhMmZhYjc1NGE3Yjg2MGYxYWE3MGJlM2IzMmRhNjM4ZTlhZTRkZGZhZmQzMThiYmI2N2VhMSJ9fX0="));


        addContent(new ContentPiece(ContentValue.COMMON, 200, new ItemBuilder(Material.BEDROCK).setAmount(5).setDisplayName("§9§l5x Bedrock").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.BEDROCK).setAmount(5).build());
            }
        });

        addContent(new ContentPiece(ContentValue.COMMON, 190, new ItemBuilder(Material.GOLDEN_APPLE).setDurability(1).setAmount(4).setDisplayName("§9§l4x OP Apfel").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.GOLDEN_APPLE).setDurability(1).setAmount(4).build());
            }
        });

        addContent(new ContentPiece(ContentValue.COMMON, 180, new ItemBuilder(Material.EXP_BOTTLE).setAmount(5).setDisplayName("§9§l5x XP Flaschen").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.EXP_BOTTLE).setAmount(5).build());
            }
        });

        addContent(new ContentPiece(ContentValue.COMMON, 160, new ItemBuilder(Material.BEDROCK).setAmount(8).setDisplayName("§9§l8x Bedrock").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.BEDROCK).setAmount(8).build());
            }
        });

        addContent(new ContentPiece(ContentValue.RARE, 100, new ItemBuilder(Material.DOUBLE_PLANT).setAmount(5).setDisplayName("§e§l5.000 Coins").build(), false) {
            @Override
            public void onWin(Player player) {
                Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney().addMoney(5000);
            }
        });

        addContent(new ContentPiece(ContentValue.RARE, 90, new ItemBuilder(SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ2ZjE1MjYxMWVhMmZhYjc1NGE3Yjg2MGYxYWE3MGJlM2IzMmRhNjM4ZTlhZTRkZGZhZmQzMThiYmI2N2VhMSJ9fX0=")).setAmount(2).setDisplayName("§e§l2x Tier 1 Crate").build(), false) {
            @Override
            public void onWin(Player player) {
                for (int i = 0; i < 2; i++)
                    Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData().addCrate(Main.getInstance().getCrateManager().getCrate("FirstTieredCrate"));
            }
        });

        addContent(new ContentPiece(ContentValue.RARE, 85, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(6).setDisplayName("§e§l6x OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.GOLDEN_APPLE).setDurability(1).setAmount(6).build());
            }
        });

        addContent(new ContentPiece(ContentValue.RARE, 80, new ItemBuilder(Material.MOB_SPAWNER).setAmount(3).setDisplayName("§e§l3x Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.MOB_SPAWNER).setAmount(3).build());
            }
        });

        addContent(new ContentPiece(ContentValue.MYSTIC, 50, new ItemBuilder(Material.BEDROCK).setAmount(24).setDisplayName("§5§l24x Bedrock").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.BEDROCK).setAmount(24).build());
            }
        });

        addContent(new ContentPiece(ContentValue.MYSTIC, 40, new ItemBuilder(Material.DOUBLE_PLANT).setAmount(7).setDisplayName("§5§l7.000 Coins").build(), false) {
            @Override
            public void onWin(Player player) {
                Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney().addMoney(5000);
            }
        });


        addContent(new ContentPiece(ContentValue.MYSTIC, 30, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(8).setDisplayName("§5§l8x OP-Äpfel").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.GOLDEN_APPLE).setDurability(1).setAmount(8).build());
            }
        });

        addContent(new ContentPiece(ContentValue.MYSTIC, 15, new ItemBuilder(Material.MOB_SPAWNER).setAmount(5).setDisplayName("§5§l5x Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.MOB_SPAWNER).setAmount(5).build());
            }
        });

        addContent(new ContentPiece(ContentValue.LEGENDARY, 5, new ItemBuilder(Material.DOUBLE_PLANT).setAmount(20).setDisplayName("§c§l20.000 Coins").build(), false) {
            @Override
            public void onWin(Player player) {
                Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney().addMoney(20000);
            }
        });

        addContent(new ContentPiece(ContentValue.LEGENDARY, 3, new ItemBuilder(Material.MOB_SPAWNER).setAmount(20).setDisplayName("§c§l20x Spawner").build(), false) {
            @Override
            public void onWin(Player player) {
                Util.addItem(player, new ItemBuilder(Material.MOB_SPAWNER).setAmount(20).build());
            }
        });
    }
}