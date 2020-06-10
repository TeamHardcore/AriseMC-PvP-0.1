/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.warp;

import com.google.common.base.Charsets;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Home;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserHomes;
import de.teamhardcore.pvp.utils.DateFormats;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.charset.CharsetEncoder;
import java.util.*;

public class CommandHome implements CommandExecutor {

    private final CharsetEncoder encoder = Charsets.ISO_8859_1.newEncoder();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (label.equalsIgnoreCase("home")) {
            if (args.length == 0 || args.length > 2) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Name> " + (player.hasPermission("arisemc.home.other") ? "[Spieler]" : ""));
                return true;
            }

            String homeName = args[0];

            if (args.length == 2) {
                if (!player.hasPermission("arisemc.home.other")) {
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Name> " + (player.hasPermission("arisemc.home.other") ? "[Spieler]" : ""));
                    return true;
                }

                Player targetOnline = Bukkit.getPlayer(args[1]);

                if (targetOnline != null) {
                    User tUser = Main.getInstance().getUserManager().getUser(targetOnline.getUniqueId());
                    UserHomes tHomes = tUser.getUserHomes();
                    teleportToHome(player, targetOnline.getName(), homeName, tHomes);
                    return true;
                }

                UUIDFetcher.getUUID(args[1], uuid -> {
                    if (uuid == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler war noch nie auf diesem Server.");
                        return;
                    }

                    User user = new User(uuid);
                    teleportToHome(player, args[1], homeName, user.getUserHomes());
                });

            }

