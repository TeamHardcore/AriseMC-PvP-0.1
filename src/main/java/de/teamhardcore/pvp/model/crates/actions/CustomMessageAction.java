/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.actions;

import org.bukkit.entity.Player;

public class CustomMessageAction implements CrateItemAction {

    private String message;

    public CustomMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void doAction(Player player) {
        player.sendMessage(this.message);
    }
}
