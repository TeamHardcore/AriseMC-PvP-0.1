/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.extras;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.ExtrasInventory;
import de.teamhardcore.pvp.model.extras.EnumCommand;
import de.teamhardcore.pvp.user.UserData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandZaubertisch implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        if (!player.hasPermission("arisemc.zaubertisch") && !userData.getUnlockedCommands().contains(EnumCommand.ENCHANT)) {
            ExtrasInventory.openInventory(player, 3);
            return true;
        }

        Location enchanter = player.getLocation();
        enchanter.setY(0.0D);
        enchanter.getBlock().setType(Material.ENCHANTMENT_TABLE);

        player.openEnchanting(enchanter, true);
        enchanter.getBlock().setType(Material.BEDROCK);
        return true;
    }
}
