/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.chat;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.SpyMode;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandCommandSpy implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.commandspy")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <all|Spieler>");
            return true;
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (Main.getInstance().getManager().getCommandSpyModeCache().containsKey(player.getUniqueId())) {
                SpyMode spyMode = Main.getInstance().getManager().getCommandSpyModeCache().get(player.getUniqueId());
                if (spyMode.isAll()) {
                    spyMode.setAll(false);
                    player.sendMessage(StringDefaults.SPY_PREFIX + "§eDu hast den CommandSpy Modus §7All §edeaktiviert.");
                } else {
                    spyMode.setAll(true);
                    player.sendMessage(StringDefaults.SPY_PREFIX + "§eDu hast deinen CommandSpy Modus auf §7All §egeändert.");
                }
                return true;
            }
            SpyMode spyMode = new SpyMode(true, new ArrayList<>());
            Main.getInstance().getManager().getCommandSpyModeCache().put(player.getUniqueId(), spyMode);
            player.sendMessage(StringDefaults.SPY_PREFIX + "§eDu hast den CommandSpy Modus §7All §eaktiviert.");
        } else {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }

            if (target == player) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dich selber nicht spionieren.");
                return true;
            }

            if (Main.getInstance().getManager().getCommandSpyModeCache().containsKey(player.getUniqueId())) {
                SpyMode spyMode = Main.getInstance().getManager().getCommandSpyModeCache().get(player.getUniqueId());

                if (!spyMode.isAll()) {
                    if (spyMode.getPlayers().contains(target)) {
                        spyMode.getPlayers().remove(target);
                        player.sendMessage(StringDefaults.SPY_PREFIX + "§7" + target.getName() + " §ewurde vom CommandSpy ausgeschlossen.");

                        if (spyMode.getPlayers().isEmpty()) {
                            Main.getInstance().getManager().getCommandSpyModeCache().remove(player.getUniqueId());
                            return true;
                        }
                    } else {
                        spyMode.getPlayers().add(target);
                        player.sendMessage(StringDefaults.SPY_PREFIX + "§7" + target.getName() + " §ewurde zum CommandSpy hinzugefügt.");
                    }
                } else {
                    spyMode.setAll(false);
                    spyMode.getPlayers().add(target);
                    player.sendMessage(StringDefaults.SPY_PREFIX + "§7" + target.getName() + " §ewurde zum CommandSpy hinzugefügt.");
                }
                return true;
            }

            SpyMode spyMode = new SpyMode(false, new ArrayList<>());
            spyMode.getPlayers().add(target);
            Main.getInstance().getManager().getCommandSpyModeCache().put(player.getUniqueId(), spyMode);
            player.sendMessage(StringDefaults.SPY_PREFIX + "§7" + target.getName() + " §ewurde zum CommandSpy hinzugefügt.");
        }
        return true;
    }
}
