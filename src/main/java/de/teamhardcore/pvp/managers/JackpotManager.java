/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.jackpot.Jackpot;

import java.util.Random;

public class JackpotManager {

    private static final int TEN_MINUTES = 10 * 60 * 1000;
    private static final Random RANDOM = new Random();

    private final Main plugin;

    private Jackpot currentJackpot;

    private long lastJackpotTimestamp;

    public JackpotManager(Main plugin) {
        this.plugin = plugin;
        this.currentJackpot = null;
        this.lastJackpotTimestamp = 0L;

        startJackpotTask();
    }

    private void startJackpotTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(Main.getInstance(), () -> {
            long diff = System.currentTimeMillis() - TEN_MINUTES;
            long lastJackpot = this.lastJackpotTimestamp;

            if (lastJackpot < diff) {
                if (this.currentJackpot != null) return;
                startJackpot(Math.round(RANDOM.nextInt(100000)));
            }

        }, 60L, 60L);
    }

    public void startJackpot(long amount) {
        if (this.currentJackpot != null) return;
        this.currentJackpot = new Jackpot(amount, 1);

        this.lastJackpotTimestamp = System.currentTimeMillis();
    }

    public void stopJackpot() {
        if (this.currentJackpot == null) return;
        this.currentJackpot = null;
        //todo: stop
    }

    public Jackpot getCurrentJackpot() {
        return currentJackpot;
    }

    public boolean isRunning() {
        return this.currentJackpot != null;
    }

    public Main getPlugin() {
        return plugin;
    }
}
