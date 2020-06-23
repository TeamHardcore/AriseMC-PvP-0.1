/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManager {

    private final Main plugin;

    private final ArrayList<UUID> playerQuits = new ArrayList<>();
    private final ConcurrentHashMap<Player, Long> taggedPlayers = new ConcurrentHashMap<>();

    public CombatManager(Main plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(new CombatEvents(this), this.plugin);

        startCombatTask();
    }

    public void startCombatTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {

            for (Map.Entry<Player, Long> entry : this.taggedPlayers.entrySet()) {
                long diff = entry.getValue() - (System.currentTimeMillis() / 1000L);
                if (diff <= 0L) {
                    entry.getKey().sendMessage(StringDefaults.PREFIX + "§eDu bist nicht mehr im Kampf, du kannst dich sicher ausloggen.");
                    this.setTagged(entry.getKey(), false);
                }
            }

        }, 20L, 20L);
    }

    public void setTagged(Player player, boolean tagged) {
        if (!tagged) {
            if (!this.taggedPlayers.containsKey(player)) return;
            this.taggedPlayers.remove(player);
            return;
        }
        if (this.taggedPlayers.containsKey(player)) return;
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        this.taggedPlayers.put(player, (System.currentTimeMillis() / 1000L) + 10L);
    }

    public void updateTime(Player player) {
        if (!isTagged(player)) return;
        this.taggedPlayers.put(player, (System.currentTimeMillis() / 1000L) + 10L);
    }

    public long getRemainingTime(Player player) {
        if (!this.taggedPlayers.containsKey(player)) return 0L;
        return this.taggedPlayers.get(player) * 1000L;
    }

    public boolean isTagged(Player player) {
        return this.taggedPlayers.containsKey(player);
    }

    public ArrayList<UUID> getPlayerQuits() {
        return playerQuits;
    }

    public Main getPlugin() {
        return plugin;
    }

    public class CombatEvents implements Listener {
        private final CombatManager manager;

        public CombatEvents(CombatManager manager) {
            this.manager = manager;
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();

            if (this.manager.isTagged(player)) {
                this.manager.setTagged(player, false);
                this.manager.getPlayerQuits().add(player.getUniqueId());
                player.setHealth(0);
                Bukkit.broadcastMessage(StringDefaults.PREFIX + "§cDer Spieler §7" + player.getName() + " §chat sich im Kampf ausgeloggt.");
            }
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            if (this.manager.getPlayerQuits().contains(player.getUniqueId())) {
                player.sendMessage(StringDefaults.PREFIX + "§cDu hast dich im Kampf ausgeloggt.");
            }
        }

        @EventHandler
        public void onEntityDamage(EntityDamageByEntityEvent event) {
            if (event.isCancelled() || !(event.getEntity() instanceof Player)) return;

            Player player = (Player) event.getEntity();

            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();

                if (Main.getInstance().getDuelManager().getDuelCache().containsKey(damager.getUniqueId()) || Main.getInstance().getDuelManager().getDuelCache().containsKey(player.getUniqueId())) {
                    return;
                }

                if (damager == player) return;

                if (this.manager.isTagged(damager))
                    updateTime(damager);
                else {
                    setTagged(damager, true);
                    damager.sendMessage(StringDefaults.PREFIX + "§cDu bist nun im Kampf! Ausloggen führt zm Tod.");
                }

                if (this.manager.isTagged(player))
                    updateTime(player);
                else {
                    setTagged(player, true);
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist nun im Kampf! Ausloggen führt zm Tod.");
                }

            }

            if (event.getDamager() instanceof Projectile) {
                Projectile pro = (Projectile) event.getDamager();

                if (!(pro.getShooter() instanceof Player)) {
                    return;
                }
                Player damager = (Player) pro.getShooter();

                if (Main.getInstance().getDuelManager().getDuelCache().containsKey(damager.getUniqueId()) || Main.getInstance().getDuelManager().getDuelCache().containsKey(player.getUniqueId())) {
                    return;
                }

                if (damager == player) return;
                if (isTagged(damager)) {
                    updateTime(damager);
                } else {
                    setTagged(damager, true);
                    damager.sendMessage(StringDefaults.PREFIX + "§cDu bist nun im Kampf! Ausloggen führt zm Tod.");
                }

                if (isTagged(player)) {
                    updateTime(player);
                } else {
                    setTagged(damager, true);
                    damager.sendMessage(StringDefaults.PREFIX + "§cDu bist nun im Kampf! Ausloggen führt zm Tod.");
                }
            }

        }

        @EventHandler
        public void onDeath(PlayerDeathEvent event) {
            Player player = event.getEntity();

            if (this.manager.isTagged(player))
                this.manager.setTagged(player, false);
        }

        @EventHandler
        public void onConsume(PlayerItemConsumeEvent event) {
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItem();

            if (this.manager.isTagged(player)) {
                if (itemStack.getType().equals(Material.POTION)) {
                    PotionMeta meta = (PotionMeta) itemStack.getItemMeta();

                    for (PotionEffect effect : meta.getCustomEffects()) {
                        if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
                            event.setCancelled(true);
                            break;
                        }
                    }
                }

            }
        }

        @EventHandler
        public void onPotionSplash(PotionSplashEvent event) {
            ThrownPotion potion = event.getPotion();

            for (PotionEffect effect : potion.getEffects()) {
                if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
                    List<Player> toRemove = new ArrayList<>();
                    for (LivingEntity entity : event.getAffectedEntities()) {
                        if (!(entity instanceof Player)) continue;

                        Player target = (Player) entity;
                        if (this.manager.isTagged(target))
                            toRemove.add(target);
                    }
                    event.getAffectedEntities().removeAll(toRemove);
                }
            }
        }

        @EventHandler
        public void onGamemodeChange(PlayerGameModeChangeEvent event) {
            Player player = event.getPlayer();
            if (this.manager.isTagged(player) && !player.hasPermission("arisemc.combat.admin"))
                event.setCancelled(true);
        }

        @EventHandler
        public void onTeleport(PlayerTeleportEvent event) {
            Player player = event.getPlayer();

            if (this.manager.isTagged(player) && !player.hasPermission("arisemc.combat.admin"))
                event.setCancelled(true);
        }

        @EventHandler
        public void onToggleFlight(PlayerToggleFlightEvent event) {
            Player player = event.getPlayer();
            if (this.manager.isTagged(player) && player.getGameMode() != GameMode.CREATIVE && !player.hasPermission("arisemc.combat.admin"))
                event.setCancelled(true);
        }

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent event) {
            Player player = event.getPlayer();
            String command = event.getMessage().substring(1).toLowerCase();

            if (this.manager.isTagged(player) && !player.hasPermission("arisemc.combat.admin")) {
                for (String blocked : this.manager.getPlugin().getFileManager().getConfigFile().getBlockedCombatCommands()) {
                    if (command.startsWith(blocked)) {
                        event.setCancelled(true);
                        player.sendMessage(StringDefaults.PREFIX + "§cDieser Befehl ist im Kampf nicht ausführbar.");
                    }
                }
            }
        }
    }

}
