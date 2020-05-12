/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.pvp;

import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandFix implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        int repaired = 0;

        for (ItemStack contents : player.getInventory().getContents()) {
            if (contents != null && contents.getType() != Material.AIR) {
                if (contents.getType().getMaxDurability() != 0 && contents.getDurability() != 0) {
                    contents.setDurability((short) 0);
                    repaired++;
                }
            }
        }

        for (ItemStack armorContents : player.getInventory().getArmorContents()) {
            if (armorContents != null && armorContents.getType() != Material.AIR) {
                if (armorContents.getType().getMaxDurability() != 0 && armorContents.getDurability() != 0) {

                    armorContents.setDurability((short) 0);
                    repaired++;
                }
            }
        }

        Util.sendActionbar(player, (repaired > 0 ? "§eDeine Items wurden erfolgreich repariert.." : "§cEs konnten keine Items repariert werden."));

        return true;
    }
}
