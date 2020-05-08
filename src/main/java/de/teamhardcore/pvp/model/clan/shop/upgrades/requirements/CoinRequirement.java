/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import org.bukkit.entity.Player;

public class CoinRequirement implements AbstractRequirement {

    private long needed;

    public CoinRequirement(long needed) {
        this.needed = needed;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.COINS;
    }

    @Override
    public long getNeeded() {
        return this.needed;
    }

    @Override
    public boolean isFulfilled(Clan clan, Player player) {
        if (clan.getMoney() >= this.needed)
            return true;
        return false;
    }
}
