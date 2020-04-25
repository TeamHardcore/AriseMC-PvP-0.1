/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.help;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Support;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;


public class CommandSupport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (args.length == 0) {
            if (player.hasPermission("arisemc.support")) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/support <Spieler>");
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/support beenden");
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/support list");
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (!player.hasPermission("arisemc.support")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }
                if (Main.getInstance().getSupportManager().getWaitingPlayers().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cGerade benötigt kein Spieler Support.");
                    return true;
                }

                JSONMessage message = new JSONMessage(StringDefaults.PREFIX + "§eFolgende Spieler benötigen Support§8:");

                for (int i = 0; i < Main.getInstance().getSupportManager().getWaitingPlayers().size(); i++) {
                    Player all = Main.getInstance().getSupportManager().getWaitingPlayers().get(i);
                    message.then("§7" + all.getName()).tooltip("§eSupporte §7" + all.getName()).runCommand("/support " + all.getName());

                    if (Main.getInstance().getSupportManager().getWaitingPlayers().size() - 1 < i) continue;

                    if (i >= Main.getInstance().getSupportManager().getWaitingPlayers().size() - 1) continue;
                    message.then("§e, ");
                }

            } else if (args[0].equalsIgnoreCase("close") || args[0].equalsIgnoreCase("beenden")) {
                if (!Main.getInstance().getSupportManager().getSupports().containsKey(player)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu befindest dich in keinem Supportchat.");
                    return true;
                }

                Support support = Main.getInstance().getSupportManager().getSupport(player);
                Support.SupportRole role = support.getRoles().get(player);

                if (role != Support.SupportRole.SUPPORTER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cNur ein Supporter darf den Support schließen.");
                    return true;
                }

                for (Map.Entry<Player, Support.SupportRole> entry : support.getRoles().entrySet()) {
                    Player target = entry.getKey();
                    Support.SupportRole targetRole = entry.getValue();

                    if (targetRole == Support.SupportRole.SUPPORTER) {
                        target.sendMessage(StringDefaults.PREFIX + "§eDer Supportchat wurde beendet.");
                        //todo: add supports done
                    } else {
                        target.sendMessage(StringDefaults.PREFIX + "§eDer Supportchat wurde beendet.");
                        target.sendMessage(StringDefaults.PREFIX + "§eWir hoffen, dir konnte geholfen werden.");
                    }
                }

            } else {
                if (!player.hasPermission("arisemc.support")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }
                Player target = Bukkit.getPlayer(args[0]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                if (target == player) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dich nicht selber supporten.");
                    return true;
                }

                if (!Main.getInstance().getSupportManager().isWaiting(target)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler benötigt keine Hilfe.");
                    return true;
                }

                if (Main.getInstance().getSupportManager().getSupports().containsKey(player)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu befindest dich bereits in einem Supportchat.");
                    return true;
                }

                Main.getInstance().getSupportManager().setWaiting(target, false);
                Main.getInstance().getSupportManager().createSupport(target, player);
                player.sendMessage(StringDefaults.PREFIX + "§eDu befindest dich nun mit §7" + target.getName() + " §eim Supportchat.");
                target.sendMessage(StringDefaults.PREFIX + "§eDu befindest dich nun mit §7" + player.getName() + " §eim Supportchat.");
            }
        }

        return true;
    }
}
