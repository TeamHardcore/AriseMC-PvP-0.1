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

public class CommandDebug implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cs.isOp()) {
            cs.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args[0].equalsIgnoreCase("handler")) {
            if (args[1].equalsIgnoreCase("info")) {
                for (HandlerGroup group : HandlerGroups.getHandlerGroups().values()) {
                    cs.sendMessage("§e" + group.getName() + " §8| §eStatus§8: §7" + (group.isRunning() ? "Aktiv" : "Inaktiv") + " §8| §eHandler§8: §7" + group.getHandlers().size());
                }
            }

        }


        //todo: add others

        return true;
    }
}
