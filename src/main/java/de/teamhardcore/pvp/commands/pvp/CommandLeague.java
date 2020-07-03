/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.pvp;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.EnumLeague;
import de.teamhardcore.pvp.user.UserStats;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLeague implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;
        UserStats stats = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserStats();
        EnumLeague currentLeague = EnumLeague.getLeagueByTrophies(stats.getTrophies());

        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lLIGA §8§l§m*-*-*-*-*-*-*-*-*");
        player.sendMessage(" §f§lAlle verfügbaren Ligen:");
        player.sendMessage(" §8■ §r" + EnumLeague.BRONZE.getDisplayName() + " §7(§6" + EnumLeague.BRONZE.getTrophies() + " Trophäen§7)");
        player.sendMessage(" §8■ §r" + EnumLeague.SILVER.getDisplayName() + " §7(§6" + EnumLeague.SILVER.getTrophies() + " Trophäen§7)");
        player.sendMessage(" §8■ §r" + EnumLeague.GOLD.getDisplayName() + "§7(§6" + EnumLeague.GOLD.getTrophies() + " Trophäen§7)");
        player.sendMessage(" §8■ §r" + EnumLeague.DIAMOND.getDisplayName() + " §7(§6" + EnumLeague.DIAMOND.getTrophies() + " Trophäen§7)");
        player.sendMessage(" §8■ §r" + EnumLeague.PLATINUM.getDisplayName() + " §7(§6" + EnumLeague.PLATINUM.getTrophies() + " Trophäen§7)");
        player.sendMessage("");
        player.sendMessage(" §6Deine momentane Liga: " + currentLeague.getDisplayName() + " §7(§6" + Util.formatNumber(stats.getTrophies()) + " Trophäen§7)");
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lLIGA §8§l§m*-*-*-*-*-*-*-*-*");

        return true;
    }
}
