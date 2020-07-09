/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.punishment;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.punishment.ArchiveInformation;
import de.teamhardcore.pvp.model.punishment.PunishmentInformation;
import de.teamhardcore.pvp.model.punishment.PunishmentType;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CommandPunishment implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;
        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.punishment.warn")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length < 2) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("unban")) {

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        if (!offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }

                        if (!Main.getInstance().getPunishmentManager().hasPunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN)) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler ist nicht gebannt.");
                            return;
                        }

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.UNBAN);
                            Bukkit.broadcastMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurden von §4" + player.getName() + " §centbannt.");
                            player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurden entbannt.");
                        });
                    });
                    return true;
                }

                if (!Main.getInstance().getPunishmentManager().hasPunishment(target.getUniqueId(), PunishmentType.BAN)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler ist nicht gebannt.");
                    return true;
                }

                if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                    return true;
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.UNBAN);
                    Bukkit.broadcastMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurden von §4" + player.getName() + " §centbannt.");
                    player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurden entbannt.");
                });
            }

            if (args[0].equalsIgnoreCase("unmute")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        if (!offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }

                        if (!Main.getInstance().getPunishmentManager().hasPunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE)) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler ist nicht gemutet.");
                            return;
                        }

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.UNMUTE);
                            Bukkit.broadcastMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurden von §4" + player.getName() + " §centmutet.");
                            player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurden entmutet.");
                        });
                    });
                    return true;
                }

                if (!Main.getInstance().getPunishmentManager().hasPunishment(target.getUniqueId(), PunishmentType.MUTE)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler ist nicht gemutet.");
                    return true;
                }

                if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                    return true;
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.UNMUTE);
                    Bukkit.broadcastMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurden von §4" + player.getName() + " §centmutet.");
                    player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurden entmutet.");
                });
            }

            if (args[0].equalsIgnoreCase("history")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }
                        PunishmentInformation current = Main.getInstance().getPunishmentManager().getPunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN);

                        if (current == null)
                            current = Main.getInstance().getPunishmentManager().getPunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE);

                        ArchiveInformation information = Main.getInstance().getPunishmentManager().getArchiveInformation(offlinePlayer.getUniqueId());
                        sendArchiveInformation(player, offlinePlayer.getName(), current, information);
                    });
                    return true;
                }

                PunishmentInformation current = Main.getInstance().getPunishmentManager().getPunishment(target.getUniqueId(), PunishmentType.BAN);

                if (current == null)
                    current = Main.getInstance().getPunishmentManager().getPunishment(target.getUniqueId(), PunishmentType.MUTE);

                ArchiveInformation information = Main.getInstance().getPunishmentManager().getArchiveInformation(target.getUniqueId());
                sendArchiveInformation(player, target.getName(), current, information);
                return true;
            }

            sendHelp(player, label);
            return true;
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("ban")) {
                Player target = Bukkit.getPlayer(args[1]);
                String reason = args[2];


                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        if (!offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.BAN, reason);
                            Bukkit.getOnlinePlayers().forEach(all -> {
                                all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurde von §4" + player.getName() + " §cgebannt.");
                                if (all.hasPermission("arisemc.punishment.notify"))
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                            });
                            player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurden gebannt.");
                        });
                    });
                    return true;
                }

                if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                    return true;
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.BAN, reason);
                    Bukkit.getOnlinePlayers().forEach(all -> {
                        all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurde von §4" + player.getName() + " §cgebannt.");
                        if (all.hasPermission("arisemc.punishment.notify"))
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                    });
                    player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurden gebannt.");
                });

            }

            if (args[0].equalsIgnoreCase("mute")) {
                Player target = Bukkit.getPlayer(args[1]);
                String reason = args[2];

                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        if (!offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.MUTE, reason);
                            Bukkit.getOnlinePlayers().forEach(all -> {
                                all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurde von §4" + player.getName() + " §cgemutet.");
                                if (all.hasPermission("arisemc.punishment.notify"))
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                            });
                            player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurden gemutet.");
                        });
                    });
                    return true;
                }

                if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                    return true;
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.MUTE, reason);
                    Bukkit.getOnlinePlayers().forEach(all -> {
                        all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurde von §4" + player.getName() + " §cgemutet.");
                        if (all.hasPermission("arisemc.punishment.notify"))
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                    });
                    player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurde gemutet.");
                });

            }

            if (args[0].equalsIgnoreCase("history")) {
                if (args[2].equalsIgnoreCase("clear")) {

                    Player target = Bukkit.getPlayer(args[1]);

                    if (target == null) {
                        UUIDFetcher.getUUID(args[1], uuid -> {
                            if (uuid == null) {
                                player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                                return;
                            }

                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                            if (!offlinePlayer.hasPlayedBefore()) {
                                player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                                return;
                            }


                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                Main.getInstance().getPunishmentManager().clearArchive(uuid);
                                player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§7" + offlinePlayer.getName() + "'s §6Archive wurde geleert.");
                            });
                        });
                        return true;
                    }

                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        Main.getInstance().getPunishmentManager().clearArchive(target.getUniqueId());
                        player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§7" + target.getName() + "'s §6Archive wurde geleert.");
                    });
                    return true;
                }

                sendHelp(player, label);
                return true;
            }
            sendHelp(player, label);
            return true;
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("warn")) {
                Player target = Bukkit.getPlayer(args[1]);

                int amount;

                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException ex) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe eine richtige Zahl an.");
                    return true;
                }

                if (args[2].equalsIgnoreCase("remove")) {

                    if (target == null) {
                        UUIDFetcher.getUUID(args[1], uuid -> {
                            if (uuid == null) {
                                player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                                return;
                            }

                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                            if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                                player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                                return;
                            }

                            if (!offlinePlayer.hasPlayedBefore()) {
                                player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                                return;
                            }

                            List<PunishmentInformation> punishments = Main.getInstance().getPunishmentManager().getPunishments(offlinePlayer.getUniqueId(), PunishmentType.WARN);

                            if (punishments.isEmpty()) {
                                player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler wurde noch nicht verwarnt.");
                                return;
                            }

                            if (punishments.size() < amount) {
                                player.sendMessage(StringDefaults.PREFIX + "§cBitte kleinere Zahl wählen. (1 bis " + punishments.size() + ")");
                                return;
                            }

                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.WARN, false, amount, "");
                                Bukkit.broadcastMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurden von §4" + player.getName() + " §4§l" + amount + " " + (amount == 1 ? "Warn" : "Warns") + " §centfernt.");
                                player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurden §7" + amount + " " + (amount == 1 ? "Warn" : "Warns") + " §centfernt.");
                            });
                        });
                        return true;
                    }

                    if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                        return true;
                    }

                    List<PunishmentInformation> punishments = Main.getInstance().getPunishmentManager().getPunishments(target.getUniqueId(), PunishmentType.WARN);

                    if (punishments.isEmpty()) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler wurde noch nicht verwarnt.");
                        return true;
                    }

                    if (punishments.size() < amount) {
                        player.sendMessage(StringDefaults.PREFIX + "§cBitte kleinere Zahl wählen. (1 bis " + punishments.size() + ")");
                        return true;
                    }

                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.WARN, false, amount, "");
                        Bukkit.broadcastMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurden von §4" + player.getName() + " §4§l" + amount + " " + (amount == 1 ? "Warn" : "Warns") + " §centfernt.");
                        player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurden §7" + amount + " " + (amount == 1 ? "Warn" : "Warns") + " §centfernt.");
                    });
                }

                sendHelp(player, label);
                return true;
            }

            if (args[0].equalsIgnoreCase("tempban")) {
                Player target = Bukkit.getPlayer(args[1]);
                String reason = args[2];
                long banTime = TimeUtil.parseTime(args[3]);

                if (banTime == -1) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBitte richtiges Zeitformat wählen.");
                    return true;
                }

                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (!offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.BAN, banTime, reason);
                            Bukkit.getOnlinePlayers().forEach(all -> {
                                all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurde von §4" + player.getName() + " §cgebannt.");
                                if (all.hasPermission("arisemc.punishment.notify")) {
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Zeit§8: §c" + TimeUtil.timeToString(banTime));
                                }
                            });
                            player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurden gebannt.");
                        });
                    });
                    return true;
                }

                if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                    return true;
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.BAN, banTime, reason);
                    Bukkit.getOnlinePlayers().forEach(all -> {
                        all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurde von §4" + player.getName() + " §cgebannt.");
                        if (all.hasPermission("arisemc.punishment.notify")) {
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Zeit§8: §c" + TimeUtil.timeToString(banTime));
                        }
                    });
                    player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurden gebannt.");
                });

            }

            if (args[0].equalsIgnoreCase("mute")) {
                Player target = Bukkit.getPlayer(args[1]);
                String reason = args[2];
                long muteTime = TimeUtil.parseTime(args[3]);

                if (muteTime == -1) {
                    player.performCommand("/punishment mute " + args[1] + " " + reason);
                    return true;
                }

                if (target == null) {
                    UUIDFetcher.getUUID(args[1], uuid -> {
                        if (uuid == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                            return;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                        if (!offlinePlayer.hasPlayedBefore()) {
                            player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                            return;
                        }

                        if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                            return;
                        }

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.MUTE, muteTime, reason);
                            Bukkit.getOnlinePlayers().forEach(all -> {
                                all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurde von §4" + player.getName() + " §cgemutet.");
                                if (all.hasPermission("arisemc.punishment.notify")) {
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Zeit§8: §c" + TimeUtil.timeToString(muteTime));
                                }
                            });
                            player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurde gemutet.");
                        });
                    });
                    return true;
                }

                if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                    return true;
                }

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.MUTE, muteTime, reason);
                    Bukkit.getOnlinePlayers().forEach(all -> {
                        all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurde von §4" + player.getName() + " §cgemutet.");
                        if (all.hasPermission("arisemc.punishment.notify")) {
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + reason);
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Zeit§8: §c" + TimeUtil.timeToString(muteTime));
                        }
                    });
                    player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurde gemutet.");
                });
                return true;
            }

            sendHelp(player, label);
        }

        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("warn")) {
                Player target = Bukkit.getPlayer(args[1]);

                int amount;

                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException ex) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe eine richtige Zahl an.");
                    return true;
                }

                if (args[2].equalsIgnoreCase("add")) {

                    if (target == null) {
                        UUIDFetcher.getUUID(args[1], uuid -> {
                            if (uuid == null) {
                                player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §ckonnte nicht gefunden werden.");
                                return;
                            }

                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                            if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                                player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                                return;
                            }

                            if (!offlinePlayer.hasPlayedBefore()) {
                                player.sendMessage(StringDefaults.PREFIX + "§c" + args[1] + " §cwar noch nie auf dem Server.");
                                return;
                            }

                            if (offlinePlayer.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                                player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                                return;
                            }

                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                Main.getInstance().getPunishmentManager().punishPlayer(offlinePlayer, player, PunishmentType.WARN, true, amount, args[4]);
                                Bukkit.getOnlinePlayers().forEach(all -> {
                                    all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + offlinePlayer.getName() + " §cwurde von §4" + player.getName() + " §4§l" + amount + " §cMal verwarnt.");
                                    if (all.hasPermission("arisemc.punishment.notify"))
                                        all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + args[4]);
                                });
                                player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + offlinePlayer.getName() + " §cwurde §7" + amount + " §cMal verwarnt.");
                            });
                        });
                        return true;
                    }

                    if (target.getUniqueId().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gebannt werden.");
                        return true;
                    }

                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        Main.getInstance().getPunishmentManager().punishPlayer(target, player, PunishmentType.WARN, true, amount, args[4]);
                        Bukkit.getOnlinePlayers().forEach(all -> {
                            all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4" + target.getName() + " §cwurde von §4" + player.getName() + " §4§l" + amount + " §cMal verwarnt.");
                            if (all.hasPermission("arisemc.punishment.notify"))
                                all.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§4Grund§8: §c" + args[4]);
                        });
                        player.sendMessage(StringDefaults.PUNISHMENT_PREFIX + "§6" + target.getName() + " §cwurde §7" + amount + " §cMal verwarnt.");
                    });
                    return true;
                }

                sendHelp(player, label);
                return true;
            }
            sendHelp(player, label);
        }


        return true;
    }

    private void sendArchiveInformation(Player player, String forWhom, PunishmentInformation currentPunishment, ArchiveInformation archive) {
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lHISTORY §8§l§m*-*-*-*-*-*-*-*-*");
        player.sendMessage(StringDefaults.PREFIX + "§cHistory von §4" + forWhom + "§c:");
        player.sendMessage("");
        player.sendMessage(StringDefaults.PREFIX + "§cAktuell§8: ");
        if (currentPunishment == null || currentPunishment.getType() == PunishmentType.UNBAN || currentPunishment.getType() == PunishmentType.UNMUTE || currentPunishment.getType() == PunishmentType.WARN)
            player.sendMessage("§7- kein aktueller Eintrag.");
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy 'um' HH:mm:ss", Locale.GERMAN);
            String when = simpleDateFormat.format(currentPunishment.getCreated());
            String whenStop = simpleDateFormat.format(new Date(currentPunishment.getCreated() + currentPunishment.getTimestamp()));
            new JSONMessage("§8- §7Typ§8: §4§l" + currentPunishment.getType().name() + "§7 §8(§e" + when + "§8)")
                    .tooltip("§7Von§8: §e" + currentPunishment.getBy(),
                            "§7Art§8: " + (currentPunishment.isPermanent() ? "§4§lPermanent" : "§c§lZeitlich"),
                            "§7Grund§8: §e" + currentPunishment.getReason(),
                            "§7Länge§8: §e" + (currentPunishment.isPermanent() ? "-" : TimeUtil.timeToString(currentPunishment.getTimestamp())),
                            "§7Ablauf§8: §e" + (currentPunishment.isPermanent() ? "-" : whenStop)).send(player);
        }
        player.sendMessage("");
        player.sendMessage(StringDefaults.PREFIX + "§cArchive:");
        if (archive == null || archive.getArchivedPunishments().isEmpty()) {
            player.sendMessage("§7- keine archivierten Einträge");
        } else {
            for (PunishmentInformation information : archive.getArchivedPunishments()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy 'um' HH:mm:ss", Locale.GERMAN);
                String when = simpleDateFormat.format(information.getCreated());
                String whenStop = simpleDateFormat.format(new Date(information.getCreated() + information.getTimestamp()));
                new JSONMessage("§8- §7Typ§8: §4§l" + information.getType().name() + "§7 §8(§e" + when + "§8)")
                        .tooltip("§7Von§8: §e" + information.getBy(),
                                "§7Art§8: " + (information.isPermanent() ? "§4§lPermanent" : "§c§lZeitlich"),
                                "§7Grund§8: §e" + information.getReason(),
                                "§7Länge§8: §e" + (information.isPermanent() ? "-" : TimeUtil.timeToString(information.getTimestamp())),
                                "§7Ablauf§8: §e" + (information.isPermanent() ? "-" : whenStop)).send(player);
            }
        }
        player.sendMessage("");
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lHISTORY §8§l§m*-*-*-*-*-*-*-*-*");
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " warn <Spieler> add <Anzahl> <Grund>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " warn <Spieler> remove <Anzahl> ");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " ban <Spieler> <Grund>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8:  §7/" + label + " tempban <Spieler> <Grund> <Zeit>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " unban <Spieler> ");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8:  §7/" + label + " mute <Spieler> <Grund> [Zeit]");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8:  §7/" + label + " unmute <Spieler> ");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8:  §7/" + label + " history <Spieler> [clear] ");
    }

}
