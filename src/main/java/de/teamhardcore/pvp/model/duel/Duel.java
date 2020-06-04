/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.arena.DuelArena;
import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Duel {
    private final int WALL_REGION = 8;

    private final DuelConfiguration configuration;
    private final List<Player> alive;
    private final Map<Player, AtomicInteger> remainingPotions;
    private final Map<Player, List<Location>> wallBlocks;

    private final String gameID;

    private final DuelWall duelWall;
    private final Player player;
    private final Player target;
    private BukkitTask startTask, gameTask, endTask;
    private int startCounter = 5, gameCounter = 300, endCounter = 2;

    private boolean canPvP;

    public Duel(DuelConfiguration configuration, String gameId) {
        this.configuration = configuration;

        Location middle = configuration.getLocation().clone();
        Location min = middle.clone().add(this.WALL_REGION, 10, this.WALL_REGION);
        Location max = middle.clone().add(-this.WALL_REGION, -10, -this.WALL_REGION);

        this.duelWall = new DuelWall(max, min);

        this.wallBlocks = new HashMap<>();
        this.remainingPotions = new HashMap<>();

        this.gameID = gameId;

        this.alive = new ArrayList<>(this.configuration.getPlayers());
        this.player = this.configuration.getPlayers().get(0);
        this.target = this.configuration.getPlayers().get(1);

        this.canPvP = false;

        preparePlayer(this.player);
        preparePlayer(this.target);
        sendMessage(StringDefaults.DUEL_PREFIX + "§aDas Duell beginnt in Kürze.");
        this.startTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (startCounter <= 0) {
                    startTask.cancel();
                    sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell hat begonnen.");
                    startGameTask();
                    return;
                }
                Util.sendTitle(player, String.valueOf(startCounter), EnumWrappers.TitleAction.TITLE, 5, 10, 5);
                Util.sendTitle(target, String.valueOf(startCounter), EnumWrappers.TitleAction.TITLE, 5, 10, 5);
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
            sendMessage(" ");
            sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");

            UserMoney lastMoney = Main.getInstance().getUserManager().getUser(last.getUniqueId()).getUserMoney();
            lastMoney.addMoney(this.configuration.getDeployment().getCoins() * 2);

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
        if (this.startTask != null) {
            this.startTask.cancel();
            this.startTask = null;
        }

        this.canPvP = true;

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

    public void startEndTask() {
        if (this.startTask != null) {
            this.startTask.cancel();
            this.startTask = null;
        }

        if (this.gameTask != null) {
            this.gameTask.cancel();
            this.gameTask = null;
        }

        this.canPvP = false;

        this.endTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if (endCounter <= 0) {
                    endTask.cancel();
                    endTask = null;
                    Main.getInstance().getDuelManager().stopDuel(Duel.this);
                    return;
                }
                endCounter--;
            }
        }, 20L, 20L);
    }

    public boolean canPvP() {
        return this.canPvP;
    }

    public void updateWall(Player player) {
        if (this.duelWall == null || this.duelWall.getWorld() != player.getWorld())
            return;

        if (!this.wallBlocks.containsKey(player))
            this.wallBlocks.put(player, new CopyOnWriteArrayList<>());

        List<Location> visibleBlocks = this.wallBlocks.get(player);
        List<Location> locationsInRange = getLocationsInRange(player.getLocation(), 3, 2);

        for (Location visible : visibleBlocks) {
            if (!locationsInRange.contains(visible)) {
                Block b = visible.getBlock();
                visibleBlocks.remove(visible);
                sendBlockChange(player, visible, b.getType(), b.getData());
            }
        }

        for (Location loc : locationsInRange) {
            if (!this.duelWall.contains(loc) || visibleBlocks.contains(loc))
                continue;
            visibleBlocks.add(loc);
            sendBlockChange(player, loc, Material.STAINED_GLASS, 14);
        }
    }

    public void removeWall(Player player) {
        if (!this.wallBlocks.containsKey(player)) return;
        for (Location location : this.wallBlocks.get(player)) {
            Block block = location.getBlock();
            sendBlockChange(player, location, block.getType(), block.getData());
        }
        this.wallBlocks.remove(player);
    }

    public void sendBlockChange(Player p, Location loc, Material mat, int data) {
        ProtocolManager m = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = m.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        packet.getBlockPositionModifier().write(0, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.getBlockData().write(0, WrappedBlockData.createData(mat, data));
        try {
            m.sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private List<Location> getLocationsInRange(Location loc, int height, int width) {
        List<Location> locations = new ArrayList<>();
        World w = loc.getWorld();
        int startX = loc.getBlockX();
        int startY = loc.getBlockY();
        int startZ = loc.getBlockZ();
        for (int x = startX - width; x <= startX + width; x++) {
            for (int y = startY - height; y <= startY + height; y++) {
                for (int z = startZ - width; z <= startZ + width; z++) {
                    Block current = w.getBlockAt(x, y, z);
                    if (current != null && current.getType() == Material.AIR)
                        locations.add(current.getLocation());
                }
            }
        }
        return locations;
    }

    public DuelWall getDuelWall() {
        return duelWall;
    }

    public Map<Player, List<Location>> getWallBlocks() {
        return wallBlocks;
    }

    public DuelConfiguration getConfiguration() {
        return configuration;
    }

    public String getGameID() {
        return gameID;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }
}
