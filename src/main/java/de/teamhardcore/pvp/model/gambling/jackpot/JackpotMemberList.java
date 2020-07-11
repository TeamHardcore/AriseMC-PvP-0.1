/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JackpotMemberList {

    private final Jackpot parent;

    private final Map<UUID, JackpotMember> members;
    private final Map<JackpotMember, Double> cachedChances;

    public JackpotMemberList(Jackpot parent) {
        this.parent = parent;
        this.members = new HashMap<>();
        this.cachedChances = new HashMap<>();
    }

    private void refreshChances() {
        this.cachedChances.clear();
        for (Map.Entry<UUID, JackpotMember> entry : this.members.entrySet()) {
            this.cachedChances.put(entry.getValue(), getPercentChange(entry.getValue()));
        }
    }

    public void addMember(UUID uuid, long amount) {
        if (this.members.containsKey(uuid)) return;
        this.members.put(uuid, new JackpotMember(uuid, amount));
        this.parent.getStatistics().setCollectedMoney(this.parent.getStatistics().getCollectedMoney() + amount);
        refreshChances();
    }

    public void removeMember(UUID uuid) {
        if (!this.members.containsKey(uuid))
            return;

        JackpotMember member = this.members.get(uuid);
        this.members.remove(uuid);
        this.parent.getStatistics().setCollectedMoney(
                this.parent.getStatistics().getCollectedMoney() - member.getAmount());
        refreshChances();
    }

    public Double getPercentChange(JackpotMember member) {
        if (this.cachedChances.containsKey(member))
            return this.cachedChances.get(member);

        return (double) member.getAmount() / this.parent.getStatistics().getCollectedMoney() * 100.0D;
    }

    public JackpotMember getMember(UUID uuid) {
        if (!this.members.containsKey(uuid)) return null;
        return this.members.get(uuid);
    }

    public Map<UUID, JackpotMember> getMembers() {
        return members;
    }
}
