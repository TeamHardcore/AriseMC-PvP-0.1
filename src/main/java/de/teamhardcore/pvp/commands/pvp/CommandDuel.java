/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.pvp;

import com.boydti.fawe.util.MainUtil;
import com.google.gson.internal.$Gson$Preconditions;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.DuelInventory;
import de.teamhardcore.pvp.model.duel.map.DuelMap;
import de.teamhardcore.pvp.model.duel.request.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.request.DuelRequest;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandDuel implements CommandExecutor {

    /*




     */


    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (Main.getInstance().getDuelManager().getDuelCache().containsKey(player.getUniqueId())) {
            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu befindest dich bereits in einem Duell.");
            return true;
        }

        if (args.length == 0) {
            if (Main.getInstance().getDuelManager().getAvailableMaps().isEmpty()) {
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cEs sind keine freien Arenen verfügbar.");
                return true;
            }

            if (Main.getInstance().getDuelManager().getDuel(player) != null) {
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu befindest dich bereits in einem Duell.");
                return true;
            }

            DuelConfiguration configuration = new DuelConfiguration(player);

            DuelInventory.openRequestInventory(player, true, configuration);
            Main.getInstance().getDuelManager().getConfigurations().put(player, configuration);
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("abbrechen")) {
                if (!Main.getInstance().getDuelManager().getConfigurations().containsKey(player)) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast noch keine Konfiguration erstellt.");
                    return true;
                }

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurations().get(player);

                for (Player targets : configuration.getPlayers()) {
                    if (targets != null && targets.isOnline() && targets != player) {
                        targets.sendMessage(StringDefaults.DUEL_PREFIX + "§cDas Duell wurde abgebrochen.");
                    }
                }

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast das Duell abgebrochen.");
                Main.getInstance().getDuelManager().getConfigurations().remove(player);
            }

            if (args[0].equalsIgnoreCase("annehmen") || args[0].equalsIgnoreCase("accept")) {

                DuelRequest request = Main.getInstance().getDuelManager().getDuelRequest(player);

                if (request == null) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu wurdest zu keinem Duell herausgefordert.");
                    return true;
                }

                DuelInventory.openRequestInventory(player, false, request.getConfiguration());
            }

            if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin create arena <Category> <Name>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin create category <Name>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin delete arena <Name>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin delete category <Category>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin location add <Arena>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin location remove <Arena>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin tp <Name>");
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cVerwendung§8: §7/duel admin list");
                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            }

        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("einladen")) {
                if (!Main.getInstance().getDuelManager().getConfigurations().containsKey(player)) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast noch keine Konfiguration erstellt.");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

            /*    if (target == player) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu kannst dich nicht selbst herausfordern.");
                    return true;
                }*/

                DuelConfiguration configuration = Main.getInstance().getDuelManager().getConfigurations().get(player);


                if (configuration.getPlayers().size() >= 2) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cEin anderer Spieler hat deine Anfrage bereits angenommen.");
                    return true;
                }

                DuelRequest duelRequest = new DuelRequest(player, target, configuration);

                Main.getInstance().getDuelManager().addDuelRequest(target, duelRequest);

                player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast §7" + target.getName() + " §ezu einem Duell herausgefordert.");
                target.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                target.sendMessage(" ");
                target.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu wurdest von §7" + player.getName() + " §eherausgefordert.");
                new JSONMessage(StringDefaults.DUEL_PREFIX + "§eKlicke hier§7, §eum die Herausforderung zu betrachten.").tooltip("§6Herausforderung betrachten").suggestCommand("/duell accept ").send(player);
                target.sendMessage(" ");
                target.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            }

            if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                if (args[1].equalsIgnoreCase("info")) {
                    player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                    for (Map.Entry<String, List<DuelMap>> entry : Main.getInstance().getDuelManager().getDuelMaps().entrySet()) {
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eKategorie§8: §7" + entry.getKey());
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eMaps§8: §7" + entry.getValue().size());
                        for (DuelMap map : entry.getValue()) {
                            new JSONMessage("                 " + StringDefaults.PREFIX + "§e" + map.getName() + "§7, §eLocations§8: §7" + map.getLocations().size() + "§7 §7§o[Klick]").tooltip("§6Teleport").runCommand("/duel admin tp " + map.getName()).send(player);
                        }
                        player.sendMessage(" ");
                        player.sendMessage(" ");
                        player.sendMessage(" ");
                    }
                    player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                }

            }

        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                DuelMap duelMap = Main.getInstance().getDuelManager().getMap(args[2]);

                if (duelMap == null) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map existiert nicht.");
                    return true;
                }

                if (duelMap.getLocations().isEmpty()) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map besitzt keine Spawnpunkte.");
                    return true;
                }

                if (args[1].equalsIgnoreCase("tp")) {
                    player.teleport(duelMap.getLocations().get(0));
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu wurdest zum ersten Spawnpunkt von §7" + duelMap.getName() + " §eteleportiert.");
                    return true;
                }
            }
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                if (args[1].equalsIgnoreCase("location")) {

                    DuelMap duelMap = Main.getInstance().getDuelManager().getMap(args[3]);

                    if (duelMap == null) {
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map existiert nicht.");
                        return true;
                    }

                    if (args[2].equalsIgnoreCase("add")) {

                        if (duelMap.getLocations().size() >= 2) {
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map hat bereits zwei Spawnpunkte.");
                            return true;
                        }

                        duelMap.getLocations().add(player.getLocation());
                        Main.getInstance().getDuelManager().saveMaps();
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast einen Spawnpunkt hinzugefügt.");
                    } else if (args[2].equalsIgnoreCase("remove")) {
                        if (duelMap.getLocations().isEmpty() || duelMap.getLocations().size() <= 0) {
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map besitzt keinen Spawnpunkt.");
                            return true;
                        }

                        duelMap.getLocations().remove(0);
                        Main.getInstance().getDuelManager().saveMaps();
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast den aktuellsten Spawnpunkt gelöscht..");
                    }

                }

                if (args[1].equalsIgnoreCase("create")) {

                    String name = args[3];

                    if (args[2].equalsIgnoreCase("category")) {

                        if (Main.getInstance().getDuelManager().getDuelMaps().containsKey(name)) {
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDiese Kategorie existiert bereits.");
                            return true;
                        }

                        Main.getInstance().getDuelManager().getDuelMaps().put(name, new ArrayList<>());
                        Main.getInstance().getDuelManager().saveMaps();
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Kategorie §7" + name + " §eerstellt.");
                    }

                }

                if (args[1].equalsIgnoreCase("delete")) {
                    String name = args[3];

                    if (args[2].equalsIgnoreCase("category")) {

                        if (!Main.getInstance().getDuelManager().getDuelMaps().containsKey(name)) {
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDiese Kategorie existiert nicht.");
                            return true;
                        }
                        Main.getInstance().getDuelManager().getDuelMaps().remove(name);
                        Main.getInstance().getDuelManager().saveMaps();
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast die Kategorie §7" + name + " §egelöscht.");
                    }

                    if (args[2].equalsIgnoreCase("arena")) {

                        if (Main.getInstance().getDuelManager().getMap(name) == null) {
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map existiert nicht.");
                            return true;
                        }

                        Main.getInstance().getDuelManager().removeMap(name);
                        Main.getInstance().getDuelManager().saveMaps();
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Map §7" + name + " §egelöscht.");
                    }
                }

            }
        }

        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (!player.hasPermission("arisemc.duel.admin")) {
                    player.sendMessage(StringDefaults.NO_PERM);
                    return true;
                }

                if (args[1].equalsIgnoreCase("create")) {
                    String name = args[4];
                    String category = args[3];

                    if (Main.getInstance().getDuelManager().getMap(name) != null) {
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Map existiert bereits.");
                        return true;
                    }

                    if (!Main.getInstance().getDuelManager().getDuelMaps().containsKey(category)) {
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDie Kategorie existiert nicht.");
                        return true;
                    }

                    if (args[2].equalsIgnoreCase("arena")) {
                        Main.getInstance().getDuelManager().addMap(name, category);
                        Main.getInstance().getDuelManager().saveMaps();
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Map §7" + name + " §ezur Kategorie §7" + category + " §ehinzugefügt.");
                    }

                }

            }
        }

        return true;
    }
}
