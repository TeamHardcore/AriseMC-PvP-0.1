/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.pvp;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.EnumLeague;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.user.UserStats;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (args.length > 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/stats [Spieler]");
            return true;
        }

        if (args.length == 0) {
            sendStats(player, player);
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                UUIDFetcher.getUUID(args[0], uuid -> {
                    if (uuid == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler war noch nie auf dem Server.");
                        return;
                    }
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                    if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler war noch nie auf dem Server.");
                        return;
                    }

                    sendStats(player, offlinePlayer);
                });
            }

        }

        return true;
    }

    public static void sendStats(Player player, OfflinePlayer target) {
        User data = target.isOnline() ? Main.getInstance().getUserManager().getUser(target.getUniqueId()) : new User(target.getUniqueId());
        UserStats stats = data.getUserStats();
        UserMoney money = data.getUserMoney();

        Clan clan = Main.getInstance().getClanManager().getClan(target.getUniqueId());

        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lSTATS §8§l§m*-*-*-*-*-*-*-*-*");
        player.sendMessage(" ");
        player.sendMessage(StringDefaults.PREFIX + "§c§lName§8: §7" + target.getName() + " §c§lRang§8: §7-");
        player.sendMessage(StringDefaults.PREFIX + "§c§lKills§8: §7" + stats.getKills() + " §c§lTode§8: §7" + stats.getDeaths() + " §c§lKD§8: §7" + stats.getKD());
        player.sendMessage(StringDefaults.PREFIX + "§c§lLiga§8: §7" + EnumLeague.getLeagueByTrophies(stats.getTrophies()).getDisplayName() + " §7[§6" + Util.formatNumber(stats.getTrophies()) + "§7]");
        player.sendMessage(StringDefaults.PREFIX + "§c§lKillstreak§8: §7" + stats.getKillStreak() + " §c§lKopfgeld§8: §7" + Util.formatNumber(stats.getKopfgeld()) + "$");
        if (clan != null)
            player.sendMessage(StringDefaults.PREFIX + "§c§lClan§8: §7" + clan.getNameColor() + clan.getName());
        player.sendMessage(StringDefaults.PREFIX + "§c§lGeld§8: §7" + Util.formatNumber(money.getMoney()) + "$");
        player.sendMessage(StringDefaults.PREFIX + "§c§lSpielzeit§8: §7" + TimeUtil.timeToString(stats.getPlaytime()));
        player.sendMessage(" ");
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lSTATS §8§l§m*-*-*-*-*-*-*-*-*");


    }

}
