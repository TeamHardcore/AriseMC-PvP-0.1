/*
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 *
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.EnumLeague;
import de.teamhardcore.pvp.model.PlayerScoreboard;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserStats;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
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

        Objective healthOb = scoreboard.registerNewObjective("PlayerHealth", "health");
        healthOb.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthOb.setDisplayName("§c§lHerzen");

        Team teamMember = scoreboard.registerNewTeam("LTeamMember");
        Team teamPro = scoreboard.registerNewTeam("KTeamUltra");
        Team teamElite = scoreboard.registerNewTeam("JTeamElite");
        Team teamHero = scoreboard.registerNewTeam("ITeamHero");
        Team teamYT = scoreboard.registerNewTeam("HTeamYT");
        Team teamAsk = scoreboard.registerNewTeam("GTeamAsk");
        Team teamFreund = scoreboard.registerNewTeam("FTeamFreund");
        Team teamSup = scoreboard.registerNewTeam("ETeamSup");
        Team teamMod = scoreboard.registerNewTeam("DTeamMod");
        Team teamDev = scoreboard.registerNewTeam("CTeamDev");
        Team teamAdmin = scoreboard.registerNewTeam("BTeamAdmin");
        Team teamOwner = scoreboard.registerNewTeam("ATeamOwner");

        teamMember.setPrefix("§7");
        teamPro.setPrefix("§b");
        teamElite.setPrefix("§e");
        teamHero.setPrefix("§6");
        teamYT.setPrefix("§d");
        teamAsk.setPrefix("§2");
        teamFreund.setPrefix("§b");
        teamSup.setPrefix("§a");
        teamMod.setPrefix("§5");
        teamDev.setPrefix("§3");
        teamAdmin.setPrefix("§c");
        teamOwner.setPrefix("§4");

        Objective sidebar = scoreboard.registerNewObjective("Default", "Default");
        sidebar.setDisplayName("    " + StringDefaults.SERVER_NAME + "   ");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        switch (type) {
            case DEFAULT:

                Team teamPlayer = scoreboard.registerNewTeam("Player");
                Team teamMoney = scoreboard.registerNewTeam("Money");
                Team teamLeague = scoreboard.registerNewTeam("League");
                Team teamStats = scoreboard.registerNewTeam("Stats");

                teamPlayer.setSuffix(player.getName());
                teamPlayer.addEntry("§7");
                teamMoney.addEntry("§7§7");
                teamStats.addEntry("§7§7§7");
                teamLeague.addEntry("§7§7§7§7");

                sidebar.getScore(" ").setScore(13);
                sidebar.getScore("§6§lName: §7").setScore(12);
                sidebar.getScore("§7").setScore(11);
                sidebar.getScore("  ").setScore(10);
                sidebar.getScore("§e§lMünzen: §7").setScore(9);
                sidebar.getScore("§7§7").setScore(8);
                sidebar.getScore("    ").setScore(7);
                sidebar.getScore("§a§lStats").setScore(6);
                sidebar.getScore("§7§7§7").setScore(5);
                sidebar.getScore("     ").setScore(4);
                sidebar.getScore("§b§lLiga: §7").setScore(3);
                sidebar.getScore("§7§7§7§7").setScore(2);

            case EVENT:
                break;
            case COMBAT:
                break;
        }
        player.setScoreboard(scoreboard);
        this.scoreboards.put(player, new PlayerScoreboard(scoreboard, type));
        updateSidebar(player);
    }

    public void updateTeamListAllPlayers(Player forWhom) {
        for (Player all : scoreboards.keySet()) {
            updateTeamList(forWhom, all, all.isOnline());
        }
    }

    public void updateTeamList(Player forWhom, Player who, boolean online) {
        if (!scoreboards.containsKey(forWhom)) {
            setScoreboard(forWhom, PlayerScoreboard.ScoreboardType.DEFAULT);
        }
        Scoreboard board = scoreboards.get(forWhom).getScoreboard();
        Team playerTeam = getTeamForPlayer(board, who);
        if (online) {
            playerTeam.addEntry(who.getName());
        } else {
            playerTeam.removeEntry(who.getName());
        }
    }

    public void updateAllScoreboards(boolean teamList, boolean sidebar) {
        for (Player all : scoreboards.keySet()) {
            if (teamList)
                updateTeamListAllPlayers(all);
            if (!sidebar) continue;
            updateSidebar(all);
        }
    }

    private Team getTeamForPlayer(Scoreboard board, Player forWhom) {
        if (forWhom.hasPermission("tab.color.owner")) {
            return board.getTeam("ATeamOwner");
        }
        if (forWhom.hasPermission("tab.color.admin")) {
            return board.getTeam("BTeamAdmin");
        }
        if (forWhom.hasPermission("tab.color.dev")) {
            return board.getTeam("CTeamDev");
        }
        if (forWhom.hasPermission("tab.color.mod")) {
            return board.getTeam("DTeamMod");
        }
        if (forWhom.hasPermission("tab.color.sup")) {
            return board.getTeam("ETeamSup");
        }
        if (forWhom.hasPermission("tab.color.freund")) {
            return board.getTeam("FTeamFreund");
        }
        if (forWhom.hasPermission("tab.color.ask")) {
            return board.getTeam("GTeamAsk");
        }
        if (forWhom.hasPermission("tab.color.yt")) {
            return board.getTeam("HTeamYT");
        }
        if (forWhom.hasPermission("tab.color.hero")) {
            return board.getTeam("ITeamHero");
        }
        if (forWhom.hasPermission("tab.color.elite")) {
            return board.getTeam("JTeamElite");
        }
        if (forWhom.hasPermission("tab.color.pro")) {
            return board.getTeam("KTeamUltra");
        }
        return board.getTeam("LTeamMember");
    }

    public void updateSidebar(Player player) {
        if (!this.scoreboards.containsKey(player))
            setScoreboard(player, PlayerScoreboard.ScoreboardType.DEFAULT);

        PlayerScoreboard playerScoreboard = this.scoreboards.get(player);
        Scoreboard scoreboard = playerScoreboard.getScoreboard();

        User user = this.plugin.getUserManager().getUser(player.getUniqueId());
        UserStats stats = user.getUserStats();


        switch (playerScoreboard.getType()) {
            case DEFAULT:
                Team teamMoney = scoreboard.getTeam("Money");
                Team teamLeague = scoreboard.getTeam("League");
                Team teamStats = scoreboard.getTeam("Stats");
                teamMoney.setSuffix("§7" + Util.formatNumber(user.getMoney()) + "$");
                teamLeague.setPrefix("" + EnumLeague.getLeagueByTrophies(stats.getTrophies()).getDisplayName() + " §7");
                teamLeague.setSuffix("[§6" + Util.formatNumber(stats.getTrophies()) + "§7] ");

                teamStats.setPrefix("§7" + stats.getKills() + " §aKills");
                teamStats.setSuffix(" §8/ §7" + stats.getDeaths() + " §cTode");
                break;
            case COMBAT:
                break;
            case EVENT:
        }
    }

    private void startUpdaterTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            this.scoreboards.keySet().forEach(this::updateSidebar);
        }, 20L, 20L);
    }

}
