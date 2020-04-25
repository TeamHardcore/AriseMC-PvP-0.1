/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.teleport;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.model.teleport.TPRequest;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpa implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (label.equalsIgnoreCase("tpa")) {
            if (!player.hasPermission("arisemc.tpa")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Spieler>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dir selber keine Anfrage schicken.");
                return true;
            }

            if (Main.getInstance().getManager().getTeleportCooldowns().containsKey(player)) {
                long diff = Main.getInstance().getManager().getTeleportCooldowns().get(player) - System.currentTimeMillis();
                if (diff / 1000L > 0L) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBitte warte einen Moment, bevor du eine weitere Teleportanfrage sendest.");
                    return true;
                }
                return true;
            }

            if (Main.getInstance().getManager().getTeleportRequests().containsKey(target)) {
                TPRequest tpRequest = Main.getInstance().getManager().getTeleportRequests().get(target);
                if (tpRequest.getPlayer() == player && !tpRequest.isHere()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs läuft bereits eine Teleportanfrage.");
                    return true;
                }
            }

            TPRequest tpRequest = new TPRequest(player, target, false);
            Main.getInstance().getManager().getTeleportCooldowns().put(player, System.currentTimeMillis() + 30000L);
            Main.getInstance().getManager().getTeleportRequests().put(target, tpRequest);

            player.sendMessage(StringDefaults.PREFIX + "§eDu hast eine Teleportanfrage an §7" + target.getName() + " §egeschickt.");
            target.sendMessage(StringDefaults.PREFIX + "§7" + player.getName() + " §emöchte sich zu dir teleportieren.");
            new JSONMessage(StringDefaults.PREFIX + "§eKlicke hier§7, §eum die Teleportanfrage anzunehmen.").tooltip("§eTeleportanfrage annehmen").runCommand("/tpaccept").send(target);

        }

        if (label.equalsIgnoreCase("tpahere")) {
            if (!player.hasPermission("arisemc.tpahere")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Spieler>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dir selber keine Anfrage schicken.");
                return true;
            }

            if (Main.getInstance().getManager().getTeleportCooldowns().containsKey(player)) {
                long diff = Main.getInstance().getManager().getTeleportCooldowns().get(player) - System.currentTimeMillis();
                if (diff / 1000L > 0L) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBitte warte einen Moment, bevor du eine weitere Teleportanfrage sendest.");
                    return true;
                }
                return true;
            }

            if (Main.getInstance().getManager().getTeleportRequests().containsKey(target)) {
                TPRequest tpRequest = Main.getInstance().getManager().getTeleportRequests().get(target);
                if (tpRequest.getPlayer() == player && tpRequest.isHere()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs läuft bereits eine Teleportanfrage.");
                    return true;
                }
            }

            TPRequest tpRequest = new TPRequest(player, target, true);
            Main.getInstance().getManager().getTeleportCooldowns().put(player, System.currentTimeMillis() + 30000L);
            Main.getInstance().getManager().getTeleportRequests().put(target, tpRequest);

            player.sendMessage(StringDefaults.PREFIX + "§eDu hast eine Teleportanfrage an §7" + target.getName() + " §egeschickt.");
            target.sendMessage(StringDefaults.PREFIX + "§7" + player.getName() + " §emöchte dich zu ihm teleportieren.");
            new JSONMessage(StringDefaults.PREFIX + "§eKlicke hier§7, §eum die Teleportanfrage anzunehmen.").tooltip("§eTeleportanfrage annehmen").runCommand("/tpaccept").send(target);

        }

        if (label.equalsIgnoreCase("tpaall")) {
            if (!player.hasPermission("arisemc.tpaall")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            int requests = 0;

            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all == player) continue;

                if (Main.getInstance().getManager().getTeleportRequests().containsKey(all)) {
                    TPRequest tpRequest = Main.getInstance().getManager().getTeleportRequests().get(all);
                    if (tpRequest.getPlayer() == player && tpRequest.isHere()) continue;
                }

                TPRequest tpRequest = new TPRequest(player, all, true);
                Main.getInstance().getManager().getTeleportRequests().put(all, tpRequest);

                all.sendMessage(StringDefaults.PREFIX + "§7" + player.getName() + " §emöchte dich zu ihm teleportieren.");
                new JSONMessage(StringDefaults.PREFIX + "§eKlicke hier§7, §eum die Teleportanfrage anzunehmen.").tooltip("§eTeleportanfrage annehmen").runCommand("/tpaccept").send(all);
                requests++;
            }

            player.sendMessage((requests == 0 ? "§cEs wurden keine Anfragen verschickt." : "§eEs wurden §7" + requests + " §eTeleportanfragen verschickt."));
        }

        if (label.equalsIgnoreCase("tpaccept")) {

            if (!Main.getInstance().getManager().getTeleportRequests().containsKey(player)) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast keine Teleportanfrage erhalten.");
                return true;
            }

            TPRequest tpRequest = Main.getInstance().getManager().getTeleportRequests().get(player);
            long sent = System.currentTimeMillis() - tpRequest.getSent();

            if (sent / 1000L > 30L) {
                player.sendMessage(StringDefaults.PREFIX + "§cDie Teleportanfrage ist abgelaufen.");
                Main.getInstance().getManager().getTeleportRequests().remove(player);
                return true;
            }

            Player requester = tpRequest.getPlayer();

            player.sendMessage(StringDefaults.PREFIX + "§eDu hast die Teleportanfrage angenommen.");
            requester.sendMessage(StringDefaults.PREFIX + "§7" + player.getName() + " §ehat die Teleportanfrage angenommen.");

            Main.getInstance().getManager().getTeleportRequests().remove(player);

            boolean here = tpRequest.isHere();
            Player to = !here ? requester : player; // Typ der Teleportiert wird
            Player target = here ? requester : player;

            if (Main.getInstance().getManager().getTeleportDelays().containsKey(to)) {
                to.sendMessage(StringDefaults.PREFIX + "§cEs läuft bereits ein Teleportvorgang.");
                return true;
            }

            if (to.hasPermission("arisemc.teleport.nodelay")) {
                Main.getInstance().getManager().getLastPositions().put(to, to.getLocation());
                to.teleport(target);
                //todo: play effect
                to.sendMessage(StringDefaults.PREFIX + "§eDu wurdest zu §7" + target.getName() + " §eteleportiert.");
            } else {
                to.sendMessage(StringDefaults.PREFIX + "§eDu wirst in §72 Sekunden §eteleportiert.");

                TPDelay tpDelay = new TPDelay(to, 0, 2) {
                    public boolean onTick() {
                        if (target.isDead() || !target.isOnline()) {
                            Main.getInstance().getManager().getTeleportDelays().remove(getPlayer());
                            getPlayer().sendMessage(StringDefaults.PREFIX + "§cDie Teleportation wurde abgebrochen.");
                            return true;
                        }
                        return false;
                    }


                    public void onEnd() {
                        Main.getInstance().getManager().getLastPositions().put(getPlayer(), getPlayer().getLocation());
                        getPlayer().teleport(target);
                        getPlayer().sendMessage(StringDefaults.PREFIX + "§eDu wurdest zu §7" + target.getName() + " §eteleportiert.");
                    }
                };

                Main.getInstance().getManager().getTeleportDelays().put(to, tpDelay);
            }
        }

        if (label.equalsIgnoreCase("tpdeny")) {

            if (!Main.getInstance().getManager().getTeleportRequests().containsKey(player)) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast keine Teleportanfrage erhalten.");
                return true;
            }

            TPRequest tpRequest = Main.getInstance().getManager().getTeleportRequests().get(player);
            long sent = System.currentTimeMillis() - tpRequest.getSent();

            if (sent / 1000L > 30L) {
                player.sendMessage(StringDefaults.PREFIX + "§cDie Teleportanfrage ist abgelaufen.");
                Main.getInstance().getManager().getTeleportRequests().remove(player);
                return true;
            }

            Player target = tpRequest.getPlayer();

            player.sendMessage(StringDefaults.PREFIX + "§eDu hast die Teleportanfrage abgelehnt.");
            target.sendMessage(StringDefaults.PREFIX + "§7" + player.getName() + " §ehat die Teleportanfrage abgelehnt.");
            Main.getInstance().getManager().getTeleportRequests().remove(player);
        }

        return true;
    }
}
