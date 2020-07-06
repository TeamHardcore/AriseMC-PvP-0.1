/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFriend implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        /*
        /friend remove <TeamHardcore>
        /friend add <TeamHardcore>
        /friend list
         */

        /*
        TODO:
         */

        if (args.length < 1 || args.length > 2) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage("todo");
                return true;
            }
            sendHelp(player, label);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {

            return true;
        }

        sendHelp(player, label);
        return true;
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " add <Spieler>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " remove <Spieler>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " list");
    }
}
