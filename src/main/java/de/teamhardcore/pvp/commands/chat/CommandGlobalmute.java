/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.chat;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.GlobalmuteTier;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGlobalmute implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.globalmute")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
        }

        if (args.length >= 1) {
            GlobalmuteTier currentTier = Main.getInstance().getChatManager().getGlobalmuteTier();
            GlobalmuteTier tier = GlobalmuteTier.getTierByID(Integer.parseInt(args[0]));

            if (tier == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe eine vernünftige Stufe an.");
                player.sendMessage(StringDefaults.PREFIX + "§cVerfügbare Stufen§8: §70,1,2,3");
                return true;
            }

            if (tier == currentTier) {
                player.sendMessage(StringDefaults.PREFIX + "§cDie Globalmute Stufe ist bereits aktiviert.");
                return true;
            }

            Main.getInstance().getChatManager().setGlobalmuteTier(tier);

            if (tier == GlobalmuteTier.NONE) {
                Bukkit.broadcastMessage(StringDefaults.GLOBAL_PREFIX + "§eDer Globalmute wurde deaktiviert.");
            } else {
                Bukkit.broadcastMessage(StringDefaults.GLOBAL_PREFIX + "§eDer Globalmute wurde aktiviert.");
            }
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(StringDefaults.PREFIX + "§cAktuell läuft die Stufe§8: §7" + Main.getInstance().getChatManager().getGlobalmuteTier().name());
    }

}
