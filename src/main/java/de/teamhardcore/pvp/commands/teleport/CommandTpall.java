/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.teleport;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpall implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.tpall")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            if (all == player) continue;
            all.teleport(player);
            all.sendMessage(StringDefaults.PREFIX + "§eDu wurdest zu §7" + player.getName() + " §eteleportiert.");
        }

        player.sendMessage(StringDefaults.PREFIX + "§eAlle Spieler wurden zu dir teleportiert.");
        return true;
    }
}
