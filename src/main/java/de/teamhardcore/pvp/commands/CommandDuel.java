/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.DuelInventory;
import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.arena.DuelArenaType;
import de.teamhardcore.pvp.model.duel.configuration.DuelDeployment;
import de.teamhardcore.pvp.model.duel.configuration.DuelSettings;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDuel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (args.length == 0) {

            DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(player.getUniqueId());

            if (configuration == null) {
                configuration = new DuelConfiguration(DuelArenaType.PRISON, new DuelSettings(), new DuelDeployment());
                configuration.getPlayers().add(player);
                Main.getInstance().getDuelManager().getConfigurationCache().put(player.getUniqueId(), configuration);
            }

            DuelInventory.openDuelRequestInventory(player, true, configuration);
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("abbrechen")) {
                if (!Main.getInstance().getDuelManager().getConfigurationCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast noch kein Duell erstellt.");
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(player.getUniqueId());

                for (Player targets : configuration.getPlayers()) {
                    if (targets != null && targets.isOnline() && targets != player) {
                        targets.sendMessage(StringDefaults.DUEL_PREFIX + "§cDas Duell wurde abgebrochen.");
                    }
                }

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast das Duell abgebrochen.");
                Main.getInstance().getDuelManager().getConfigurationCache().remove(player.getUniqueId());
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("einladen")) {
                if (!Main.getInstance().getDuelManager().getConfigurationCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast noch kein Duell erstellt.");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(player.getUniqueId());

                if (configuration.getPlayers().size() >= 2) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast bereits einen Spieler herausgefordert.");
                    return true;
                }

                if (configuration.getInvites().contains(target)) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast diesen Spieler bereits herausgefordert.");
                    return true;
                }

                configuration.getInvites().add(target);

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast §7" + target.getName() + " §ezu einem Duell herausgefordert.");
                target.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu wurdest von §7" + player.getName() + " §ezu einem Duell herausgefordert.");
                new JSONMessage(StringDefaults.DUEL_PREFIX + "§eKlicke hier§7, §eum die Einstellungen zu betrachten.").runCommand("/duel accept " + player.getName()).tooltip("§eEinstellungen betrachten").send(target);

            } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("entfernen")) {

            } else if (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("annehmen")) {
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurationCache().get(target.getUniqueId());
                if (configuration == null) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu wurdest von diesem Spieler zu keinem Duell herausgefordert.");
                    return true;
                }

                if (!configuration.getInvites().contains(player)) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu wurdest von diesem Spieler zu keinem Duell herausgefordert.");
                    return true;
                }

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Duellanfrage akzeptiert.");
                target.sendMessage(StringDefaults.DUEL_PREFIX + "§eDer Spieler §7" + player.getName() + " §ehat die Duellanfrage akzeptiert.");

                configuration.getInvites().remove(player);
                configuration.getPlayers().add(player);

                Main.getInstance().getDuelManager().getConfigurationCache().remove(player.getUniqueId());
                Main.getInstance().getDuelManager().createDuel(configuration);
            }
        }

        return true;
    }
}
