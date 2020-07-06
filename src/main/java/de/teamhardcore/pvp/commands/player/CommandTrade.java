/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandTrade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (args.length < 1 || args.length > 2) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu kannst nicht mit dir selber handeln.");
                return true;
            }

            if (Main.getInstance().getTradeManager().getPlayerTrades().containsKey(target)) {
                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDer Spieler handelt bereits mit jemandem.");
                return true;
            }

            List<UUID> requests = Main.getInstance().getTradeManager().getRequests().getOrDefault(player, new ArrayList<>());

            if (requests.contains(target.getUniqueId())) {
                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu hast bereits eine Handelsanfrage an §7" + target.getName() + " §cgesendet.");
                return true;
            }

            requests.add(target.getUniqueId());
            Main.getInstance().getTradeManager().getRequests().put(player, requests);

            player.sendMessage(StringDefaults.TRADE_PREFIX + "§6Du hast eine Handelsanfrage an §7" + target.getName() + " §6gesendet.");
            target.sendMessage(StringDefaults.TRADE_PREFIX + "§6Du hast eine Handelsanfrage von §7" + player.getName() + " §6erhalten.");
            new JSONMessage(StringDefaults.TRADE_PREFIX + "§a§lAnnehmen ")
                    .runCommand("/trade accept " + player.getName()).tooltip("§aHandelsanfrage annehmen")
                    .then(" §8§l/ ")
                    .then("§c§lAblehnen").runCommand("/trade deny " + player.getName()).tooltip("§cHandelsanfrage ablehnen").send(target);
        }

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("accept")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                if (target == player) {
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu kannst nicht mit dir selber handeln.");
                    return true;
                }

                if (Main.getInstance().getTradeManager().getPlayerTrades().containsKey(player)) {
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu handelst bereits mit jemandem.");
                    return true;
                }

                List<UUID> targetRequests = Main.getInstance().getTradeManager().getRequests().getOrDefault(target, null);

                if (targetRequests == null || !targetRequests.contains(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§7" + target.getName() + " §cmöchte nicht mit dir handeln.");
                    return true;
                }

                if (Main.getInstance().getTradeManager().getPlayerTrades().containsKey(target)) {
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§7" + target.getName() + " §chandelt bereits mit jemandem.");
                    return true;
                }

                for (Player all : Main.getInstance().getTradeManager().getRequests().keySet()) {
                    List<UUID> requests = Main.getInstance().getTradeManager().getRequests().get(all);
                    requests.remove(player.getUniqueId());
                }

                Main.getInstance().getTradeManager().getRequests().remove(target);
                Main.getInstance().getTradeManager().createTrade(player, target);

                player.sendMessage(StringDefaults.TRADE_PREFIX + "§6Du bist nun mit §7" + target.getName() + " §6im Handel.");
                target.sendMessage(StringDefaults.TRADE_PREFIX + "§6Du bist nun mit §7" + player.getName() + " §6im Handel.");


                return true;
            }

            if (args[0].equalsIgnoreCase("deny")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                if (target == player) {
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu kannst dich nicht selber ablehnen.");
                    return true;
                }

                List<UUID> requests = Main.getInstance().getTradeManager().getRequests().getOrDefault(target, null);

                if (requests == null || !requests.contains(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.TRADE_PREFIX + "§7" + target.getName() + " §cmöchte nicht dir handeln.");
                    return true;
                }

                requests.remove(player.getUniqueId());

                player.sendMessage(StringDefaults.TRADE_PREFIX + "§cDu hast die Handelsanfrage von §7" + target.getName() + " §cabgelehnt§6.");
                target.sendMessage(StringDefaults.TRADE_PREFIX + "§cDie Handelsanfrage an §7" + player.getName() + " §cwurde §cabgelehnt§6.");
                return true;
            }
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7" + label + " <Spieler>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7" + label + " <accept|deny> <Spieler>");
    }

}
