package de.teamhardcore.pvp.model;

import org.bukkit.scoreboard.Scoreboard;

public class PlayerScoreboard {

    private Scoreboard scoreboard;
    private ScoreboardType type;

    public PlayerScoreboard(Scoreboard scoreboard, ScoreboardType type) {
        this.scoreboard = scoreboard;
        this.type = type;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public ScoreboardType getType() {
        return type;
    }

    public enum ScoreboardType {
        DEFAULT,
        EVENT,
        COMBAT
    }
}
