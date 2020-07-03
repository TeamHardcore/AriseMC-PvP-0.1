/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.phases;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractDuelPhase implements Runnable {

    private BukkitTask task;
    private Duel duel;
    private int time, delay, repeat;

    public AbstractDuelPhase(Duel duel, int delay, int repeat) {
        this.duel = duel;
        this.delay = delay;
        this.repeat = repeat;
    }

    public void start(int time) {
        stop();
        this.time = time;
        this.task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, this.delay, this.repeat);
    }

    public void stop() {
        if (!isCurrentlyRunning()) return;
        this.task.cancel();
        this.task = null;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public boolean isCurrentlyRunning() {
        return this.task != null;
    }

    public Duel getDuel() {
        return duel;
    }

    public abstract DuelPhase getType();

}
