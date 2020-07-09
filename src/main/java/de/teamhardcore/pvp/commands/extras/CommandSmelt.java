/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.extras;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.ExtrasInventory;
import de.teamhardcore.pvp.inventories.SmeltInventory;
import de.teamhardcore.pvp.model.EnumSmeltable;
import de.teamhardcore.pvp.model.extras.EnumCommand;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandSmelt implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        if (!player.hasPermission("arisemc.smelt") && !userData.getUnlockedCommands().contains(EnumCommand.SMELT)) {
            ExtrasInventory.openInventory(player, 3);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            SmeltInventory.openSmeltInventory(player);
            return true;
        }

        ItemStack itemStack = player.getItemInHand();
        EnumSmeltable smeltable = EnumSmeltable.getByItemData(itemStack.getType(), itemStack.getDurability());

        if (itemStack.getType() == Material.AIR) {
            player.sendMessage(StringDefaults.PREFIX + "§cDu musst ein Item in der Hand halten.");
            return true;
        }

        if (smeltable == null) {
            player.sendMessage(StringDefaults.PREFIX + "§cDieses Item kann nicht geschmolzen werden.");
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " list");
            return true;
        }

        itemStack.setType(smeltable.getTo());
        itemStack.setDurability((short) smeltable.getToData());
        player.sendMessage(StringDefaults.PREFIX + "§eDie Items wurden geschmolzen.");
        return true;
    }
}
