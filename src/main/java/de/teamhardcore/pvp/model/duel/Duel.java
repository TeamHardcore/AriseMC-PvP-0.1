/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.arena.DuelArena;
import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Duel {

    /*
     Knochen der Mobs
     Knochen eines Zombies (Beispiel)
     Benötigt um Spawn-Eier freizuschalten.

     Merchant/Transmog (Prefix) System für Arena(ArenaPunkte bei Ranked Arena)

     Eine Zone am Spawn, die ein Clan einnehmen kann für Belohnungen (Stronghold)

     */
    private final DuelConfiguration configuration;
    private final List<Player> alive;
    private final Map<Player, AtomicInteger> remainingPotions;

    private Player player, target;

    private BukkitTask startTask, gameTask, endTask;
    private DuelArena arena;

    private int startCounter = 5, gameCounter = 300, endCounter = 2;

    public Duel(DuelConfiguration configuration, DuelArena arena) {
        this.configuration = configuration;
        this.arena = arena;

        this.remainingPotions = new HashMap<>();

        this.alive = new ArrayList<>();
        this.alive.addAll(this.configuration.getPlayers());

        this.player = this.configuration.getPlayers().get(0);
        this.target = this.configuration.getPlayers().get(1);

        preparePlayer(this.player);
        preparePlayer(this.target);

        sendMessage(StringDefaults.DUEL_PREFIX + "§aDas Duell beginnt in Kürze.");

        this.startTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (startCounter <= 0) {
                    startTask.cancel();
                    sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell hat begonnen.");
                  /*  player.teleport(arena.getSpawnLocation(0));
                    target.teleport(arena.getSpawnLocation(1));*/
                    startGameTask();
                    return;
                }

                player.sendTitle(new Title(String.valueOf(startCounter), "", 5, 10, 5));
                target.sendTitle(new Title(String.valueOf(startCounter), "", 5, 10, 5));
                sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell beginnt in §7" + startCounter + " §e" + (startCounter == 1 ? "Sekunde" : "Sekunden"));
                startCounter--;
            }
        }, 20L, 20L);
    }

    public void performDeath(Player player) {
        if (!this.configuration.getPlayers().contains(player)) return;
        if (!this.alive.contains(player)) return;
        this.alive.remove(player);

        player.setHealth(20);
        player.setVelocity(new Vector(0, 0, 0));

        player.getWorld().strikeLightningEffect(player.getLocation().add(0, 6, 0));
        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

        sendMessage(StringDefaults.DUEL_PREFIX + "§7" + player.getName() + " §eist gestorben.");
        checkDuel();
    }

    public void checkDuel() {
        if (this.alive.size() == 1) {
            Player last = this.alive.get(0);

            sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            sendMessage(StringDefaults.PREFIX + "§c§l" + last.getName() + " §ehat das Duell gewonnen.");
            sendMessage(" ");
            sendMessage(StringDefaults.PREFIX + "§6§lGewinn§8: ");
            if (this.configuration.getDeployment().getCoins() > 0)
                sendMessage("  §8■ §eMünzen§8: §a§l" + Util.formatNumber((this.configuration.getDeployment().getCoins() * 2)) + "$");
            if (this.configuration.getDeployment().isInventory())
                sendMessage("  §8■ §eInventar des Gegners");
            sendMessage(" ");
            sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            last.playSound(last.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            startEndTask();
            return;
        }
    }

    public void sendMessage(String message) {
        this.configuration.getPlayers().forEach(all -> all.sendMessage(message));
    }

    public void setRemainingPotions(Player player, int amount) {
        if (!this.remainingPotions.containsKey(player)) {
            int remain = this.configuration.getSettings().getMaxHealStacks() == -1 ? -1 : (int) (this.configuration.getSettings().getMaxHealStacks() * 64);
            this.remainingPotions.put(player, new AtomicInteger(remain));
        }

        this.remainingPotions.get(player).set(this.remainingPotions.get(player).get() - amount);

    }

    public boolean hasRemainingPotions(Player player) {
        if (this.configuration.getSettings().getMaxHealStacks() == -1)
            return true;

        return this.remainingPotions.get(player).get() >= 0;
    }

    public int getRemainingPotions(Player player) {
        if (!this.remainingPotions.containsKey(player)) {
            int remain = this.configuration.getSettings().getMaxHealStacks() == -1 ? -1 : (int) (this.configuration.getSettings().getMaxHealStacks() * 64);
            this.remainingPotions.put(player, new AtomicInteger(remain));
        }

        return this.remainingPotions.get(player).get();
    }

    private void preparePlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(30);
        player.setHealth(20);
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        player.setFireTicks(0);
        player.setFlying(false);
        player.setAllowFlight(false);
    }

    private void startGameTask() {
        this.gameTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (gameCounter <= 0) {
                    gameTask.cancel();

                    startEndTask();
                    return;
                }

                if (gameCounter == 240 || gameCounter == 180 || gameCounter == 120 || gameCounter == 60 || gameCounter == 30 || gameCounter == 10 || gameCounter == 5) {
                    sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §7" + gameCounter + " §eSekunden.");
                }
                gameCounter--;
            }
        }, 20L, 20L);
    }

    private void startEndTask() {
        this.endTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (endCounter <= 0) {
                    endTask.cancel();
                    Main.getInstance().getDuelManager().stopDuel(Duel.this);
                    return;
                }
                endCounter--;
            }
        }, 20L, 20L);
    }

    public BukkitTask getStartTask() {
        return startTask;
    }

    public BukkitTask getGameTask() {
        return gameTask;
    }

    public BukkitTask getEndTask() {
        return endTask;
    }

    public DuelArena getArena() {
        return arena;
    }

    public int getStartCounter() {
        return startCounter;
    }

    public int getGameCounter() {
        return gameCounter;
    }

    public int getEndCounter() {
        return endCounter;
    }

    public DuelConfiguration getConfiguration() {
        return configuration;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }
}
