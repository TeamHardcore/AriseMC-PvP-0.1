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

public class CommandTphere implements CommandExecutor {
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.teleport")) {
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
            player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dich nicht zu dir selbst teleportieren.");
            return true;
        }

        target.teleport(player.getLocation());
        target.sendMessage(StringDefaults.PREFIX + "§eDu wurdest zu §7" + player.getName() + " §eteleportiert.");
        player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + target.getName() + " §ezu dir teleportiert.");
        return true;
    }
}
