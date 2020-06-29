/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena;

public class ArenaOptionBase extends AbstractOption {

    public ArenaOptionBase(String category, String optionName) {
        super(category, optionName);
    }

    public boolean validateParams() {
        return true;
    }

    @Override
    public String toString() {
        return getCategory() + ":" + getOptionName();
    }
}
