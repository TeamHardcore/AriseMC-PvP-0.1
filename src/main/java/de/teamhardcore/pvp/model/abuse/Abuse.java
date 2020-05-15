/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.abuse;

import java.util.UUID;

public class Abuse {

    private String sender;
    private UUID target;
    private String accessKey;
    private String reason;
    private AbuseType type;
    private long create;
    private long end;
    private boolean unbanned;

    public AbuseType getType() {
        return type;
    }

    public void setType(AbuseType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public UUID getTarget() {
        return target;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getReason() {
        return reason;
    }

    public long getCreate() {
        return create;
    }

    public long getEnd() {
        return end;
    }

    public boolean isUnbanned() {
        return unbanned;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setUnbanned(boolean unbanned) {
        this.unbanned = unbanned;
    }
}
