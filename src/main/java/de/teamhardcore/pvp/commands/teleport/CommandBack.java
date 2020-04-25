/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.teleport;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBack implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.back")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (!Main.getInstance().getManager().getLastPositions().containsKey(player)) {
            player.sendMessage(StringDefaults.PREFIX + "§cDu hast keine vorherige Position.");
            return true;
        }

        Location location = Main.getInstance().getManager().getLastPositions().get(player);

        if (player.hasPermission("arisemc.teleport.nodelay")) {
            Main.getInstance().getManager().getLastPositions().put(player, player.getLocation());
            player.teleport(location);
            player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest an deine vorherige Position teleportiert.");
        } else {
            player.sendMessage(StringDefaults.PREFIX + "§eDu wirst in §72 Sekunden §eteleportiert.");

            TPDelay tpDelay = new TPDelay(player, 0, 2) {
                @Override
                public boolean onTick() {
                    return false;
                }

                @Override
                public void onEnd() {
                    Main.getInstance().getManager().getLastPositions().put(getPlayer(), getPlayer().getLocation());
                    getPlayer().teleport(location);
                    player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest an deine vorherige Position teleportiert.");
                }
            };
            Main.getInstance().getManager().getTeleportDelays().put(player, tpDelay);
            return true;
        }
        return true;
    }
}
