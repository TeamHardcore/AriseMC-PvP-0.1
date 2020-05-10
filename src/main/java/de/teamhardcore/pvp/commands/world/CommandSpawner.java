/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.world;

import com.google.gson.internal.$Gson$Preconditions;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.SpawnerInventory;
import de.teamhardcore.pvp.model.customspawner.CustomSpawner;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class CommandSpawner implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.spawner")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        Block target = player.getTargetBlock((Set<Material>) null, 5);

        if (target == null || target.getType() != Material.MOB_SPAWNER) {
            player.sendMessage(StringDefaults.PREFIX + "§cDu musst einen Spawner ansehen.");
            return true;
        }

        CustomSpawner customSpawner = Main.getInstance().getSpawnerManager().getCustomSpawner(target.getLocation());

        if (customSpawner == null || (!customSpawner.getOwner().equals(player.getUniqueId()) || !player.hasPermission("arisemc.spawner.admin"))) {
            player.sendMessage(StringDefaults.PREFIX + "§cDu kannst nur deine eigenen Spawner ändern.");
            return true;
        }

        SpawnerInventory.openInventory(player, customSpawner);
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/spawner");
    }
}
