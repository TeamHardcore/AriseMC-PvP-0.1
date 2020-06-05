/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.dev;

import de.teamhardcore.pvp.database.HandlerGroup;
import de.teamhardcore.pvp.database.HandlerGroups;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDebug implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (!player.getUniqueId().toString().equals("dad65097-f091-4531-8431-42e2fb2bd80c") || !player.getUniqueId().toString().equals("032a105a-7029-48ed-8fb3-c85e115c2c31")) {
            cs.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/debug <handler> <info>");
            player.sendMessage(StringDefaults.PREFIX + "§7§oWeitere Möglichkeiten folgen.");
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("handler")) {
                if (args[1].equalsIgnoreCase("info")) {
                    for (HandlerGroup group : HandlerGroups.getHandlerGroups().values()) {
                        player.sendMessage("§e" + group.getName() + " §8| §eStatus§8: §7" + (group.isRunning() ? "Aktiv" : "Inaktiv") + " §8| §eHandler§8: §7" + group.getHandlers().size());
                    }
                }
            }
        }
        //todo: add others
        return true;
    }
}
