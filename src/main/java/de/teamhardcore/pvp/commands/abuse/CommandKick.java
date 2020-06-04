/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.abuse;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKick implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (label.equalsIgnoreCase("kick")) {
            if (!player.hasPermission("arisemc.kick")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/kick <Spieler> [Grund]");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NOT_ONLINE);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dich nicht selber kicken.");
                return true;
            }

            if (target.getUniqueId().toString().equals("dad65097-f091-4531-8431-42e2fb2bd80c")) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu darfst diesen Spieler nicht kicken.");
                return true;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                sb.append(args[i]).append(" ");
            }
            String reason = sb.length() == 0 ? "-" : sb.substring(0, sb.length() - 1);
            target.kickPlayer("§cDu wurdest vom Server gekickt. Grund§8: §7" + reason);

            Bukkit.getOnlinePlayers().forEach(players -> {
                players.sendMessage(StringDefaults.PREFIX + "§4Der Spieler §7" + target.getName() + " §4wurde vom Server gekickt.");
                if (players.hasPermission("arisemc.kick.notify"))
                    players.sendMessage(StringDefaults.PREFIX + "§4Grund§8: §7" + reason);
            });

            player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + target.getName() + " §evom Server gekickt.");
        }

        if (label.equalsIgnoreCase("kickall")) {
            if (!player.hasPermission("arisemc.kickall")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; ++i) {
                sb.append(args[i]).append(" ");
            }
            String reason = sb.length() == 0 ? "-" : sb.substring(0, sb.length() - 1);

            Bukkit.getOnlinePlayers().forEach(players -> {
                if (players != player && !players.hasPermission("arisemc.kickall.bypass")
                        && !players.getUniqueId().toString().equals("dad65097-f091-4531-8431-42e2fb2bd80c")) {

                    if (Main.getInstance().getCombatManager().isTagged(players))
                        Main.getInstance().getCombatManager().setTagged(players, false);

                    players.kickPlayer("§cDu wurdest vom Server gekickt. Grund§8: §7" + reason);
                }
            });

            player.sendMessage(StringDefaults.PREFIX + "§eDu hast alle Spieler vom Server gekickt.");
        }

        return true;
    }
}
