/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

public class UserMoney {

    private User user;

    private long money;

    public UserMoney(User user) {
        this.user = user;
        this.money = 0;
    }

    /**
     * Set the money
     *
     * @param amount The amount to which the player's money is set
     */
    public void setMoney(long amount) {
        this.money = amount;
    }

    /**
     * Add money
     *
     * @param amount The amount to which the player's money is added
     */
    public void addMoney(long amount) {
        this.money += amount;
    }

    /**
     * Remove money
     *
     * @param amount The amount to which the player's money is removed
     */
    public void removeMoney(long amount) {
        this.money -= amount;
    }

    /**
     * Get money
     */
    public long getMoney() {
        return money;
    }

}
