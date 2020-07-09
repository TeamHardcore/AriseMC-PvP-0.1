/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.punishment;

import java.util.UUID;

public class PunishmentInformation {

    private UUID uuid;
    private PunishmentType type;
    private String reason;
    private String by;
    private Long timestamp;
    private long created;

    /*
        Punishments (UUID, Type, Reason, By, Timestamp)
     */

    public PunishmentInformation(UUID uuid, PunishmentType type, String reason, String by, Long created, Long timestamp) {
        this.uuid = uuid;
        this.type = type;
        this.reason = reason;
        this.by = by;
        this.timestamp = timestamp;
        this.created = created;
    }

    public PunishmentInformation(UUID uuid, PunishmentType type, String reason, String by, Long timestamp) {
        this.uuid = uuid;
        this.type = type;
        this.reason = reason;
        this.by = by;
        this.timestamp = timestamp;
        this.created = System.currentTimeMillis();
    }

    public PunishmentInformation(UUID uuid, PunishmentType type, String by, Long timestamp) {
        this(uuid, type, "Nicht angegeben", by, timestamp);
    }

    public PunishmentInformation(UUID uuid, PunishmentType type, Long timestamp) {
        this(uuid, type, "Nicht angegeben", "console", timestamp);
    }

    public boolean isPermanent() {
        return this.timestamp == -1L;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PunishmentType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getBy() {
        return by;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public long getCreated() {
        return created;
    }
}
