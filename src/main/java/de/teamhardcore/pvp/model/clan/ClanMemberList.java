/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClanMemberList {

    private final HashMap<UUID, ClanMember> members;
    private Clan clan;

    public ClanMemberList(Clan clan) {
        this.members = new HashMap<>();
        this.clan = clan;

        loadMembers();
    }

    private void loadMembers() {
        //TODO: LOAD MEMBERS;
    }

    public ClanMember getMember(UUID uuid) {
        if (!this.members.containsKey(uuid)) return null;
        return this.members.get(uuid);
    }

    public void addMember(ClanMember member) {
        if (this.members.containsKey(member.getUuid())) return;
        this.members.put(member.getUuid(), member);
    }

    public void removeMember(ClanMember member) {
        if (!this.members.containsKey(member.getUuid())) return;
        this.members.remove(member.getUuid());
    }

    public boolean containsMember(ClanMember member) {
        return this.members.containsKey(member.getUuid());
    }

    public List<ClanMember> getMembers(ClanRank rank) {
        List<ClanMember> members = new ArrayList<>();
        this.members.values().stream().filter(member -> member.getRank().equals(rank)).forEach(members::add);
        return members;
    }

    public void sendMessageToMembers(String message, Player player) {
        for (ClanMember members : clan.getMemberList().getMembers().values()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
            if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player) continue;
            offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + message);
        }
    }

    public void sendMessageToMembers(String message) {
        for (ClanMember members : clan.getMemberList().getMembers().values()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
            if (offlinePlayer == null || !offlinePlayer.isOnline()) continue;
            offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + message);
        }
    }

    public Clan getClan() {
        return clan;
    }

    public HashMap<UUID, ClanMember> getMembers() {
        return members;
    }
}
