/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import org.bukkit.entity.Item;

import java.util.List;
import java.util.UUID;

public class LootProtection {

    private final UUID uuid;
    private final List<Item> items;

    private long timestamp = System.currentTimeMillis();

    public LootProtection(UUID uuid, List<Item> items) {
        this.uuid = uuid;
        this.items = items;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Item> getItems() {
        return items;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
