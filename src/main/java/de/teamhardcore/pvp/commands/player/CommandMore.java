/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.more")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        ItemStack hand = player.getItemInHand();

        if (hand == null || hand.getType() == Material.AIR) {
            player.sendMessage(StringDefaults.PREFIX + "§cDu musst ein Item in der Hand halten.");
            return true;
        }

        hand.setAmount(64);
        Util.sendActionbar(player, "§eDas Item wurde vervollständigt.");
        return true;
    }
}
