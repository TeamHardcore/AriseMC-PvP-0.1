/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.punishment;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ArchiveInformation {

    private final UUID uuid;
    private final List<PunishmentInformation> archivedPunishments;

    public ArchiveInformation(UUID uuid, List<PunishmentInformation> archivedPunishments) {
        this.uuid = uuid;
        this.archivedPunishments = archivedPunishments;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<PunishmentInformation> getArchivedPunishments() {
        Collections.reverse(this.archivedPunishments);
        return archivedPunishments;
    }
}
