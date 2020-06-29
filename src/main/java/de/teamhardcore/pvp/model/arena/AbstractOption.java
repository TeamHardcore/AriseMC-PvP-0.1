/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena;

public abstract class AbstractOption {

    private final String category;
    private final String optionName;

    public AbstractOption(String category, String optionName) {
        this.category = category;
        this.optionName = optionName;
    }

    public boolean validateParams() {
        return true;
    }

    public String getCategory() {
        return category;
    }

    public String getOptionName() {
        return optionName;
    }

    public abstract String toString();

}
