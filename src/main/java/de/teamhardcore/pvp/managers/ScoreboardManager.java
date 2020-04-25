package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.PlayerScoreboard;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class ScoreboardManager {
    private final Main plugin;

    private final HashMap<Player, PlayerScoreboard> scoreboards = new HashMap<>();

    public ScoreboardManager(Main plugin) {
        this.plugin = plugin;

        startUpdaterTask();
    }

    public void setScoreboard(Player player, PlayerScoreboard.ScoreboardType type) {
        if (this.scoreboards.containsKey(player)) {
            this.scoreboards.remove(player);
            player.setScoreboard(this.plugin.getServer().getScoreboardManager().getNewScoreboard());
        }

        Scoreboard scoreboard = this.plugin.getServer().getScoreboardManager().getNewScoreboard();

        Objective sidebar = scoreboard.registerNewObjective("Default", "Default");
        sidebar.setDisplayName("    " + StringDefaults.GLOBAL_PREFIX + "   ");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        switch (type) {
            case DEFAULT:

                Team teamPlayer = scoreboard.registerNewTeam("Player");
                Team teamMoney = scoreboard.registerNewTeam("Money");
                Team teamLeague = scoreboard.registerNewTeam("League");

                teamPlayer.setSuffix(player.getName());
                teamPlayer.addEntry("§7");
                teamLeague.addEntry("§7§7");
                teamMoney.addEntry("§7§7§7");

                sidebar.getScore(" ").setScore(10);
                sidebar.getScore("§a§lName: §7").setScore(9);
                sidebar.getScore("§7").setScore(8);
                sidebar.getScore("  ").setScore(7);
                sidebar.getScore("§6§lMünzen: §7").setScore(6);
                sidebar.getScore("§7§7§7").setScore(5);
                sidebar.getScore("   ").setScore(4);
                sidebar.getScore("§e§lLiga: §7").setScore(3);
                sidebar.getScore("§7§7").setScore(2);
                sidebar.getScore("    ").setScore(1);
                player.setScoreboard(scoreboard);
                this.scoreboards.put(player, new PlayerScoreboard(scoreboard, type));
                updateSidebar(player);
                break;
            case EVENT:
                break;
            case COMBAT:
                break;
        }
    }

    public void updateSidebar(Player player) {
        if (!this.scoreboards.containsKey(player))
            setScoreboard(player, PlayerScoreboard.ScoreboardType.DEFAULT);

        PlayerScoreboard playerScoreboard = this.scoreboards.get(player);
        Scoreboard scoreboard = playerScoreboard.getScoreboard();

        User user = this.plugin.getUserManager().getUser(player.getUniqueId());

        switch (playerScoreboard.getType()) {
            case DEFAULT:

                Team teamMoney = scoreboard.getTeam("Money");
                Team teamLeague = scoreboard.getTeam("League");
                teamMoney.setSuffix("§7" + user.getMoney() + "$");
                teamLeague.setPrefix("§cUnplatziert §7");
                teamLeague.setSuffix("[§60§7] ");
                break;
        }
    }

    private void startUpdaterTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            this.scoreboards.keySet().forEach(this::updateSidebar);
        }, 20L, 20L);
    }

}
