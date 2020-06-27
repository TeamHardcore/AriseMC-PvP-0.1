package de.howaner.FakeMobs;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.collect.Multimap;
import de.howaner.FakeMobs.adjuster.MyWorldAccess;
import de.howaner.FakeMobs.event.RemoveFakeMobEvent;
import de.howaner.FakeMobs.event.SpawnFakeMobEvent;
import de.howaner.FakeMobs.listener.MobListener;
import de.howaner.FakeMobs.listener.ProtocolListener;
import de.howaner.FakeMobs.merchant.ReflectionUtils;
import de.howaner.FakeMobs.util.FakeMob;
import de.howaner.FakeMobs.util.LookUpdate;
import de.howaner.FakeMobs.util.SkinQueue;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class FakeMobsPlugin {

    private static final Map<Integer, FakeMob> mobs = new HashMap<>();

    private static JavaPlugin instance;
    private static ProtocolListener pListener;
    private static SkinQueue skinQueue;

    public static void onEnable(JavaPlugin instance) {
        if (FakeMobsPlugin.instance != null) return;

        FakeMobsPlugin.instance = instance;

        skinQueue = new SkinQueue();
        skinQueue.start();


        Bukkit.getPluginManager().registerEvents(new MobListener(), instance);

        for (Player player : Bukkit.getOnlinePlayers())
            updatePlayerView(player);

        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, new LookUpdate(), 5L, 5L);
        ProtocolLibrary.getProtocolManager().addPacketListener((pListener = new ProtocolListener()));

        for (World world : Bukkit.getWorlds()) {
            MyWorldAccess.registerWorldAccess(world);
        }

    }

    public static void onDisable(JavaPlugin instance) {
        ProtocolLibrary.getProtocolManager().removePacketListener(pListener);

        for (FakeMob mob : getMobs())
            for (Player player : Bukkit.getOnlinePlayers())
                if (mob.getWorld() == player.getWorld())
                    mob.sendDestroyPacket(player);

        for (World world : Bukkit.getWorlds()) {
            MyWorldAccess.unregisterWorldAccess(world);
        }

    }

    public static SkinQueue getSkinQueue() {
        return skinQueue;
    }

    public static boolean existsMob(int id) {
        return mobs.containsKey(id);
    }

    public static FakeMob getMob(Location loc) {
        for (FakeMob mob : getMobs()) {
            if (mob.getLocation().getWorld() == loc.getWorld() &&
                    mob.getLocation().getBlockX() == loc.getBlockX() &&
                    mob.getLocation().getBlockY() == loc.getBlockY() &&
                    mob.getLocation().getBlockZ() == loc.getBlockZ())
                return mob;
        }
        return null;
    }

    public static boolean isMobOnLocation(Location loc) {
        return (getMob(loc) != null);
    }

    public static FakeMob getMob(int id) {
        return mobs.get(id);
    }

    public static void removeMob(int id) {
        FakeMob mob = mobs.get(id);
        if (mob == null) return;

        RemoveFakeMobEvent event = new RemoveFakeMobEvent(mob);
        Bukkit.getPluginManager().callEvent(event);

        for (Player player : mob.getWorld().getPlayers()) {
            mob.unloadPlayer(player);
        }

        mobs.remove(id);
    }

    public static FakeMob spawnMob(Location loc, EntityType type) {
        return spawnMob(loc, type, null);
    }

    public static FakeMob spawnMob(Location loc, EntityType type, String customName) {
        int id = getNewId();
        FakeMob mob = new FakeMob(id, loc, type);
        mob.setCustomName(customName);

        SpawnFakeMobEvent event = new SpawnFakeMobEvent(loc, type, mob);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return null;

        for (Player player : loc.getWorld().getPlayers()) {
            if (mob.isInRange(player)) {
                mob.loadPlayer(player);
            }
        }

        mobs.put(id, mob);
        return mob;
    }

    public static FakeMob spawnPlayer(Location loc, String name) {
        return spawnPlayer(loc, name, (Multimap<String, WrappedSignedProperty>) null);
    }

    public static FakeMob spawnPlayer(Location loc, String name, Player skin) {
        return spawnPlayer(loc, name, WrappedGameProfile.fromPlayer(skin).getProperties());
    }

    public static FakeMob spawnPlayer(Location loc, String name, Multimap<String, WrappedSignedProperty> skin) {
        int id = getNewId();
        FakeMob mob = new FakeMob(id, loc, EntityType.PLAYER);
        mob.setCustomName(name);
        mob.setPlayerSkin(skin);

        SpawnFakeMobEvent event = new SpawnFakeMobEvent(loc, EntityType.PLAYER, mob);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return null;

        for (Player player : loc.getWorld().getPlayers()) {
            if (mob.isInRange(player)) {
                mob.loadPlayer(player);
            }
        }

        mobs.put(id, mob);
        return mob;
    }

    public static int getNewId() {
        int id = -1;
        for (FakeMob mob : getMobs())
            if (mob.getId() > id)
                id = mob.getId();
        return id + 1;
    }

    public static List<FakeMob> getMobs() {
        return new ArrayList<>(mobs.values());
    }

    public static Map<Integer, FakeMob> getMobsMap() {
        return mobs;
    }

    public static void updatePlayerView(Player player) {
        for (FakeMob mob : getMobs()) {
            if (mob.isInRange(player)) {
                mob.loadPlayer(player);
            } else {
                mob.unloadPlayer(player);
            }
        }
    }

    public static List<FakeMob> getMobsInRadius(Location loc, int radius) {
        List<FakeMob> mobList = new ArrayList<>();
        for (FakeMob mob : getMobs()) {
            if (mob.getWorld() == loc.getWorld() && mob.getLocation().distance(loc) <= radius) {
                mobList.add(mob);
            }
        }

        return mobList;
    }

    public static List<FakeMob> getMobsInChunk(World world, int chunkX, int chunkZ) {
        List<FakeMob> mobList = new ArrayList<FakeMob>();

        for (FakeMob mob : getMobs()) {
            Chunk chunk = mob.getLocation().getChunk();
            if (mob.getWorld() == world && chunk.getX() == chunkX && chunk.getZ() == chunkZ) {
                mobList.add(mob);
            }
        }

        return mobList;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static void adjustEntityCount() {
        try {
            Class entityClass = Class.forName(ReflectionUtils.getNMSPackageName() + ".Entity");

            Field field = entityClass.getDeclaredField("entityCount");
            field.setAccessible(true);
            int currentCount = field.getInt(null);

            if (currentCount >= 2300) {
                while (existsMob(currentCount - 2300)) {
                    currentCount++;
                }

                field.set(null, currentCount);
            }
        } catch (Exception ex) {
            instance.getLogger().log(Level.WARNING, "Can't adjust entity count", ex);
        }
    }

}
