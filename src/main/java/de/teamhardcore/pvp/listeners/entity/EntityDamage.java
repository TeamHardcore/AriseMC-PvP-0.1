/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.entity;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.duel.Duel;
import de.teamhardcore.pvp.duel.phases.DuelPhase;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    private final Main plugin;

    public EntityDamage(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();


            if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
                event.setCancelled(true);

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEventByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player damager = null;

        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                damager = (Player) ((Projectile) event.getDamager()).getShooter();
            }
        }

        if (damager == null) return;
        if (damager == player) return;

        if (this.plugin.getClanManager().hasClan(player.getUniqueId()) && this.plugin.getClanManager().hasClan(damager.getUniqueId())) {
            Clan playerClan = this.plugin.getClanManager().getClan(player.getUniqueId());
            Clan damagerClan = this.plugin.getClanManager().getClan(damager.getUniqueId());

            if (playerClan == damagerClan) {
                event.setCancelled(true);
                damager.sendMessage(StringDefaults.CLAN_PREFIX + "§cDu bist mit §7" + player.getName() + " §cin einem Clan.");
            }
        }
    }

}
