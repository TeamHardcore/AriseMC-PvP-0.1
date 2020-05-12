/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.actions;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.User;
import org.bukkit.entity.Player;

public class CustomMoneyAction implements CrateItemAction {

    private long amount;

    public CustomMoneyAction(long amount) {
        this.amount = amount;
    }

    @Override
    public void doAction(Player player) {
        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
        user.addMoney(this.amount);
    }
}
