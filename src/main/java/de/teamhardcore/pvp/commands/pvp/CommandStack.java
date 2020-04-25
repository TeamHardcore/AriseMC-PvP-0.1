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

public class CommandStack implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        ItemStack[] contents = player.getInventory().getContents();
        int changed = 0;
        for (int i = 0; i < contents.length; ++i) {
            ItemStack current = contents[i];
            if (current == null || current.getType() == Material.AIR || current.getAmount() <= 0 || current.getAmount() >= 64)
                continue;
            int needed = 64 - current.getAmount();
            for (int i2 = i + 1; i2 < contents.length; ++i2) {
                ItemStack nextCurrent = contents[i2];
                if (nextCurrent == null || nextCurrent.getType() == Material.AIR || nextCurrent.getAmount() <= 0 || current.getType() != nextCurrent.getType() || current.getDurability() != nextCurrent.getDurability() || (current.getItemMeta() != null || nextCurrent.getItemMeta() != null) && (current.getItemMeta() == null || !current.getItemMeta().equals((Object) nextCurrent.getItemMeta())))
                    continue;
                if (nextCurrent.getAmount() > needed) {
                    current.setAmount(64);
                    nextCurrent.setAmount(nextCurrent.getAmount() - needed);
                    continue;
                }
                contents[i2] = null;
                current.setAmount(current.getAmount() + nextCurrent.getAmount());
                needed = 64 - current.getAmount();
                ++changed;
            }
        }

        if (changed > 0) {
            player.getInventory().setContents(contents);
            Util.sendActionbar(player, "§eDeine Items wurden erfolgreich gestackt.");
        } else Util.sendActionbar(player, "§cEs konnten keine Items gestackt werden.");

        return true;
    }
}
