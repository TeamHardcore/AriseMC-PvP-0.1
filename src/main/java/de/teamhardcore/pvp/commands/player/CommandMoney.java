/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class CommandMoney implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (label.equalsIgnoreCase("money")) {

            if (args.length == 2 || args.length > 3) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " [Spieler]");
                if (player.hasPermission("arisemc.money.edit"))
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " <add|remove|set> <Spieler> <Münzen>");
                return true;
            }

            if (args.length == 0) {
                User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
                player.sendMessage(StringDefaults.PREFIX + "§eDeine Münzen§8: §7" + Util.formatNumber(user.getMoney()) + "$");
                return true;
            }

            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                User user = Main.getInstance().getUserManager().getUser(target.getUniqueId());

                player.sendMessage(StringDefaults.PREFIX + "§e" + target.getName() + "'s Münzen§8: §7" + Util.formatNumber(user.getMoney()) + "$");
                return true;
            }

            if (!player.hasPermission("arisemc.money.edit")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("set")) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " [Spieler]");
                if (player.hasPermission("arisemc.money.edit"))
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " <add|remove|set> <Spieler> <Münzen>");
                return true;
            }

            long amount;

            try {
                amount = Long.parseLong(args[2]);
            } catch (NumberFormatException ex) {
                player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe einen richtigen Wert an.");
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (amount < 0L) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Betrag muss mindestens 0$ betragen.");
                    return true;
                }
            } else if (amount <= 0L) {
                player.sendMessage(StringDefaults.PREFIX + "§cDer Betrag muss größer als 0$ sein.");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                UUIDFetcher.getUUID(args[1], uuid -> {
                    if (uuid == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler war noch nie auf diesem Server.");
                        return;
                    }

                    User user = Main.getInstance().getUserManager().getUser(uuid);
                    String name = UUIDFetcher.getName(uuid);

                    if (args[0].equalsIgnoreCase("remove") && user.getMoney() - amount < 0L) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler hat nicht soviel Geld.");
                        return;
                    }

                    if (args[0].equalsIgnoreCase("add")) {
                        user.addMoney(amount);
                        player.sendMessage(StringDefaults.PREFIX + "§eDu hast dem Geld von §7" + name + " " + Util.formatNumber(amount) + "$ §ehinzugefügt.");
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        user.removeMoney(amount);
                        player.sendMessage(StringDefaults.PREFIX + "§eDu hast dem Geld von §7" + name + " " + Util.formatNumber(amount) + "$ §eentfernt.");
                    } else {
                        user.setMoney(amount);
                        player.sendMessage(StringDefaults.PREFIX + "§eDas Geld von §7" + name + " §ewurde auf " + Util.formatNumber(amount) + "$ §egesetzt.");
                    }
                });
                return true;
            }

            User user = Main.getInstance().getUserManager().getUser(target.getUniqueId());

            if (args[0].equalsIgnoreCase("add")) {
                user.addMoney(amount);
                if (target != player)
                    player.sendMessage(StringDefaults.PREFIX + "§eDu hast dem Geld von §7" + target.getName() + " " + Util.formatNumber(amount) + "$ §ehinzugefügt.");
                target.sendMessage(StringDefaults.PREFIX + "§eDeinem Geld wurden §7" + Util.formatNumber(amount) + "$ §ehinzugefügt.");
            } else if (args[0].equalsIgnoreCase("remove")) {
                user.removeMoney(amount);
                if (target != player)
                    player.sendMessage(StringDefaults.PREFIX + "§eDu hast dem Geld von §7" + target.getName() + " " + Util.formatNumber(amount) + "$ §eentfernt.");
                target.sendMessage(StringDefaults.PREFIX + "§eDeinem Geld wurden §7" + Util.formatNumber(amount) + "$ §eentfernt.");
            } else {
                user.setMoney(amount);
                if (target != player)
                    player.sendMessage(StringDefaults.PREFIX + "§eDas Geld von §7" + target.getName() + " §ewurde auf " + Util.formatNumber(amount) + "$ §egesetzt.");
                target.sendMessage(StringDefaults.PREFIX + "§eDein Geld wurde auf §7" + Util.formatNumber(amount) + "$ §egesetzt.");
            }
        }

        if (label.equalsIgnoreCase("pay")) {

            if (args.length != 2) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/pay <Spieler> <Münzen>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dir selber kein Geld senden.");
                return true;
            }

            long amount;

            try {
                amount = Long.parseLong(args[1]);
            } catch (NumberFormatException ex) {
                player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe einen richtigen Wert an.");
                return true;
            }

            if (amount < 1000) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu musst mindestens §71000$ §csenden.");
                return true;
            }

            User pUser = Main.getInstance().getUserManager().getUser(player.getUniqueId());
            User tUser = Main.getInstance().getUserManager().getUser(target.getUniqueId());

            if (pUser.getMoney() < amount) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast nicht soviel Geld.");
                return true;
            }

            pUser.removeMoney(amount);
            tUser.addMoney(amount);


            player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + target.getName() + " §eerfolgreich §7" + Util.formatNumber(amount) + "$ §egesendet.");
            target.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + Util.formatNumber(amount) + "$ §evon §7" + player.getName() + " §eerhalten.");
        }


        return true;
    }
}
