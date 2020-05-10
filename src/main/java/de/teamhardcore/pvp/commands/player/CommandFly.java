/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.fly")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/fly [Spieler]");
            return true;
        }

        if (args.length == 0) {
            if (player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                player.sendMessage(StringDefaults.PREFIX + "§eDein Flugmodus wurde deaktiviert.");
            } else {
                player.setAllowFlight(true);
                player.sendMessage(StringDefaults.PREFIX + "§eDein Flugmodus wurde aktiviert.");
            }

            return true;
        }

        if (!player.hasPermission("arisemc.fly.other")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(StringDefaults.NOT_ONLINE);
            return true;
        }

        if (target.getAllowFlight()) {
            target.setFlying(false);
            target.setAllowFlight(false);
            target.sendMessage(StringDefaults.PREFIX + "§eDein Flugmodus wurde deaktiviert.");
            player.sendMessage(StringDefaults.PREFIX + "§eDer Flugmodus wurde deaktiviert.");
        } else {
            target.setAllowFlight(true);
            target.sendMessage(StringDefaults.PREFIX + "§eDein Flugmodus wurde aktiviert.");
            player.sendMessage(StringDefaults.PREFIX + "§eDer Flugmodus wurde aktiviert.");
        }
        return true;
    }


}
