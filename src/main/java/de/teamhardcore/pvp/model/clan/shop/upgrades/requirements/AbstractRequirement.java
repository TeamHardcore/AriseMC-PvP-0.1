/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.requirements;

import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public interface AbstractRequirement {

    RequirementType getType();

    long getNeeded();

    boolean isFulfilled(Clan clan, Player player);

}
