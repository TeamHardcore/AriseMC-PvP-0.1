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

public class CommandSwitchglass implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        if (!player.hasPermission("arisemc.switchglass") && !userData.getUnlockedCommands().contains(EnumCommand.SWITCHGLASS)) {
            ExtrasInventory.openInventory(player, 3);
            return true;
        }

        int count = 0;
        PlayerInventory inventory = player.getInventory();

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType() == Material.AIR)
                count += itemStack.getAmount();
        }

        if (count < 3) {
            Util.sendActionbar(player, "§cDu hast zu wenig Glas im Inventar.");
            return true;
        }
        inventory.remove(Material.GLASS);

        int glasscount = 0;

        for (int amount = count; amount >= 3; amount -= 3) {
            glasscount += 3;
            count -= 3;
        }

        if (count > 0)
            Util.addItem(player, new ItemStack(Material.GLASS, count));

        Util.addItem(player, new ItemStack(Material.GLASS_BOTTLE, glasscount));
        Util.sendActionbar(player, "§eDein Glas wurde zu Glasflaschen umgewandelt.");

        return true;
    }
}
