package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.SpyMode;
import de.teamhardcore.pvp.model.extras.EnumKilleffect;
import de.teamhardcore.pvp.model.extras.EnumPerk;
import de.teamhardcore.pvp.model.extras.effects.AbstractKillEffect;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.model.teleport.TPRequest;
import de.teamhardcore.pvp.utils.Reflection;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import de.teamhardcore.pvp.utils.particle.ParticleEffect;
import gnu.trove.TCollections;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Manager {

    public static final int ADDRESS_LIMIT = 3;

    private final Main plugin;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final HashMap<Player, Long> teleportCooldowns = new HashMap<>();
    private final HashMap<Player, TPRequest> teleportRequests = new HashMap<>();
    private final HashMap<Player, TPDelay> teleportDelays = new HashMap<>();
    private final HashMap<Player, Location> lastPositions = new HashMap<>();
    private final HashMap<Player, Long> healCooldown = new HashMap<>();
    private final HashMap<Player, Long> goldenAppleCooldown = new HashMap<>();
    private final HashMap<EnumPerk, Set<Player>> perkEffects = new HashMap<>();
    private final HashMap<UUID, SpyMode> commandSpyModeCache = new HashMap<>();
    private final HashMap<UUID, SpyMode> messageSpyModeCache = new HashMap<>();

    private final ArrayList<AbstractKillEffect> activeKilleffects = new ArrayList<>();
    private final ArrayList<Player> playerParticles = new ArrayList<>();
    private final ArrayList<Player> playersInEnderchest = new ArrayList<>();
    private final ArrayList<Player> playersInInvsee = new ArrayList<>();
    private final ArrayList<Player> playersInVanish = new ArrayList<>();

    private final TObjectIntMap<InetAddress> addresses = TCollections.synchronizedMap(new TObjectIntHashMap<>());

    public Manager(Main plugin) {
        this.plugin = plugin;

        for (EnumPerk perk : EnumPerk.values())
            this.perkEffects.put(perk, new HashSet<>());

        startTabListTask();
        startPerkUpdater();
        startAutoMessages();
        startParticleTask();
    }

    private void startParticleTask() {
        new BukkitRunnable() {
            private double times = 0.0D;

            @Override
            public void run() {
                for (Player player : getPlayerParticles()) {
                    this.times += 0.2617993877991494D;
                    double x = 0.8D * Math.cos(this.times);
                    double z = 0.8D * Math.sin(this.times);
                    double x2 = 0.8D * Math.cos(this.times + Math.PI);
                    double z2 = 0.8D * Math.sin(this.times + Math.PI);
                    Location loc = player.getLocation().clone().add(x, 2D, z);
                    Location loc2 = player.getLocation().clone().add(x2, 2D, z2);
                    ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, loc, 20);
                    ParticleEffect.FIREWORKS_SPARK.display(0.0F, 0.0F, 0.0F, 0.0F, 1, loc2, 20);
                    if (this.times >= 6.283185307179586D)
                        this.times = 0.0D;
                }
            }
        }.runTaskTimer(this.plugin, 20L, 20L);
    }

    private void startTabListTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> Bukkit.getOnlinePlayers().forEach(players -> Util.sendHeaderFooter(players,
                "\n " + StringDefaults.SERVER_NAME + " \n§a\n §7Es befinden sich §6" + this.plugin.getUserManager().getOnlinePlayers() + " Spieler §7auf dem Server \n",
                "\n§7Unsere Website§8: §6www.arisemc.de\n§7Unser TeamSpeak§8: §6ts.arisemc.de\n\n" + dateTimeFormatter.format(LocalDateTime.now()))), 20L, 20L);
    }

    private void startAutoMessages() {
        if (this.plugin.getFileManager().getConfigFile().getAutoMessages().isEmpty()) return;

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§c§lINFO§8: §7" + getPlugin().getFileManager().getConfigFile().getAutoMessages().get(counter)));
                this.counter = (this.counter >= getPlugin().getFileManager().getConfigFile().getAutoMessages().size() - 1) ? 0 : (this.counter + 1);
            }
        }.runTaskTimer(this.plugin, 0L, 3600L);
    }

    private void startPerkUpdater() {
        Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            for (Set<Player> players : this.perkEffects.values())
                for (Player player : players)
                    refreshPerkEffects(player);
        }, 200L, 200L);
    }

    public void vanishAll(Player player) {
        if (this.playersInVanish.contains(player)) return;
        this.playersInVanish.add(player);
        Bukkit.getOnlinePlayers().forEach(players -> {
            if (!players.hasPermission("arisemc.vanish.see") && players != player)
                players.hidePlayer(player);
        });
    }

    public void unVanishAll(Player player) {
        if (!this.playersInVanish.contains(player)) return;
        this.playersInVanish.remove(player);
        Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
    }

    public void addPerkEffect(Player player, EnumPerk perk) {
        if (!this.perkEffects.containsKey(perk)) return;

        Set<Player> players = this.perkEffects.get(perk);
        if (players.contains(player)) return;
        players.add(player);
        refreshPerkEffects(player);
    }

    public void removePerkEffect(Player player, EnumPerk perk) {
        if (!this.perkEffects.containsKey(perk)) return;
        Set<Player> players = this.perkEffects.get(perk);
        if (!players.contains(player)) return;
        players.remove(player);
    }

    public void refreshPerkEffects(Player player) {
        for (Map.Entry<EnumPerk, Set<Player>> entry : this.perkEffects.entrySet()) {
            if (!entry.getValue().contains(player)) continue;
            if (entry.getKey().getType() != null) {
                EnumPerk perk = entry.getKey();
                PotionEffect effect = new PotionEffect(perk.getType(), Integer.MAX_VALUE, perk.getAmplifier(), true);
                player.addPotionEffect(effect);
            }
        }
    }

    public void stopKillEffect(AbstractKillEffect effect) {
        effect.stopEffect();
        this.activeKilleffects.remove(effect);
    }

    public void playKillEffect(EnumKilleffect enumEffect, Player killed) {
        AbstractKillEffect effect = (AbstractKillEffect) Reflection.newInstance(Reflection.getConstructor(enumEffect.getEffectClass(), Player.class), new Object[]{killed});
        this.activeKilleffects.add(effect);
        effect.playEffect();
    }

    private void stopKillEffects() {
        for (AbstractKillEffect effect : this.activeKilleffects)
            stopKillEffect(effect);
    }

    public ArrayList<AbstractKillEffect> getActiveKilleffects() {
        return activeKilleffects;
    }

    public HashMap<Player, Long> getGoldenAppleCooldown() {
        return goldenAppleCooldown;
    }

    public HashMap<Player, Long> getTeleportCooldowns() {
        return teleportCooldowns;
    }

    public HashMap<Player, TPRequest> getTeleportRequests() {
        return teleportRequests;
    }

    public HashMap<Player, TPDelay> getTeleportDelays() {
        return teleportDelays;
    }

    public HashMap<Player, Location> getLastPositions() {
        return lastPositions;
    }

    public HashMap<Player, Long> getHealCooldown() {
        return healCooldown;
    }

    public ArrayList<Player> getPlayersInEnderchest() {
        return playersInEnderchest;
    }

    public ArrayList<Player> getPlayersInVanish() {
        return playersInVanish;
    }

    public ArrayList<Player> getPlayersInInvsee() {
        return playersInInvsee;
    }

    public HashMap<UUID, SpyMode> getCommandSpyModeCache() {
        return commandSpyModeCache;
    }

    public HashMap<UUID, SpyMode> getMessageSpyModeCache() {
        return messageSpyModeCache;
    }

    public ArrayList<Player> getPlayerParticles() {
        return playerParticles;
    }

    public TObjectIntMap<InetAddress> getAddresses() {
        return addresses;
    }

    public Main getPlugin() {
        return plugin;
    }
}
