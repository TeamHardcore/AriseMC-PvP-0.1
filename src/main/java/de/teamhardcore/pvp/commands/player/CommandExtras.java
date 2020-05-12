/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.inventories.ExtrasInventory;
import de.teamhardcore.pvp.utils.StringDefaults;
import net.minecraft.server.v1_8_R3.IPlayerFileData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;

public class CommandExtras implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        // /extras teamhardcore <add|remove|info> [extra]

        Player player = (Player) cs;

        if (args.length != 0 && args.length != 3) {
            if (player.hasPermission("arisemc.extras.admin")) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/extras ");
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/extras <info|add|remove> <extra>");
                return true;
            }
            ExtrasInventory.openInventory(player, 1);
        }

        if (args.length == 0) {
            ExtrasInventory.openInventory((Player) cs, 3);
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("add")) {

                String[] extraString = args[2].split(":");

                if (extraString.length != 2) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDas Extra wurde nicht gefunden.");
                    return true;
                }

                String category = extraString[0];
                String extra = extraString[1];

                if (category == null || extra == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDas Extra wurde nicht gefunden.");
                    return true;
                }

                //todo: fix that bitch


            }

        }
        return true;
    }
}
