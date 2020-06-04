/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.pvp;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.DuelInventory;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.arena.DuelArenaType;
import de.teamhardcore.pvp.model.duel.configuration.DuelDeployment;
import de.teamhardcore.pvp.model.duel.configuration.DuelSettings;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandDuel implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (args.length == 0) {
            DuelConfiguration configuration = new DuelConfiguration(player.getLocation(), new DuelSettings(), new DuelDeployment());
            configuration.getPlayers().add(player);
            Main.getInstance().getDuelManager().getConfigurationCache().remove(player.getUniqueId());
            Main.getInstance().getDuelManager().getConfigurationCache().put(player.getUniqueId(), configuration);
            DuelInventory.openDuelRequestInventory(player, true, configuration);
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("abbrechen")) {
                if (!Main.getInstance().getDuelManager().getConfigurationCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast noch kein Duell erstellt.");
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(player.getUniqueId());

                for (Player targets : configuration.getPlayers()) {
                    if (targets != null && targets.isOnline() && targets != player) {
                        targets.sendMessage(StringDefaults.DUEL_PREFIX + "§cDas Duell wurde abgebrochen.");
                    }
                }

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast das Duell abgebrochen.");
                Main.getInstance().getDuelManager().getConfigurationCache().remove(player.getUniqueId());
            } else if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                Set<String> runningDuels = new HashSet<>(Main.getInstance().getDuelManager().getGameIdCache());

                if (runningDuels.isEmpty()) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cEs existieren keine laufenden Duelle.");
                    return true;
                }

                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/duel admin <GameID> <Operating> §7§o(Klick auf eine GameID)");
                player.sendMessage(" ");
                for (String gameID : runningDuels) {
                    Duel duel = Main.getInstance().getDuelManager().getDuel(gameID);
                    if (duel == null) continue;

                    String duelLocation = "X: " + (int) duel.getConfiguration().getLocation().getX() + ", Y: " + (int) duel.getConfiguration().getLocation().getY() + ", Z: " + (int) duel.getConfiguration().getLocation().getZ();
                    String duelPlayers = duel.getPlayer().getName() + " vs. " + duel.getTarget().getName();

                    new JSONMessage("§8■ §6§l" + duelPlayers).tooltip("§c§lPosition§8: §7" + duelLocation + "\n" + "§c§lGameID§8: §7" + gameID)
                            .suggestCommand("/duel admin " + gameID + " ").send(player);
                }
                player.sendMessage(" ");
                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                if (!Main.getInstance().getDuelManager().getConfigurationCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast noch kein Duell erstellt.");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

            /*    if (target == player) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu kannst dich nicht selbst herausfordern.");
                    return true;
                }*/


                double distance = player.getLocation().distanceSquared(target.getLocation());

                if (distance > 5) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu musst dich näher an §7" + target.getName() + " §cbefinden.");
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(player.getUniqueId());

                if (configuration.getPlayers().size() >= 2) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast bereits einen Spieler herausgefordert.");
                    return true;
                }

                if (configuration.getInvites().contains(target)) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast diesen Spieler bereits herausgefordert.");
                    return true;
                }

                configuration.getInvites().add(target);

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast §7" + target.getName() + " §ezu einem Duell herausgefordert.");
                target.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                target.sendMessage(StringDefaults.PREFIX + "§eDu wurdest von §7" + player.getName() + " §eherausgefordert.");
                target.sendMessage(" ");
                target.sendMessage(StringDefaults.PREFIX + "§6§lEinstellungen§8: ");
                target.sendMessage(" §8● §eGoldene Äpfel§8: " + (configuration.getSettings().canUseGoldenApple() ? "§a§laktiviert" : "§c§ldeaktiviert"));
                target.sendMessage(" §8● §eHeiltränke§8: " + (configuration.getSettings().getMaxHealStacks() == -1 ? "§a§lUnbegrenz" : "§a§l" + configuration.getSettings().getMaxHealStacks() + " Stacks"));
                target.sendMessage(" ");
                if (configuration.getDeployment().getCoins() != 0) {
                    target.sendMessage(StringDefaults.PREFIX + "§6§lGewinn§8:");
                    target.sendMessage(" §8● §eEinsatz§8: §a§l" + (Util.formatNumber(configuration.getDeployment().getCoins()) + "$"));
                    target.sendMessage(" ");
                }
                new JSONMessage(StringDefaults.PREFIX + "§eKlicke hier§7, §eum die Herausforderung anzunehmen.").runCommand("/duel accept " + player.getName()).tooltip("§eHerausforderung annehmen").send(target);
                target.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            } else if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("annehmen")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                double distance = player.getLocation().distanceSquared(target.getLocation());

                if (distance > 5) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu musst dich näher an §7" + target.getName() + " §cbefinden.");
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(target.getUniqueId());
                if (configuration == null) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu wurdest von diesem Spieler zu keinem Duell herausgefordert.");
                    return true;
                }

                if (!configuration.getInvites().contains(player)) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu wurdest von diesem Spieler zu keinem Duell herausgefordert.");
                    return true;
                }

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Duellanfrage akzeptiert.");
                target.sendMessage(StringDefaults.DUEL_PREFIX + "§eDer Spieler §7" + player.getName() + " §ehat die Duellanfrage akzeptiert.");

                configuration.getInvites().remove(player);
                configuration.getPlayers().add(player);

                if (configuration.getLocation() != target.getLocation())
                    configuration.setLocation(player.getLocation());


                Main.getInstance().getDuelManager().getConfigurationCache().remove(player.getUniqueId());
                Main.getInstance().getDuelManager().createDuel(configuration);
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                String gameID = args[1];
                Duel duel = Main.getInstance().getDuelManager().getDuel(gameID);

                if (duel == null) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie GameID ist ungültig!");
                    return true;
                }

                String duelLocation = "X: " + (int) duel.getConfiguration().getLocation().getX() + ", Y: " + (int) duel.getConfiguration().getLocation().getY() + ", Z: " + (int) duel.getConfiguration().getLocation().getZ();
                String duelPlayers = duel.getPlayer().getName() + ", " + duel.getTarget().getName();

                if (args[2].equalsIgnoreCase("stop")) {
                    duel.startEndTask();
                    duel.sendMessage(StringDefaults.DUEL_PREFIX + "§cDas Duell wurde von einem Admin gestoppt.");
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast ein Duell gestoppt.");
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cGameID§8: §7" + duel.getGameID());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

                    Bukkit.getOnlinePlayers().forEach(players -> {
                        if (players.hasPermission("arisemc.duel.admin") && player != players) {
                            players.sendMessage(StringDefaults.DUEL_PREFIX + "§7" + player.getName() + " §chat ein Duell beendet.");
                            players.sendMessage(StringDefaults.DUEL_PREFIX + "§cGameID§8: §7" + duel.getGameID());
                            players.sendMessage(StringDefaults.DUEL_PREFIX + "§cSpieler§8: §7" + duelPlayers);
                        }
                    });

                } else if (args[2].equalsIgnoreCase("info")) {

                    player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                    player.sendMessage(" ");
                    player.sendMessage(StringDefaults.PREFIX + "§cGameID§8: §7" + duel.getGameID());
                    player.sendMessage(" §8● §cPosition§8: §7" + duelLocation);
                    player.sendMessage(" §8● §cSpieler§8: §7" + duelPlayers);
                    player.sendMessage(" ");
                    player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                } else if (args[2].equalsIgnoreCase("teleport")) {
                    player.teleport(duel.getConfiguration().getLocation());
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu wurdest zur Duell Location teleportiert.");
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                }

            }
        }

        return true;
    }
}
