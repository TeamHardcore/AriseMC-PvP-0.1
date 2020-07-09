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
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CommandSwitchgold implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        if (!player.hasPermission("arisemc.switchgold") && !userData.getUnlockedCommands().contains(EnumCommand.GOLDSWITCH)) {
            ExtrasInventory.openInventory(player, 3);
            return true;
        }

        int count = 0;
        PlayerInventory inventory = player.getInventory();

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.AIR)
                count += itemStack.getAmount();
        }

        if (count < 9) {
            Util.sendActionbar(player, "§cDu hast zu wenig Goldnuggets im Inventar.");
            return true;
        }

        inventory.remove(Material.GOLD_NUGGET);

        int goldcount = 0;

        for (int amount = count; amount >= 9; amount -= 9) {
            goldcount++;
            count -= 9;
        }

        if (count > 0)
            Util.addItem(player, new ItemStack(Material.GLASS, count));

        Util.addItem(player, new ItemStack(Material.GLASS_BOTTLE, goldcount));
        Util.sendActionbar(player, "§eDeine Goldnuggets wurden erfolgreich umgewandelt.");
        return true;
    }
}
