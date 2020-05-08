/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades;

import de.teamhardcore.pvp.model.clan.shop.upgrades.actions.ClanChestAction;
import de.teamhardcore.pvp.model.clan.shop.upgrades.actions.LevelAction;
import de.teamhardcore.pvp.model.clan.shop.upgrades.actions.SlotAction;
import de.teamhardcore.pvp.model.clan.shop.upgrades.actions.WarpAction;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.*;

import java.util.ArrayList;
import java.util.List;

public enum EnumUpgrade {

    WARP_1(new WarpUpgrade(new WarpAction(1), 1, new CoinRequirement(450), new LevelRequirement(1))),
    WARP_2(new WarpUpgrade(new WarpAction(2), 2, new CoinRequirement(750), new LevelRequirement(2))),
    WARP_3(new WarpUpgrade(new WarpAction(3), 3, new CoinRequirement(1050), new LevelRequirement(4))),

    LEVEL_1(new LevelUpgrade(new LevelAction(1), 1, new MoneyRequirement(40000), new TrophyRequirement(3000), new KillRequirement(400))),
    LEVEL_2(new LevelUpgrade(new LevelAction(2), 2, new MoneyRequirement(80000), new TrophyRequirement(5000), new KillRequirement(700))),
    LEVEL_3(new LevelUpgrade(new LevelAction(3), 3, new MoneyRequirement(150000), new TrophyRequirement(8000), new KillRequirement(1000))),
    LEVEL_4(new LevelUpgrade(new LevelAction(4), 4, new MoneyRequirement(400000), new TrophyRequirement(10000), new KillRequirement(1500))),

    SLOT_1(new SlotUpgrade(new SlotAction(9), 1, new CoinRequirement(450), new LevelRequirement(1))),
    SLOT_2(new SlotUpgrade(new SlotAction(10), 2, new CoinRequirement(750), new LevelRequirement(2))),
    SLOT_3(new SlotUpgrade(new SlotAction(11), 3, new CoinRequirement(1050), new LevelRequirement(3))),
    SLOT_4(new SlotUpgrade(new SlotAction(12), 4, new CoinRequirement(1550), new LevelRequirement(4))),

    CHEST_1(new ClanChestUpgrade(new ClanChestAction(9), 1, new CoinRequirement(0), new LevelRequirement(1))),
    CHEST_2(new ClanChestUpgrade(new ClanChestAction(9), 2, new CoinRequirement(0))),
    CHEST_3(new ClanChestUpgrade(new ClanChestAction(9), 3, new CoinRequirement(0), new LevelRequirement(3))),
    CHEST_4(new ClanChestUpgrade(new ClanChestAction(9), 4, new CoinRequirement(0))),
    CHEST_5(new ClanChestUpgrade(new ClanChestAction(9), 5, new CoinRequirement(0)));

    private final AbstractUpgrade upgrade;

    EnumUpgrade(AbstractUpgrade upgrade) {
        this.upgrade = upgrade;
    }

    public int getLevel() {
        return upgrade.getLevel();
    }

    public AbstractUpgrade getUpgrade() {
        return upgrade;
    }

    public static int getUpgrades(String identifier) {
        List<EnumUpgrade> upgrades = new ArrayList<>();

        for (EnumUpgrade upgrade : values())
            if (upgrade.name().contains(identifier.toUpperCase()))
                upgrades.add(upgrade);

        return upgrades.size();
    }

    public static EnumUpgrade getNextUpgrade(int current, String identifier) {
        for (EnumUpgrade upgrade : values()) {
            if (!upgrade.name().contains(identifier.toUpperCase())) {
                continue;
            }

            if (upgrade.getLevel() <= current) continue;

            if (upgrade.getLevel() > current) {
                return upgrade;
            }
        }
        return null;
    }

}
