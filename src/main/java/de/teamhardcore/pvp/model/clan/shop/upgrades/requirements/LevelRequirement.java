/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public class LevelRequirement implements AbstractRequirement {

    private int needed;

    public LevelRequirement(int needed) {
        this.needed = needed;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.LEVEL;
    }

    @Override
    public long getNeeded() {
        return this.needed;
    }

    @Override
    public boolean isFulfilled(Clan clan, Player player) {
        if (clan.getLevel() >= this.needed)
            return true;
        return false;
    }
}
