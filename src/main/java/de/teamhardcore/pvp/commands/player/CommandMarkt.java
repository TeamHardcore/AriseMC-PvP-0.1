/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.UserMarket;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandMarkt implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (label.equalsIgnoreCase("verkaufen")) {
            if (args.length == 0) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Münzen>");
                return true;
            }

            if (args.length == 1) {
                ItemStack itemStack = player.getItemInHand();

                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu musst ein Item in der Hand halten.");
                    return true;
                }

                long coins;
                try {
                    coins = Long.parseLong(args[0]);
                    if (coins <= 500)
                        throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Preis muss mindestens §7500 Münzen §cbetragen.");
                    return true;
                }

                UserMarket userMarket = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMarket();

                if (userMarket.getItems().size() >= 5) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast bereits 5 Auktionen erstellt.");
                    return true;
                }

                Main.getInstance().getMarketManager().createOffer(player.getUniqueId(), itemStack, coins);

                player.getItemInHand().setType(Material.AIR);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast erfolgreich eine Auktion erstellt.");
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
            }
        }

        if (label.equalsIgnoreCase("markt")) {
            Main.getInstance().getMarketManager().openInventory(player, 1, 1);
        }

        return true;
    }
}
