/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.chat;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcast implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.broadcast")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/broadcast <Nachricht>");
            return true;
        }

        StringBuilder builder = new StringBuilder();

        for (String arg : args)
            builder.append(ChatColor.translateAlternateColorCodes('&', arg)).append(" ");

        String message = builder.substring(0, builder.length() - 1);

        Bukkit.broadcastMessage(StringDefaults.GLOBAL_PREFIX + message);
        return true;
    }
}
