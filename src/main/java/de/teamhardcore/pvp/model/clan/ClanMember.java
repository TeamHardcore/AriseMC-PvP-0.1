/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan;

import de.teamhardcore.pvp.database.TimedDatabaseUpdate;

import java.util.UUID;

public class ClanMember extends TimedDatabaseUpdate {

    private UUID uuid;
    private Clan clan;
    private String lastName;
    private ClanRank rank;

    public ClanMember(UUID uuid, String lastName, Clan clan, ClanRank rank) {
        super("ClanMember", true);
        this.uuid = uuid;
        this.clan = clan;
        this.lastName = lastName;
        this.rank = rank;

        setReady(true);
    }

    public ClanMember(UUID uuid, boolean timedUpdate, boolean async) {
        super("ClanMember", timedUpdate);
        this.uuid = uuid;

        if (async)
            loadDataAsync();
        else
            loadData();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLastName() {
        return lastName;
    }

    public ClanRank getRank() {
        return rank;
    }

    public void setRank(ClanRank rank) {
        this.rank = rank;
        setUpdate(true);
    }

    public Clan getClan() {
        return clan;
    }

    @Override
    public void saveData() {
        //TODO: load data
    }

    @Override
    public void loadData() {
        //not used
    }

    @Override
    public void deleteData() {
        //todo: delete data
    }
}
