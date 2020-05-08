/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public class KillRequirement implements AbstractRequirement {

    private int needed;

    public KillRequirement(int needed) {
        this.needed = needed;
    }

    @Override
    public long getNeeded() {
        return needed;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.KILLS;
    }

    @Override
    public boolean isFulfilled(Clan clan, Player player) {
        if (clan.getKills() >= this.needed)
            return true;
        return false;
    }
}