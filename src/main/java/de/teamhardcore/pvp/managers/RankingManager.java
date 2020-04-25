/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RankingManager {

    private final Main plugin;

    private final HashMap<UUID, Long> pvpRanking = new HashMap<>();
    private final HashMap<UUID, Long> moneyRanking = new HashMap<>();

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public RankingManager(Main plugin) {
        this.plugin = plugin;

        startRankingUpdater();
    }

    private void startRankingUpdater() {
        service.scheduleAtFixedRate(() -> {
            this.pvpRanking.clear();
            this.moneyRanking.clear();

        }, 1L, 30L, TimeUnit.SECONDS);
    }

    public HashMap<UUID, Long> getMoneyRanking() {
        return moneyRanking;
    }

    public HashMap<UUID, Long> getPvpRanking() {
        return pvpRanking;
    }
}
