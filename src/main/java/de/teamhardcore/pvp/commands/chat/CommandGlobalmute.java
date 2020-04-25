/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.chat;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGlobalmute implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.globalmute")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 1) {
            //globalmute <1-2>


        }

        return true;
    }
}
