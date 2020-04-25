/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPlayertime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.ptime")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        //todo: check if player has extra command

        if (args.length != 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/+" + label + " <day|night|ticks|reset>");
            return true;
        }

        if (args[0].equalsIgnoreCase("day")) {
            long time = player.getPlayerTime();
            time -= time % 24000L;
            time += 24000L - player.getWorld().getTime();
            player.setPlayerTime(time, true);
            player.sendMessage(StringDefaults.PREFIX + "§eDeine Client-Zeit wurde auf §7Tag §egesetzt.");

        } else if (args[0].equalsIgnoreCase("night")) {
            long time = player.getPlayerTime();
            time -= time % 24000L;
            time += 38000L - player.getWorld().getTime();
            player.setPlayerTime(time, true);
            player.sendMessage(StringDefaults.PREFIX + "§eDeine Client-Zeit wurde auf §7Nacht §egesetzt.");

        } else if (args[0].equalsIgnoreCase("reset")) {
            player.resetPlayerTime();
            player.sendMessage(StringDefaults.PREFIX + "§eDeine Client-Zeit läuft nun wieder nach der Serverzeit.");
        } else {
            int timeParsed;
            try {
                timeParsed = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe eine gültige Zeit an.");
                return true;
            }
            long time = player.getPlayerTime();
            time -= time % 24000L;
            time += (24000 + timeParsed) - player.getWorld().getTime();
            player.setPlayerTime(time, true);
            player.sendMessage(StringDefaults.PREFIX + "§eDeine Client-Zeit wurde auf §7" + time + " Ticks §egesetzt.");
        }

        return true;
    }
}
