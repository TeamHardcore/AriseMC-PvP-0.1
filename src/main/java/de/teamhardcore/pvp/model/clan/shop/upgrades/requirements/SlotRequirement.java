/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public class SlotRequirement implements AbstractRequirement {

    private int needed;

    public SlotRequirement(int needed) {
        this.needed = needed;
    }

    @Override
    public long getNeeded() {
        return needed;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.SLOTS;
    }

    @Override
    public boolean isFulfilled(Clan clan, Player player) {
        if (clan.getMemberList().getMembers().size() >= this.needed)
            return true;
        return false;
    }
}
