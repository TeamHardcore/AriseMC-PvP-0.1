/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.warp;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (label.equalsIgnoreCase("spawn")) {
            if (Main.getInstance().getWarpManager().getWarp("Spawn") == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cEs existiert noch kein Spawn.");
                return true;
            }

            if (Main.getInstance().getManager().getTeleportDelays().containsKey(player)) {
                player.sendMessage(StringDefaults.PREFIX + "§cEs läuft bereits ein Teleportvorgang.");
                return true;
            }

            if (player.hasPermission("arisemc.teleport.nodelay")) {
                Main.getInstance().getManager().getLastPositions().put(player, player.getLocation());
                player.teleport(Main.getInstance().getWarpManager().getWarp("Spawn").getLocation());
                Util.sendActionbar(player, "§eDu wurdest zum Spawn teleportiert.");
            } else {
                Util.sendActionbar(player, "§eDu wirst in §72 Sekunden §eteleportiert.");

                TPDelay tpDelay = new TPDelay(player, 0, 2) {
                    @Override
                    public boolean onTick() {
                        return false;
                    }

                    @Override
                    public void onEnd() {
                        Main.getInstance().getManager().getLastPositions().put(getPlayer(), getPlayer().getLocation());
                        getPlayer().teleport(Main.getInstance().getWarpManager().getWarp("Spawn").getLocation());
                        Util.sendActionbar(player, "§eDu wurdest zum Spawn teleportiert.");
                    }
                };
                Main.getInstance().getManager().getTeleportDelays().put(player, tpDelay);
            }
        }

        if (label.equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("arisemc.setspawn")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            Main.getInstance().getWarpManager().addWarp("Spawn", player.getLocation());
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Spawn gesetzt.");
        }


        return true;
    }
}