            User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
            UserHomes homes = user.getUserHomes();
            teleportToHome(player, player.getName(), homeName, homes);
        }

        if (label.equalsIgnoreCase("homes")) {
            if (args.length > 1) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + (player.hasPermission("arisemc.home.other") ? " [Spieler]" : ""));
                return true;
            }

            if (args.length == 1) {
                Player targetOnline = Bukkit.getPlayer(args[0]);

                if (targetOnline != null) {
                    User tUser = Main.getInstance().getUserManager().getUser(targetOnline.getUniqueId());
                    UserHomes tHomes = tUser.getUserHomes();
                    sendHomeList(player, targetOnline.getName(), tHomes);
                    return true;
                }

                UUIDFetcher.getUUID(args[0], uuid -> {
                    if (uuid == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler war noch nie auf diesem Server.");
                        return;
                    }

                    User user = new User(uuid);
                    sendHomeList(player, args[0], user.getUserHomes());
                });
                return true;
            }

            User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
            sendHomeList(player, player.getName(), user.getUserHomes());
        }

        if (label.equalsIgnoreCase("sethome")) {
            if (args.length != 1) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Spieler>");
                return true;
            }

            User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());

            String homeName = args[0].toLowerCase();
            Home home = user.getUserHomes().getHome(homeName);

            if (home != null) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu besitzt bereits ein Home mit dem Namen §7" + args[0] + "§c.");
                return true;
            }

            if (user.getHomeLimit() != -1 && user.getUserHomes().getHomes().size() >= user.getHomeLimit()) {
                player.sendMessage(user.getUserHomes().getHomes().size() + "/" + user.getHomeLimit());
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast bereits die maximale Anzahl an Homes erreicht.");
                return true;
            }

            if (homeName.matches("[^\\\\dA-Za-z0-9]")) {
                player.sendMessage(StringDefaults.PREFIX + "§cBitte verwende keine Sonderzeichen.");
                return true;
            }

            if (!this.encoder.canEncode(homeName)) {
                player.sendMessage(StringDefaults.PREFIX + "§cDer Name enthält unerlaubte Sonderzeichen.");
                return true;
            }

            if (homeName.length() > 20) {
                player.sendMessage(StringDefaults.PREFIX + "§cDer Name des Homes darf nicht länger als 20 Zeichen sein.");
                return true;
            }

            Location playerLocation = player.getLocation();
            Location homeLocation = new Location(player.getWorld(), playerLocation.getBlockX() + 0.5D, playerLocation.getBlockY(), playerLocation.getBlockZ() + 0.5D, playerLocation.getYaw(), playerLocation.getPitch());
            user.getUserHomes().addHome(homeName, homeLocation, true);
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Home §7" + homeName + " §eerstellt.");
        }

        if (label.equalsIgnoreCase("delhome")) {
            if (args.length != 1) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Spieler>");
                return true;
            }

            User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());

            String homeName = args[0].toLowerCase();
            Home home = user.getUserHomes().getHome(homeName);

            if (home == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu besitzt keinen Home mit dem Namen §7" + args[0] + "§c.");
                return true;
            }

            user.getUserHomes().removeHome(homeName, true);
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Home §7" + homeName + " §egelöscht.");
        }

        return true;
    }

    private void teleportToHome(Player player, String target, String name, UserHomes homes) {
        boolean self = player.getUniqueId().equals(homes.getUser().getUuid());
        Home home = homes.getHome(name);

        if (home == null) {
            player.sendMessage(StringDefaults.PREFIX + "§cDer Home existiert§7" + (self ? "" : (" §cbei §7" + target)) + " §cnicht.");
            return;
        }

        if (!homes.isReady()) {
            player.sendMessage(StringDefaults.PREFIX + "§cBitte warte einen Moment, während die Homes geladen werden...");
            return;
        }

        player.teleport(home.getLocation());
        if (self) {
            home.setLastTeleportDate(System.currentTimeMillis());
            homes.updateLastTeleportTime(name, true);
        }
        player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest zum Home §7" + home.getName() + (self ? "" : (" §evon §7" + target)) + " §eteleportiert.");
    }

    private void sendHomeList(Player player, String target, UserHomes userHomes) {
        boolean self = player.getUniqueId().equals(userHomes.getUser().getUuid());

        if (!userHomes.isReady()) {
            player.sendMessage(StringDefaults.PREFIX + "§cBitte warte einen Moment, während die Homes geladen werden...");
            return;
        }

        Map<String, Home> homes = userHomes.getHomes();
        if (homes == null || homes.isEmpty()) {
            player.sendMessage(StringDefaults.PREFIX + "§c" + (self ? "Du" : ("§7" + target)) + " §cbesitzt keine Homes.");
            return;
        }

        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lHOMES §8§l§m*-*-*-*-*-*-*-*-*");
        player.sendMessage(" ");
        player.sendMessage("§cAlle Homes§8:");
        player.sendMessage(" ");

        Iterator<Map.Entry<String, Home>> iterator = homes.entrySet().iterator();
        JSONMessage message = new JSONMessage("");
        while (iterator.hasNext()) {
            Map.Entry<String, Home> entry = iterator.next();
            String homeName = entry.getKey();
            Home home = entry.getValue();

            ArrayList<String> tooltip = new ArrayList<>();
            tooltip.add("§eTeleportiere dich zum Home §7" + homeName + (self ? "" : " §evon §7" + target));

            if (player.hasPermission("arisemc.home.other")) {
                tooltip.add(" ");
                tooltip.add("§c§lWeitere Informationen§8:");
                tooltip.add(" §8● §cErstellt am§8: §7" + DateFormats.FORMAT_HOME.format(new Date(home.getCreationDate())));
                tooltip.add(" §8● §cLetzter Teleport§8: §7" + ((home.getLastTeleportDate() == -1 ? "-" : DateFormats.FORMAT_HOME.format(new Date(home.getLastTeleportDate())))));
            }

            message.then("§7" + homeName).tooltip(tooltip).runCommand("/home " + homeName + (self ? "" : " " + target));
            if (iterator.hasNext()) {
                message.then("§e, ");
            }
        }
        message.send(player);
        player.sendMessage(" ");
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lHOMES §8§l§m*-*-*-*-*-*-*-*-*");

    }

}
