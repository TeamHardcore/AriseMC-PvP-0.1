/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.chat;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMsg implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;
        if (label.equalsIgnoreCase("msg") || label.equalsIgnoreCase("tell") || label.equalsIgnoreCase("pn") || label.equalsIgnoreCase("whisper") || label.equalsIgnoreCase("t") || label.equalsIgnoreCase("m") || label.equalsIgnoreCase("w")) {

            if (args.length < 2) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7" + label + " <Spieler> <Nachricht>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (player == target) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dir selber keine Nachricht schreiben.");
                return true;
            }

            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }

            String message = builder.substring(0, builder.length() - 1);

            player.sendMessage(StringDefaults.MSG_PREFIX + "§6Du -> §e" + target.getName() + "§8: §f" + message);
            target.sendMessage(StringDefaults.MSG_PREFIX + "§e" + player.getName() + "§6 -> Dir§8: §f" + message);

            Main.getInstance().getChatManager().getLastMessageContacts().put(player, target);
            Main.getInstance().getChatManager().getLastMessageContacts().put(target, player);

        }

        if (label.equalsIgnoreCase("r") || label.equalsIgnoreCase("reply") || label.equalsIgnoreCase("antworten")) {

            if (args.length == 0) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7" + label + " <Nachricht>");
                return true;
            }

            if (!Main.getInstance().getChatManager().getLastMessageContacts().containsKey(player)) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast niemandem zuvor geschrieben.");
                return true;
            }

            Player target = Main.getInstance().getChatManager().getLastMessageContacts().get(player);

            StringBuilder builder = new StringBuilder();

            for (String arg : args) {
                builder.append(arg).append(" ");
            }
            String message = builder.substring(0, builder.length() - 1);

            player.sendMessage(StringDefaults.MSG_PREFIX + "§6Du -> §e" + target.getName() + "§8: §f" + message);
            target.sendMessage(StringDefaults.MSG_PREFIX + "§e" + player.getName() + "§6 -> Dir§8: §f" + message);

            Main.getInstance().getChatManager().getLastMessageContacts().put(player, target);
            Main.getInstance().getChatManager().getLastMessageContacts().put(target, player);
        }

        return true;
    }
}
