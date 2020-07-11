/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot.phases;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.jackpot.Jackpot;
import de.teamhardcore.pvp.model.gambling.jackpot.JackpotState;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class JackpotPhaseBase implements Runnable {

    private final Jackpot parent;
    private final int delay;
    private final int repeat;
    private BukkitTask task;
    private int time;

    public JackpotPhaseBase(Jackpot parent, int delay, int repeat) {
        this.parent = parent;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isCurrentlyRunning() {
        return this.task != null;
    }

    public Jackpot getParent() {
        return parent;
    }

    public int getDelay() {
        return delay;
    }

    public int getRepeat() {
        return repeat;
    }

    public BukkitTask getTask() {
        return task;
    }

    public abstract JackpotState getState();

}
