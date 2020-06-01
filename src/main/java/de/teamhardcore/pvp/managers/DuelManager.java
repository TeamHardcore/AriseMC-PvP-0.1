/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.model.duel.configuration.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.arena.DuelArena;
import de.teamhardcore.pvp.model.duel.arena.DuelArenaType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class DuelManager {

    private final Main plugin;

    private final Map<UUID, DuelConfiguration> configurationCache;
    private final Map<UUID, Duel> duelCache;

    public DuelManager(Main plugin) {
        this.plugin = plugin;
        this.configurationCache = new HashMap<>();
        this.duelCache = new HashMap<>();

        registerPacketListener();
    }

    private void registerPacketListener() {
        ProtocolManager m = ProtocolLibrary.getProtocolManager();
        m.addPacketListener(new PacketAdapter(Main.getInstance(), ListenerPriority.HIGHEST, PacketType.Play.Client.BLOCK_PLACE, PacketType.Play.Client.BLOCK_DIG) {
            @Override
            public void onPacketReceiving(PacketEvent event) {

                Player player = event.getPlayer();
                PacketContainer packetContainer = event.getPacket();

                Duel duel = DuelManager.this.duelCache.get(player.getUniqueId());

                if (duel == null) return;

                if (!duel.getWallBlocks().containsKey(player)) return;

                List<Location> visibleBlocks = duel.getWallBlocks().get(player);
                BlockPosition position = packetContainer.getBlockPositionModifier().read(0);
                Location location = new Location(player.getWorld(), position.getX(), position.getY(), position.getZ());

                if (duel.getDuelWall().contains(location) && visibleBlocks.contains(location)) {
                    event.setCancelled(true);
                    if (packetContainer.getType() == PacketType.Play.Client.BLOCK_DIG) {
                        duel.sendBlockChange(player, location, Material.STAINED_GLASS, 14);
                        System.out.println("called duel 4");
                    }
                }
            }
        });
    }

    public void createDuel(DuelConfiguration configuration) {
        if (configuration.getPlayers().size() <= 1) return;

        Duel duel = new Duel(configuration);
        this.duelCache.put(duel.getPlayer().getUniqueId(), duel);
        this.duelCache.put(duel.getTarget().getUniqueId(), duel);
    }

    public void stopDuel(Duel duel) {
        for (Player player : duel.getConfiguration().getPlayers()) {
            duel.removeWall(player);
            this.duelCache.remove(player.getUniqueId());
        }
    }

    public Map<UUID, DuelConfiguration> getConfigurationCache() {
        return configurationCache;
    }

    public Map<UUID, Duel> getDuelCache() {
        return duelCache;
    }
}
