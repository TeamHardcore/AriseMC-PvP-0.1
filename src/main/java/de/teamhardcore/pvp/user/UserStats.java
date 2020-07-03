/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

import de.teamhardcore.pvp.database.TimedDatabaseUpdate;

import java.math.BigDecimal;

public class UserStats extends TimedDatabaseUpdate {

    private User user;

    private int kills, deaths, trophies, killStreak;
    private long timeCreated, playtime, kopfgeld;

    public UserStats(User user) {
        this(user, true, true);
    }

    public UserStats(User user, boolean timedUpdate, boolean asyncLoad) {
        super("UserStats", timedUpdate, 30000L);
        setForceUpdate(true);

        this.user = user;

        this.kills = 0;
        this.deaths = 0;
        this.trophies = 1572;
        this.playtime = 0L;
        this.killStreak = 0;
        this.kopfgeld = 0;

        this.timeCreated = System.currentTimeMillis();

       /* if (asyncLoad)
            loadDataAsync();
        else loadData();*/
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void calculateNewPlaytime() {
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - this.timeCreated;
        this.playtime = this.playtime + diff;
        this.timeCreated = currentTime;
        setUpdate(true);
    }

    public void setKills(int kills) {
        this.kills = kills;
        setUpdate(true);
    }

    public void addKills(int kills) {
        this.kills += kills;
        setUpdate(true);
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        setUpdate(true);
    }

    public void addDeaths(int deaths) {
        this.deaths += deaths;
        setUpdate(true);
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
        if (trophies < 0)
            this.trophies = 0;

        setUpdate(true);
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
        setUpdate(true);
    }

    public double getKD() {
        if (this.kills <= 0)
            return 0.0D;
        if (this.deaths <= 0)
            return getKills();
        BigDecimal dec = new BigDecimal(this.kills / this.deaths);
        dec = dec.setScale(2, 4);
        return dec.doubleValue();
    }

    public int getKillStreak() {
        return killStreak;
    }

    public long getKopfgeld() {
        return kopfgeld;
    }

    public void setKillStreak(int streak) {
        this.killStreak = streak;
        setUpdate(true);
    }

    public void setKopfgeld(long kopfgeld) {
        this.kopfgeld = kopfgeld;
        setUpdate(true);
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getTrophies() {
        return trophies;
    }

    public long getPlaytime() {
        return playtime;
    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void deleteData() {

    }
}
