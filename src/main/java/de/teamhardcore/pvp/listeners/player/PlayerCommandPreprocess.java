/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

public class PlayerCommandPreprocess implements Listener {

    private final Main plugin;
    private final String[] blockedCommands = new String[]{"ver", "version", "pl", "plugins", "me", "?", "help", "icanhasbukkit", "about", "lp", "luckperms"};

    public PlayerCommandPreprocess(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().contains("̇") || event.getMessage().equalsIgnoreCase("")) {
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        String fullCmd = event.getMessage().substring(1);
        String cmd = fullCmd.split(" ")[0];

        if (!player.hasPermission("arisemc.useblockedcmd")) {
            if (cmd.startsWith("bukkit:") || cmd.startsWith("minecraft:")) {
                event.setCancelled(true);
                player.sendMessage(StringDefaults.PREFIX + "§cDer Befehl wurde nicht gefunden. §6§l/help");
                return;
            }

            for (String blocked : this.blockedCommands) {
                if (cmd.equalsIgnoreCase(blocked)) {
                    event.setCancelled(true);
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Befehl wurde nicht gefunden. §6§l/help");
                    return;
                }
            }
        }

        if (!event.isCancelled()) {
            HelpTopic topic = Bukkit.getHelpMap().getHelpTopic("/" + cmd);
            if (topic == null) {
                event.setCancelled(true);
                player.sendMessage(StringDefaults.PREFIX + "§cDer Befehl wurde nicht gefunden. §6§l/help");
            }
        }

    }

}
