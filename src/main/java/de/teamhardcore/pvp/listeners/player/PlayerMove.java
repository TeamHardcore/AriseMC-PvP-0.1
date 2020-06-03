/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    private final Main plugin;

    /*
     Knochen der Mobs
     Knochen eines Zombies (Beispiel)
     Benötigt um Spawn-Eier freizuschalten.

     Merchant/Transmog (Prefix) System für Arena(ArenaPunkte bei Ranked Arena)

     Eine Zone am Spawn, die ein Clan einnehmen kann für Belohnungen (Stronghold)

     */
    public PlayerMove(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!locationChanged(event.getFrom(), event.getTo()) || this.plugin.getDuelManager().getDuelCache().get(player.getUniqueId()) == null) {
            return;
        }

        Duel duel = this.plugin.getDuelManager().getDuelCache().get(player.getUniqueId());
        duel.updateWall(player);

        if (duel.getDuelWall().contains(player.getLocation())) {
            duel.performDeath(player);
            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu hast die Duell Zone verlassen.");
            return;
        }
    }

    private boolean locationChanged(Location from, Location to) {
        return (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ());
    }

}
