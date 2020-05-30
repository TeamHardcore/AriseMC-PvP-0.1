/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandVanish implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.vanish")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/vanish [Spieler]");
            return true;
        }

        if (args.length == 0) {
            if (!Main.getInstance().getManager().getPlayersInVanish().contains(player)) {
                Main.getInstance().getManager().vanishAll(player);
                player.sendMessage(StringDefaults.PREFIX + "§eDein Vanishmode wurde aktiviert.");
            } else {
                Main.getInstance().getManager().unVanishAll(player);
                player.sendMessage(StringDefaults.PREFIX + "§eDein Vanishmode wurde deaktiviert.");
            }
        }

        if (args.length == 1) {
            if (!player.hasPermission("arisemc.vanish.other")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.performCommand("/vanish");
                return true;
            }

            if (!Main.getInstance().getManager().getPlayersInVanish().contains(target)) {
                Main.getInstance().getManager().vanishAll(target);
                target.sendMessage(StringDefaults.PREFIX + "§eDein Vanishmode wurde aktiviert.");
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Vanishmode für §7" + target.getName() + " §eaktiviert.");
            } else {
                Main.getInstance().getManager().unVanishAll(target);
                target.sendMessage(StringDefaults.PREFIX + "§eDein Vanishmode wurde deaktiviert.");
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Vanishmode für §7" + target.getName() + " §edeaktiviert.");
            }

        }

        return true;
    }
}
