/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel;

import com.comphenix.protocol.wrappers.EnumWrappers;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.event.DuelWinEvent;
import de.teamhardcore.pvp.model.duel.map.DuelMap;
import de.teamhardcore.pvp.model.duel.phases.AbstractDuelPhase;
import de.teamhardcore.pvp.model.duel.phases.EndPhase;
import de.teamhardcore.pvp.model.duel.phases.StartPhase;
import de.teamhardcore.pvp.model.duel.request.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.request.DuelRequest;
import de.teamhardcore.pvp.utils.SimpleUID;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Duel {

    private final SimpleUID id;

    private final List<Player> alive;
    private final List<Player> players;

    private final DuelConfiguration configuration;
    private final DuelMap map;

    private final Map<Player, AtomicInteger> remainingPotions;

    private AbstractDuelPhase phase;

    public Duel(DuelConfiguration request) {
        this.configuration = request;
        this.id = new SimpleUID(8);

        this.remainingPotions = new HashMap<>();
        this.alive = new ArrayList<>(this.configuration.getPlayers());
        this.players = new ArrayList<>(this.configuration.getPlayers());


        if (this.configuration.getCurrentCategory().equals("random")) {
            List<String> availableCategories = new ArrayList<>(Main.getInstance().getDuelManager().getAvailableMaps().keySet());
            this.map = Main.getInstance().getDuelManager().getAvailableMap(availableCategories.get(new Random().nextInt(availableCategories.size())));
        } else
            this.map = Main.getInstance().getDuelManager().getAvailableMap(this.configuration.getCurrentCategory());

        if (this.map == null) {
            startPhase(new EndPhase(this));
            sendMessage(StringDefaults.DUEL_PREFIX + "§cEin Fehler ist aufgetreten, bitte melden!");
            sendMessage(StringDefaults.DUEL_PREFIX + "§cDas Duell wurde vorsichtshalber beendet.");
            return;
        }

        preparePlayers();

        sendMessage(StringDefaults.DUEL_PREFIX + "§aDas Duell beginnt in Kürze.");
        sendMessage(StringDefaults.DUEL_PREFIX + "§eGespielt wird auf der Map " + "§6§l" + this.map.getCategory());

        startPhase(new StartPhase(this));
    }

    private void preparePlayers() {
        this.alive.forEach(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(30);
            player.setHealth(20);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            player.setFireTicks(0);
            player.setFlying(false);
            player.setAllowFlight(false);
        });
    }

    public void performDeath(Player player) {
        if (!this.players.contains(player)) return;
        if (!this.alive.contains(player)) return;
        this.alive.remove(player);

        player.getWorld().strikeLightningEffect(player.getLocation().add(0, 6, 0));
        player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
        player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);

        sendMessage(StringDefaults.DUEL_PREFIX + "§7" + player.getName() + " §eist gestorben.");
        checkDuel();
    }

    public void checkDuel() {
        if (this.alive.size() == 1) {
            Player duelWinner = this.alive.get(0);
            Main.getInstance().getServer().getPluginManager().callEvent(new DuelWinEvent(duelWinner, this));
            getPhase().stop();
            startPhase(new EndPhase(this));
        }
    }

    public void setRemainingPotions(Player player, int amount) {
        if (!this.remainingPotions.containsKey(player)) {
            int remain = this.configuration.getCurrentPotionLimitation() == -1 ? -1 : (int) (this.configuration.getCurrentPotionLimitation() * 64);
            this.remainingPotions.put(player, new AtomicInteger(remain));
        }

        this.remainingPotions.get(player).set(this.remainingPotions.get(player).get() - amount);

    }

    public boolean hasRemainingPotions(Player player) {
        if (this.configuration.getCurrentPotionLimitation() == -1)
            return true;

        return this.remainingPotions.get(player).get() >= 0;
    }

    public int getRemainingPotions(Player player) {
        if (!this.remainingPotions.containsKey(player)) {
            int remain = this.configuration.getCurrentPotionLimitation() == -1 ? -1 : (int) (this.configuration.getCurrentPotionLimitation() * 64);
            this.remainingPotions.put(player, new AtomicInteger(remain));
        }

        return this.remainingPotions.get(player).get();
    }

    public void sendMessage(String message) {
        this.players.forEach(player -> {
            player.sendMessage(message);
        });
    }

    public void sendTitle(String title, int fadeIn, int stay, int fadeOut) {
        this.players.forEach(player -> {
            Util.sendTitle(player, String.valueOf(title), EnumWrappers.TitleAction.TITLE, fadeIn, stay, fadeOut);
        });
    }

    public void startPhase(AbstractDuelPhase phase) {
        this.phase = phase;
    }

    public SimpleUID getId() {
        return id;
    }

    public List<Player> getAlive() {
        return alive;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public DuelMap getMap() {
        return map;
    }

    public DuelConfiguration getConfiguration() {
        return configuration;
    }

    public AbstractDuelPhase getPhase() {
        return phase;
    }
}
