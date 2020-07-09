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
import de.teamhardcore.pvp.utils.VirtualAnvil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandAnvil implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        if (!player.hasPermission("arisemc.anvil") && !userData.getUnlockedCommands().contains(EnumCommand.ANVIL)) {
            ExtrasInventory.openInventory(player, 3);
            return true;
        }

        VirtualAnvil.openVirtualAnvil(player);
        return true;
    }
}
