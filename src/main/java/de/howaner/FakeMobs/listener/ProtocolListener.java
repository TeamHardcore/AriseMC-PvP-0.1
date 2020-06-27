package de.howaner.FakeMobs.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.GamePhase;
import de.howaner.FakeMobs.FakeMobsPlugin;
import de.howaner.FakeMobs.event.PlayerInteractFakeMobEvent;
import de.howaner.FakeMobs.event.PlayerInteractFakeMobEvent.Action;
import de.howaner.FakeMobs.util.FakeMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public class ProtocolListener implements PacketListener {

    @Override
    public void onPacketSending(PacketEvent pe) {
    }

    @Override
    public void onPacketReceiving(PacketEvent pe) {
        PacketContainer packet = pe.getPacket();
        final Player player = pe.getPlayer();

        if (packet.getType() == PacketType.Play.Client.USE_ENTITY) {
            int id = packet.getIntegers().read(0) - 2300;

            if (id < 0) return;
            final FakeMob mob = FakeMobsPlugin.getMob(id);
            if (mob == null || player.getWorld() != mob.getWorld()) return;

            if (player.isDead()) return;
            if (player.getLocation().distance(mob.getLocation()) > 8) {
                return;
            }

            final Action action;
            try {
                Field field = packet.getEntityUseActions().getField(0);
                field.setAccessible(true);
                Object obj = field.get(packet.getEntityUseActions().getTarget());
                String actionName = (obj == null) ? "" : obj.toString();

                if (actionName.equals("INTERACT")) {
                    action = Action.RIGHT_CLICK;
                } else if (actionName.equals("ATTACK")) {
                    action = Action.LEFT_CLICK;
                } else {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            pe.setCancelled(true);
            Bukkit.getScheduler().runTask(FakeMobsPlugin.getInstance(), () -> {
                PlayerInteractFakeMobEvent event = new PlayerInteractFakeMobEvent(player, mob, action);
                Bukkit.getPluginManager().callEvent(event);
            });
        }
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return ListeningWhitelist.newBuilder().
                priority(ListenerPriority.NORMAL).
                types(PacketType.Play.Client.USE_ENTITY).
                gamePhase(GamePhase.PLAYING).
                options(new ListenerOptions[0]).
                build();
    }

    @Override
    public Plugin getPlugin() {
        return FakeMobsPlugin.getInstance();
    }

}
