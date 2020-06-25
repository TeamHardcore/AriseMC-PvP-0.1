package de.teamhardcore.pvp.model.teleport;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class TPDelay {

    private Player player;
    private Location location;
    private int time;
    private double times;
    private int runs;

    private BukkitTask task;

    public TPDelay(Player player, int startTime, int time) {
        this.time = time * 20;
        this.times = 0;
        this.runs = 0;

        this.player = player;
        this.location = player.getLocation();

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) {
                    cancel();
                    return;
                }

                if (getPlayer().isDead() || !getPlayer().isOnline() || onTick()) {
                    cancel();
                    Main.getInstance().getManager().getTeleportDelays().remove(getPlayer());
                    return;
                }

                if (TPDelay.this.hasMoved(TPDelay.this.location, TPDelay.this.player.getLocation())) {
                    TPDelay.this.player.sendMessage(StringDefaults.PREFIX + "§cDer Teleport wurde abgebrochen.");
                    cancel();
                    Main.getInstance().getManager().getTeleportDelays().remove(getPlayer());
                }

                TPDelay.this.times = TPDelay.this.times + Math.atan(0.41421356237309503);
                TPDelay.this.runs = TPDelay.this.runs + 1;

                for (int i = 0; i < 1; i++) {
                    //todo: play effect
                }


                if (TPDelay.this.runs >= TPDelay.this.time) {
                    onEnd();
                    //todo: play effect
                    Main.getInstance().getManager().getTeleportDelays().remove(getPlayer());
                    cancel();
                }

            }
        }.runTaskTimer(Main.getInstance(), startTime, 1L);
    }

    public void cancel() {
        this.task.cancel();
    }

    private boolean hasMoved(Location from, Location to) {
        return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }

    public abstract boolean onTick();

    public abstract void onEnd();

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public int getTime() {
        return time;
    }

    public double getTimes() {
        return times;
    }

    public int getRuns() {
        return runs;
    }

    public BukkitTask getTask() {
        return task;
    }
}
