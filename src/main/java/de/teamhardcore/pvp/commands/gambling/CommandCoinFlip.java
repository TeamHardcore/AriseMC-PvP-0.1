/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.gambling;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.CoinFlip;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCoinFlip implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (args.length == 0) {
            if (Main.getInstance().getCoinFlipManager().getOpenCoinFlip(player) != null) {
                player.sendMessage(StringDefaults.COINFLIP_PREFIX + "§cDu hast bereits einen Coinflip geöffnet.");
                return true;
            }

            Main.getInstance().getCoinFlipManager().addCoinFlip(player, 0);
            player.sendMessage(StringDefaults.COINFLIP_PREFIX + "§6Du hast einen Coinflip geöffnet.");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.COINFLIP_PREFIX + "§cDu kannst deinem eigenen Coinflip nicht beitreten.");
                return true;
            }

            CoinFlip coinFlip = Main.getInstance().getCoinFlipManager().getOpenCoinFlip(target);

            if (coinFlip == null) {
                player.sendMessage(StringDefaults.COINFLIP_PREFIX + "§cDieser Spieler hat keinen Coinflip geöffnet.");
                return true;
            }

            Main.getInstance().getCoinFlipManager().startCoinFlip(coinFlip, player);
            coinFlip.getEntries().get(0).sendMessage(StringDefaults.COINFLIP_PREFIX + "§6Du befindest dich mit §e" + player.getName() + " §6in einem Coinflip.");
            player.sendMessage(StringDefaults.COINFLIP_PREFIX + "§6Du befindest dich mit §e" + coinFlip.getEntries().get(0).getName() + " §6in einem CoinFlip.");

            return true;
        }

        return true;
    }
}
