package de.teamhardcore.pvp.model.teleport;

import org.bukkit.entity.Player;

public class TPRequest {

    private Player player, to;
    private boolean here;
    private long sent;

    public TPRequest(Player player, Player to, boolean here) {
        this.player = player;
        this.to = to;
        this.here = here;
        this.sent = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTo() {
        return to;
    }

    public boolean isHere() {
        return here;
    }

    public long getSent() {
        return sent;
    }
}
