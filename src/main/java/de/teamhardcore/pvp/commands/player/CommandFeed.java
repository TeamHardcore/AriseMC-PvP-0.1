/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class CommandFeed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.feed")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/feed [Spieler]");
            return true;
        }

        if (args.length == 0) {
            player.setFoodLevel(30);
            player.playSound(player.getLocation(), Sound.EAT, 1.0F, 1.0F);
            player.sendMessage(StringDefaults.PREFIX + "§eDein Hunger wurde gestillt.");
        } else {
            if (!player.hasPermission("arisemc.feed.other")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            } else if (target == player) {
                player.performCommand("/feed");
                return true;
            }

            target.setFoodLevel(30);
            target.playSound(target.getLocation(), Sound.EAT, 1.0F, 1.0F);
            target.sendMessage(StringDefaults.PREFIX + "§eDein Hunger wurde gestillt.");
            player.sendMessage(StringDefaults.PREFIX + "§eDer Hunger von §7" + target.getName() + " §ewurde gestillt.");
        }

        return true;
    }
}
