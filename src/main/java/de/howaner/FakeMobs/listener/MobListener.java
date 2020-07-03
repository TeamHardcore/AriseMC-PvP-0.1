package de.howaner.FakeMobs.listener;

import de.howaner.FakeMobs.FakeMobsPlugin;
import de.howaner.FakeMobs.adjuster.MyWorldAccess;
import de.howaner.FakeMobs.event.PlayerInteractFakeMobEvent;
import de.howaner.FakeMobs.event.PlayerInteractFakeMobEvent.Action;
import de.howaner.FakeMobs.util.FakeMob;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class MobListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInit(WorldInitEvent event) {
        MyWorldAccess.registerWorldAccess(event.getWorld());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldUnload(WorldUnloadEvent event) {
        MyWorldAccess.unregisterWorldAccess(event.getWorld());
    }

    @EventHandler
    public void onSelectMob(PlayerInteractFakeMobEvent event) {
        Player player = event.getPlayer();
        FakeMob mob = event.getMob();

        if (event.getAction() == Action.RIGHT_CLICK && mob.haveShop())
            mob.getShop().openShop(player, (mob.getCustomName() != null && !mob.getCustomName().isEmpty()) ? mob.getCustomName() : null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(FakeMobsPlugin.getInstance(), () -> FakeMobsPlugin.updatePlayerView(player), 5L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getWorld() == event.getTo().getWorld() &&
                event.getFrom().getBlockX() == event.getTo().getBlockX() &&
                event.getFrom().getBlockY() == event.getTo().getBlockY() &&
                event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        Player player = event.getPlayer();
        if (player.getHealth() <= 0.0D) return;

        Chunk oldChunk = event.getFrom().getChunk();
        Chunk newChunk = event.getTo().getChunk();

        if (oldChunk.getWorld() != newChunk.getWorld() || oldChunk.getX() != newChunk.getX() || oldChunk.getZ() != newChunk.getZ()) {
            FakeMobsPlugin.updatePlayerView(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FakeMobsPlugin.updatePlayerView(player);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        FakeMobsPlugin.updatePlayerView(player);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location loc = event.getBlock().getLocation();
        if (FakeMobsPlugin.isMobOnLocation(loc))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (FakeMob mob : FakeMobsPlugin.getMobs())
            mob.unloadPlayer(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(FakeMobsPlugin.getInstance(), () -> FakeMobsPlugin.updatePlayerView(player), 5L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        for (FakeMob mob : FakeMobsPlugin.getMobs())
            mob.unloadPlayer(player);
    }

}
