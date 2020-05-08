/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.user.User;
import org.bukkit.entity.Player;

public class MoneyRequirement implements AbstractRequirement {

    private long needed;

    public MoneyRequirement(long needed) {
        this.needed = needed;
    }

    @Override
    public long getNeeded() {
        return this.needed;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MONEY;
    }

    @Override
    public boolean isFulfilled(Clan clan, Player player) {
        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
        if (user.getMoney() >= this.needed)
            return true;
        return false;
    }

}