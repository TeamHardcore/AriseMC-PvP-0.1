/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.ClanMember;
import de.teamhardcore.pvp.model.clan.ClanRank;
import de.teamhardcore.pvp.model.clan.shop.ClanShopManager;

import java.util.*;

public class ClanManager {

    private final Main plugin;

    private final HashMap<String, Clan> clans = new HashMap<>();
    private final HashMap<UUID, ClanMember> clanMembers = new HashMap<>();
    private final HashMap<UUID, List<Clan>> clanRequests = new HashMap<>();

    private ClanShopManager clanShopManager;

    public ClanManager(Main plugin) {
        this.plugin = plugin;
        this.clanShopManager = new ClanShopManager();
        loadClans();
    }

    private void loadClans() {

    }

    public void createClan(UUID uuid, String ownerName, String name) {
        if (this.clans.containsKey(name)) return;

        Clan clan = new Clan(name, true, true, false);
        addMember(uuid, ownerName, clan, ClanRank.OWNER);
        this.clans.put(name, clan);
    }

    public void deleteClan(String name) {
        if (!this.clans.containsKey(name)) return;

        Clan clan = getClan(name);

        Map<UUID, ClanMember> copy = new HashMap<>(clan.getMemberList().getMembers());

        for (Map.Entry<UUID, ClanMember> entry : copy.entrySet()) {
            if (getMember(entry.getKey()) != null) {
                removeMember(entry.getKey());
                continue;
            }
            // entry.getValue().deleteDataAsync();
            clan.getMemberList().removeMember(entry.getValue());
        }
        // clan.deleteDataAsync();
        this.clans.remove(name);
    }

    public void addMember(UUID uuid, String lastName, Clan clan, ClanRank rank) {
        if (this.clanMembers.containsKey(uuid)) return;
        ClanMember member = new ClanMember(uuid, lastName, clan, rank);
        //member.saveDataAsync();
        clan.getMemberList().addMember(member);
        this.clanMembers.put(uuid, member);
    }

    public void removeMember(UUID uuid) {
        ClanMember member = this.clanMembers.get(uuid);
        if (member == null) return;

        Clan clan = member.getClan();
        //  member.deleteDataAsync();
        clan.getMemberList().removeMember(member);
        this.clanMembers.remove(uuid);
    }

    public void addClanRequest(UUID uuid, Clan clan) {
        List<Clan> requests = new ArrayList<>();

        if (this.clanRequests.containsKey(uuid))
            requests = this.clanRequests.get(uuid);

        if (requests.contains(clan))
            return;

        requests.add(clan);
        this.clanRequests.put(uuid, requests);
    }

    public void removeClanRequest(UUID uuid, Clan clan) {
        if (getClanRequests(uuid).isEmpty())
            return;

        List<Clan> requests = getClanRequests(uuid);
        if (!requests.contains(clan))
            return;

        requests.remove(clan);
        this.clanRequests.put(uuid, requests);
    }

    public Clan getClan(UUID uuid) {
        if (!this.clanMembers.containsKey(uuid))
            return null;
        return this.clanMembers.get(uuid).getClan();
    }

    public Clan getClan(String name) {
        for (Map.Entry<String, Clan> entry : this.clans.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(name))
                return entry.getValue();
        }
        return null;
    }

    public ClanMember getMember(UUID uuid) {
        if (!this.clanMembers.containsKey(uuid)) return null;
        return this.clanMembers.get(uuid);
    }

    public List<Clan> getClanRequests(UUID uuid) {
        if (!this.clanRequests.containsKey(uuid))
            return new ArrayList<>();
        return this.clanRequests.get(uuid);
    }

    public HashMap<UUID, ClanMember> getClanMembers() {
        return clanMembers;
    }

    public HashMap<String, Clan> getClans() {
        return clans;
    }

    public boolean hasClan(UUID uuid) {
        return this.clanMembers.containsKey(uuid);
    }

    public ClanShopManager getClanShopManager() {
        return clanShopManager;
    }
}
