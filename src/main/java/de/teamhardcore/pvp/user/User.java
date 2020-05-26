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
    private UserMarket userMarket;


    public User(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(this.uuid);
        this.userData = new UserData(this);
        this.userMoney = new UserMoney(this);
        this.userStats = new UserStats(this);
        this.userMarket = new UserMarket(this);
    }

    public long getMoney() {
        return this.userMoney.getMoney();
    }

    public void addMoney(long amount) {
        this.userMoney.addMoney(amount);
    }

    public void removeMoney(long amount) {
        this.userMoney.removeMoney(amount);
    }

    public void setMoney(long amount) {
        this.userMoney.setMoney(amount);
    }

    public UserMoney getUserMoney() {
        return userMoney;
    }

    public UserData getUserData() {
        return userData;
    }

    public UserStats getUserStats() {
        return userStats;
    }

    public UserMarket getUserMarket() {
        return userMarket;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }
}
