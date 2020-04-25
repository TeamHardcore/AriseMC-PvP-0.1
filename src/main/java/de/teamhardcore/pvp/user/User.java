/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

import java.util.UUID;

public class User {

    private UserMoney userMoney;

    public User(UUID uuid) {
        this.userMoney = new UserMoney(this);
    }

    /**
     * Get the money of this user
     */
    public long getMoney() {
        return this.userMoney.getMoney();
    }

    /**
     * Add money to this user
     *
     * @param amount
     */
    public void addMoney(long amount) {
        this.userMoney.addMoney(amount);
    }

    /**
     * Remove money of this user
     *
     * @param amount
     */
    public void removeMoney(long amount) {
        this.userMoney.removeMoney(amount);
    }

    public void setMoney(long amount) {
        this.userMoney.setMoney(amount);
    }

    /**
     * The class where all currency will operated
     */
    public UserMoney getUserMoney() {
        return userMoney;
    }
}
