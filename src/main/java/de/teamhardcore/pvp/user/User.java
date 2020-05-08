/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {

    private final UUID uuid;
    private final Player player;

    private UserMoney userMoney;
    private UserData userData;
    private UserStats userStats;

    public User(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(this.uuid);
        this.userData = new UserData(this);
        this.userMoney = new UserMoney(this);
        this.userStats = new UserStats(this);
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

    public UserData getUserData() {
        return userData;
    }

    public UserStats getUserStats() {
        return userStats;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }
}
