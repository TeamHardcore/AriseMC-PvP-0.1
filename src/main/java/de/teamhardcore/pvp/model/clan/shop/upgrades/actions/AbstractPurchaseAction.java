/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.actions;

import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public interface AbstractPurchaseAction {
    void doAction(Clan clan, Player player);
}
