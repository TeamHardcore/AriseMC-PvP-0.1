/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.actions;

import org.bukkit.entity.Player;

public class CustomMoneyAction implements CrateItemAction {

    private long amount;

    public CustomMoneyAction(long amount) {
        this.amount = amount;
    }

    @Override
    public void doAction(Player player) {
        //todo: ADD MONEY TO USER
    }
}
