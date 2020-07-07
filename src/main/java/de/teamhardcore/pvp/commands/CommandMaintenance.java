/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandMaintenance implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.maintenance")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length <= 0 || args.length > 2) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                Main.getInstance().getFileManager().getMaintenanceFile().setMaintenance(true);
                Bukkit.getOnlinePlayers().forEach(target -> {
                    if (!Main.getInstance().getFileManager().getMaintenanceFile().getPlayers().contains(target.getUniqueId().toString()) && !target.isOp()) {
                        if (Main.getInstance().getCombatManager().isTagged(target))
                            Main.getInstance().getCombatManager().setTagged(target, false);

                        target.kickPlayer("§cDu wurdest vom Server gekickt. Grund§8: §7Wartungsarbeiten");
                    }
                });
                player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§6Der Wartungsmodus wurden aktiviert.");
                player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§6Alle Spieler wurden gekickt.");
                return true;
            }

            if (args[0].equalsIgnoreCase("off")) {

                if (!Main.getInstance().getFileManager().getMaintenanceFile().isMaintenance()) {
                    player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§cDer Wartungsmodus ist nicht aktiviert.");
                    return true;
                }

                Main.getInstance().getFileManager().getMaintenanceFile().setMaintenance(false);
                player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§6Der Wartungsmodus wurden deaktiviert.");
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {

                if (Main.getInstance().getFileManager().getMaintenanceFile().getPlayers().isEmpty()) {
                    player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§cEs befinden sich keine Spieler auf der Liste.");
                    return true;
                }

                StringBuilder builder = new StringBuilder();

                for (String uuid : Main.getInstance().getFileManager().getMaintenanceFile().getPlayers()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                    if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) continue;

                    builder.append("§e").append(offlinePlayer.getName()).append("§7, ");
                }

                String players = builder.substring(0, builder.length() - 4);
                player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§eFolgende Spieler befinden sich auf der Liste§8: " + players);
                return true;
            }

            sendHelp(player, label);
        }

        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);

            if (args[0].equalsIgnoreCase("allow")) {
                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde nicht gefunden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde nicht gefunden.");
                            return;
                        }

                        if (Main.getInstance().getFileManager().getMaintenanceFile().getPlayers().contains(uuid.toString())) {
                            player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§cDer Spieler wurde breits hinzugefügt.");
                            return;
                        }

                        Main.getInstance().getFileManager().getMaintenanceFile().addPlayer(uuid);
                        player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§7" + offlinePlayer.getName() + " §ewurde hinzugefügt.");
                    });
                    return true;
                }

                if (Main.getInstance().getFileManager().getMaintenanceFile().getPlayers().contains(target.getUniqueId().toString())) {
                    player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§cDer Spieler wurde breits hinzugefügt.");
                    return true;
                }

                Main.getInstance().getFileManager().getMaintenanceFile().addPlayer(target.getUniqueId());
                player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§7" + target.getName() + " §ewurde hinzugefügt.");
                return true;
            }

            if (args[0].equalsIgnoreCase("disallow")) {
                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde nicht gefunden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde nicht gefunden.");
                            return;
                        }

                        if (!Main.getInstance().getFileManager().getMaintenanceFile().getPlayers().contains(uuid.toString())) {
                            player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§cDer Spieler wurde nicht hinzugefügt.");
                            return;
                        }


                        Main.getInstance().getFileManager().getMaintenanceFile().removePlayer(uuid);
                        player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§7" + offlinePlayer.getName() + " §ewurde entfernt.");
                    });
                    return true;
                }

                if (!Main.getInstance().getFileManager().getMaintenanceFile().getPlayers().contains(target.getUniqueId().toString())) {
                    player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§cDer Spieler wurde nicht hinzugefügt.");
                    return true;
                }

                Main.getInstance().getFileManager().getMaintenanceFile().removePlayer(target.getUniqueId());
                player.sendMessage(StringDefaults.MAINTENANCE_PREFIX + "§7" + target.getName() + " §ewurde entfernt.");
                return true;
            }

            sendHelp(player, label);
            return true;
        }

        sendHelp(player, label);
        return true;
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " §c<on|off>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " §c<allow|disallow> <Spieler>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " §clist");
    }
}
