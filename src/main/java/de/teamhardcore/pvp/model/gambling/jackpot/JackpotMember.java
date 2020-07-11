/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot;

import java.util.UUID;

public class JackpotMember {

    private final UUID uuid;
    private final long amount;

    public JackpotMember(UUID uuid, long amount) {
        this.uuid = uuid;
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public UUID getUuid() {
        return uuid;
    }
}
