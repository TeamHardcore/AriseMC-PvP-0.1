/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan;

import de.teamhardcore.pvp.database.TimedDatabaseUpdate;
import de.teamhardcore.pvp.model.clan.shop.upgrades.AbstractUpgrade;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Clan extends TimedDatabaseUpdate {

    private final ClanMemberList memberList = new ClanMemberList(this);
    private final ClanChest clanChest;

    private final String name;
    private Location base;

    private final Map<String, Integer> upgrades = new HashMap<>();
    private final Map<String, Location> warps = new HashMap<>();

    private int kills, deaths, trophies, maxMembers, maxWarps, rank, level;
    private long money;

    /**
     * @param name
     * @param timed
     * @param async
     * @param load  if false = save else load
     */
    public Clan(String name, boolean timed, boolean async, boolean load) {
        super("Clan", timed);
        this.name = name;
        this.base = null;

        this.kills = 0;
        this.deaths = 0;
        this.trophies = 0;
        this.rank = -1;
        this.money = 0;
        this.maxMembers = 8;
        this.maxWarps = 0;
        this.level = 0;

        this.clanChest = new ClanChest(this, async);

      /*  if (load) {
            if (async)
                loadDataAsync();
            else loadData();
        } else if (async) {
            saveDataAsync();
        } else saveData();*/
    }

    public void setUpgrade(String identifier, AbstractUpgrade upgrade) {
        this.upgrades.put(identifier, upgrade.getLevel());
    }

    public int getUpgradeLevel(String identifier) {
        if (!this.upgrades.containsKey(identifier)) return 0;
        return this.upgrades.get(identifier);
    }

    public Map<String, Integer> getUpgrades() {
        return upgrades;
    }

    public ClanMemberList getMemberList() {
        return memberList;
    }

    public String getName() {
        return name;
    }

    public String getNameColor() {
        if (this.name.equals("Team"))
            return "§4§l";

        int level = getLevel();
        if (level >= 4)
            return "§c";
        if (level >= 2)
            return "§8";
        if (level >= 1)
            return "§d";
        return "§b";
    }

    public Location getBase() {
        return base;
    }

    public void setBase(Location base) {
        this.base = base;
        setUpdate(true);
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public long getMoney() {
        return money;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int max) {
        if (this.maxMembers == max) return;
        this.maxMembers = max;
        setUpdate(true);
    }

    public int getLevel() {
        return level;
    }

    public void setKills(int kills) {
        this.kills = kills;
        setUpdate(true);
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
        setUpdate(true);
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
        setUpdate(true);
    }

    public void setLevel(int level) {
        this.level = level;
        setUpdate(true);
    }

    public void setMoney(long money) {
        this.money = money;
        setUpdate(true);
    }

    public void removeMoney(long money) {
        this.money -= money;
        setUpdate(true);
    }

    public void addMoney(long money) {
        this.money += money;
        setUpdate(true);
    }

    public Location getWarp(String name) {
        if (this.warps.containsKey(name))
            return this.warps.get(name);
        return null;
    }

    public void addWarp(String name, Location location) {
        this.warps.put(name, location);
        setUpdate(true);
    }

    public void removeWarp(String name) {
        this.warps.remove(name);
        setUpdate(true);
    }

    public Map<String, Location> getWarps() {
        return warps;
    }

    public int getMaxWarps() {
        return maxWarps;
    }

    public void setMaxWarps(int maxWarps) {
        this.maxWarps = maxWarps;
        setUpdate(true);
    }

    public ClanChest getClanChest() {
        return clanChest;
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
