/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Report {

    private final HashMap<UUID, HashMap<ReportReason, Long>> reporter = new HashMap<>();

    private Player target;

    public Report(Player target) {
        this.target = target;
    }

    public void addReport(Player player, ReportReason reportReason) {
        if (this.reporter.containsKey(player.getUniqueId())) return;

        this.reporter.put(player.getUniqueId(), new HashMap<ReportReason, Long>() {{
            put(reportReason, System.currentTimeMillis());
        }});
    }

    public void removeReport(Player player) {
        if (!this.reporter.containsKey(player.getUniqueId())) return;

        this.reporter.remove(player.getUniqueId());
    }

    public HashMap<UUID, HashMap<ReportReason, Long>> getReporter() {
        return reporter;
    }

    public Player getTarget() {
        return target;
    }

    public enum ReportReason {
        CHEATING("Cheating/Hack-Client"),
        INSULT("Beleidigung"),
        SPAM("Spam");

        private String name;

        ReportReason(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
