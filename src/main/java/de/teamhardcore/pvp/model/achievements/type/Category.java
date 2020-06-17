/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.type;

public enum Category {

    GENERAL("general", "Allgemeine"),
    COMABT("combat", "PvP");

    private final String packageName;
    private final String categoryName;

    Category(String packageName, String categoryName) {
        this.packageName = packageName;
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getPackageName() {
        return packageName;
    }
}
