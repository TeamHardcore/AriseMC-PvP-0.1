/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.gambling;

import de.teamhardcore.pvp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCasino implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        // player.openInventory(CasinoInventory.getCasinoInventory());

        if (args.length == 0) {
            player.sendMessage("/jackpot start");
            player.sendMessage("/jackpot add");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                Main.getInstance().getJackpotManager().startJackpot(20000);
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                Main.getInstance().getJackpotManager().getCurrentJackpot().getMemberList().addMember(
                        player.getUniqueId(),
                        20000);

                Main.getInstance().getJackpotManager().getCurrentJackpot().getJackpotInventory().addMemberToInventory(
                        player);
                Main.getInstance().getJackpotManager().getCurrentJackpot().getJackpotInventory().openInventory(player);
            }
        }


        return true;
    }
}
