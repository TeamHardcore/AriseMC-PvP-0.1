/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.inventory;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandEnderchest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.enderchest")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/enderchest [Spieler]");
            return true;
        }

        if (args.length == 0) {
            openEnderchest(player, player);
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            if (target == player) {
                player.performCommand("/enderchest");
                return true;
            }

            openEnderchest(player, target);
            Main.getInstance().getManager().getPlayersInEnderchest().add(player);
        }

        return true;
    }

    private void openEnderchest(Player player, Player target) {
        player.openInventory(target.getEnderChest());
    }
}
