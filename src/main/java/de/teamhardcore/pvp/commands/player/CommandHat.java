/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;


public class CommandHat implements CommandExecutor {

    private final ArrayList<Material> blockedMaterials = new ArrayList<>(Arrays.asList(Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.GOLD_BOOTS, Material.GOLD_LEGGINGS, Material.GOLD_CHESTPLATE));

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.hat")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 0) {
            ItemStack hand = player.getItemInHand();
            ItemStack head = player.getInventory().getHelmet();

            if (hand == null || hand.getType() == Material.AIR) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu musst ein Item in der Hand halten.");
                return true;
            }

            if (this.blockedMaterials.contains(hand.getType())) {
                player.sendMessage(StringDefaults.PREFIX + "§cAndere Rüstungsteile können nicht als Hut genutzt werden.");
                return true;
            }

            player.getInventory().setHelmet(hand);
            player.setItemInHand(head);
            player.sendMessage(StringDefaults.PREFIX + "§eDu trägst nun einen Hut.");
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("remove")) {
                ItemStack head = player.getInventory().getItemInHand();

                if (head == null || head.getType() == Material.AIR) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast kein Item auf dem Kopf.");
                    return true;
                }

                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), head);
                } else {
                    player.getInventory().addItem(head);
                }
                player.sendMessage(StringDefaults.PREFIX + "§eDein Hut wurde entfernt.");

            }
        }

        return true;
    }
}
