/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.dev;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandRelore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("minechaos.relore")) {
            player.sendMessage(" §8§l►§7§l► §r§cFür diese Aktion besitzt du keine Rechte.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player, label);
            return true;
        }

        ItemStack hand = player.getItemInHand();

        if (hand == null || hand.getType() == Material.AIR) {
            player.sendMessage(" §8§l►§7§l► §r§cDu musst ein Item in der Hand halten.");
            return true;
        }

        ItemMeta meta = hand.getItemMeta();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {

                if (!meta.hasLore()) {
                    player.sendMessage(" §8§l►§7§l► §r§cDas Item besitzt keine Lore.");
                    return true;
                }

                meta.setLore(null);
                hand.setItemMeta(meta);

                player.sendMessage(" §8§l►§7§l► §r§eDie Lore des Items wurde zurückgesetzt.");


            } else if (args[0].equalsIgnoreCase("remove")) {

                if (!meta.hasLore()) {
                    player.sendMessage(" §8§l►§7§l► §r§cDas Item besitzt keine Lore.");
                    return true;
                }

                List<String> lore = meta.getLore();

                if (lore.size() == 1) {
                    lore = null;
                } else {
                    lore.remove(lore.size() - 1);
                }
                meta.setLore(lore);
                hand.setItemMeta(meta);

                player.sendMessage(" §8§l►§7§l► §r§eDie letzte Zeile der Lore wurde gelöscht.");
            } else {
                sendHelp(player, label);
                return true;
            }
        }


        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                List<String> lore;


                if (!meta.hasLore()) {
                    lore = new ArrayList<>();
                } else {
                    lore = meta.getLore();
                }
                String newText = ChatColor.translateAlternateColorCodes('&', args[1]);

                lore.add(newText);
                meta.setLore(lore);
                hand.setItemMeta(meta);

                player.sendMessage(" §8§l►§7§l► §r§eEs wurde eine Zeile der Lore hinzugefügt.");


            } else if (args[0].equalsIgnoreCase("remove")) {
                int pos;
                if (!meta.hasLore()) {
                    player.sendMessage(" §8§l►§7§l► §r§cDas Item besitzt keine Lore.");
                    return true;
                }

                List<String> lore = meta.getLore();


                try {
                    pos = Integer.parseInt(args[1]);
                    if (pos < 1 || pos > lore.size())
                        throw new NumberFormatException();
                    pos--;
                } catch (NumberFormatException e) {
                    player.sendMessage(" §8§l►§7§l► §r§cBitte gebe eine gültige Position ein. (1-" + lore.size() + ")");
                    return true;
                }

                lore.remove(pos);

                if (lore.isEmpty()) {
                    lore = null;
                }
                meta.setLore(lore);
                hand.setItemMeta(meta);

                player.sendMessage(" §8§l►§7§l► §r§eDie Zeile §7" + ++pos + " §eder Lore wurde gelöscht.");
            } else {

                sendHelp(player, label);
                return true;
            }
        }


        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("add")) {
                int pos;

                List<String> lore;
                if (!meta.hasLore()) {
                    lore = new ArrayList<>();
                } else {
                    lore = meta.getLore();
                }


                try {
                    pos = Integer.parseInt(args[1]);
                    if (pos < 1 || pos > lore.size() + 1)
                        throw new NumberFormatException();
                    pos--;
                } catch (NumberFormatException e) {
                    pos = -1;
                }

                StringBuilder sb = new StringBuilder();

                for (int i = (pos == -1) ? 1 : 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }

                String newText = ChatColor.translateAlternateColorCodes('&', sb.substring(0, sb.length() - 1));

                if (pos == -1) {
                    lore.add(newText);
                } else {
                    lore.add(pos, newText);
                }
                meta.setLore(lore);
                hand.setItemMeta(meta);

                player.sendMessage(" §8§l►§7§l► §r§eEs wurde" + ((pos == -1) ? "" : (" an der Stelle §7" + ++pos + "§e")) + " eine Zeile der Lore hinzugefügt.");


            } else if (args[0].equalsIgnoreCase("set")) {
                int pos;

                List<String> lore;
                if (!meta.hasLore()) {
                    lore = new ArrayList<>();
                } else {
                    lore = meta.getLore();
                }


                try {
                    pos = Integer.parseInt(args[1]);
                    if (pos < 1 || pos > lore.size())
                        throw new NumberFormatException();
                    pos--;
                } catch (NumberFormatException e) {
                    player.sendMessage(" §8§l►§7§l► §r§cBitte gebe eine gültige Zeile an.");
                    return true;
                }

                StringBuilder sb = new StringBuilder();

                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }

                String newText = ChatColor.translateAlternateColorCodes('&', sb.substring(0, sb.length() - 1));

                lore.set(pos, newText);
                meta.setLore(lore);
                hand.setItemMeta(meta);

                player.sendMessage(" §8§l►§7§l► §r§eEs wurde die Zeile §7" + ++pos + " §eder Lore editiert.");
            } else {

                sendHelp(player, label);
                return true;
            }
        }


        return true;
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(" §8§l►§7§l► §r§cVerwendung: §7/" + label + " reset");
        player.sendMessage(" §8§l►§7§l► §r§cVerwendung: §7/" + label + " add [Position] <Text>");
        player.sendMessage(" §8§l►§7§l► §r§cVerwendung: §7/" + label + " remove [Position]");
        player.sendMessage(" §8§l►§7§l► §r§cVerwendung: §7/" + label + " set <Position> <Text>");
    }

}
