/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.help;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Report;
import de.teamhardcore.pvp.utils.DateFormats;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandReport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;
        Player player = (Player) cs;

        if (args.length == 0) {
            //TODO: SENDING USAGE
        }

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("list")) {
                if (!player.hasPermission("arisemc.report")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                if (Main.getInstance().getReportManager().getReports().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cIm Moment gibt es keine offenen Reports.");
                    return true;
                }

                List<Report> reports = new ArrayList<>(Main.getInstance().getReportManager().getReports().values());

                player.sendMessage("§8§m->-----------§r §c§lREPORT §8§m-----------<-");
                player.sendMessage(StringDefaults.PREFIX + "§eFolgende Spieler wurden reportet§8: ");
                player.sendMessage(" ");

                for (int i = 0; i < reports.size(); i++) {
                    Report report = reports.get(i);

                    JSONMessage message = new JSONMessage("  §8§l- §7" + report.getTarget().getName());
                    message.tooltip("§7Klicke hier, für weitere Details");
                    message.runCommand("/report info " + report.getTarget().getName());
                    message.send(player);

                    if (i >= reports.size() - 1) break;
                }
                player.sendMessage(" ");
                player.sendMessage(StringDefaults.PREFIX + "§eKlicke auf einen Namen für weitere Details.");
                player.sendMessage("§8§m->-----------§r §c§lREPORT §8§m-----------<-");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dich nicht selber reporten.");
                return true;
            }

            if (Main.getInstance().getReportManager().hasReported(player, target)) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast diesen Spieler bereits reportet.");
                return true;
            }

            Main.getInstance().getReportManager().openReportConfirmation(player, target);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                if (!player.hasPermission("arisemc.report")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                if (Main.getInstance().getReportManager().getReport(target) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler wurde nicht reportet.");
                    return true;
                }

                Report report = Main.getInstance().getReportManager().getReport(target);
                player.sendMessage("§8§m->-----------§r §c§lREPORT §8§m-----------<-");
                player.sendMessage(StringDefaults.PREFIX + "§eFolgende Spieler haben §7" + target.getName() + " §ereportet§8:");
                player.sendMessage(" ");

                for (Map.Entry<UUID, HashMap<Report.ReportReason, Long>> entry : report.getReporter().entrySet()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(entry.getKey());
                    if (offlinePlayer == null) continue;

                    Optional<Report.ReportReason> reason = entry.getValue().keySet().stream().findFirst();
                    if (!reason.isPresent()) continue;
                    Optional<Long> timestamp = entry.getValue().values().stream().findFirst();

                    player.sendMessage("  §8§l- " + (offlinePlayer.isOnline() ? "§a§l" : "§c§l") + offlinePlayer.getName() + " §7§o(" + reason.get().getName() + ", " + DateFormats.FORMAT_SIMPLE.format(timestamp.get()) + ")");
                }

                player.sendMessage("§8§m->-----------§r §c§lREPORT §8§m-----------<-");
            } else if (args[0].equalsIgnoreCase("done")) {
                if (!player.hasPermission("arisemc.report")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                if (Main.getInstance().getReportManager().getReport(target) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler wurde nicht reportet.");
                    return true;
                }

                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Report von §7" + target.getName() + " §egelöscht.");
                Main.getInstance().getReportManager().removeReport(target, false);
            }
        }

        return true;
    }
}
