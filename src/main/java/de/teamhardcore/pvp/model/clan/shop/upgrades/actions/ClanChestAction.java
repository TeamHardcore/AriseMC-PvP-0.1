/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades.actions;

import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.ClanChest;
import org.bukkit.entity.Player;

public class ClanChestAction implements AbstractPurchaseAction {

    private int slots;

    public ClanChestAction(int slotsToAdd) {
        this.slots = slotsToAdd;
    }

    @Override
    public void doAction(Clan clan, Player player) {
        clan.getClanChest().addSlot(this.slots);
    }
}
