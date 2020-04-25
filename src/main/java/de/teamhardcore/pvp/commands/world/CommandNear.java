/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 *
 *
 */

package de.teamhardcore.pvp.commands.world;

import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommandNear implements CommandExecutor {

    private int distance = 200;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.near")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        HashMap<Player, Double> players = new HashMap<>();

        player.getWorld()
                .getPlayers()
                .stream()
                .filter(all -> (all.getLocation().distanceSquared(player.getLocation()) <= (this.distance * this.distance)))
                .forEach(all -> players.put(all, all.getLocation().distanceSquared(player.getLocation())));
        players.remove(player);

        List<Player> toRemove = null;

        for (Player all : players.keySet()) {
            //todo: check if player is in vanish
            if (toRemove == null) toRemove = new ArrayList<>();
            toRemove.add(all);
        }

        if (toRemove != null) toRemove.forEach(players::remove);

        Map<Player, Double> finalPlayers = Util.sortMapByValues(players);

        player.sendMessage("§8--------------");
        player.sendMessage(" ");

        if (finalPlayers.isEmpty())
            player.sendMessage(StringDefaults.PREFIX + "§cEs befinden sich keine Spieler in deiner Nähe.");
        else
            player.sendMessage(StringDefaults.PREFIX + "§cEs wurde" + ((finalPlayers.size() == 1) ? "" : "n") + " §7" + finalPlayers.size() + " §cSpieler in der Nähe gefunden:");
        player.sendMessage(" ");
        StringBuilder builder = new StringBuilder(" ");
        for (Map.Entry<Player, Double> entry : finalPlayers.entrySet()) {
            builder.append("§e").append(entry.getKey().getName()).append(" §8(§7").append((int) Math.sqrt(entry.getValue())).append("m§8), ");
        }
        player.sendMessage(builder.substring(0, builder.length() - 1));
        player.sendMessage("");
        player.sendMessage("§8--------------");

        return true;
    }
}
