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

    public void setMoney(long amount) {
        this.money = amount;
    }

    public void addMoney(long amount) {
        this.money += amount;
    }

    public void removeMoney(long amount) {
        this.money -= amount;
    }

    public long getMoney() {
        return money;
    }

}
