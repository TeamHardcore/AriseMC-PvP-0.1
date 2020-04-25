package de.teamhardcore.pvp.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Util {

    public static String locationToString(Location location) {
        if (location == null)
            return "";

        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();

    }

    public static Location stringToLocation(String string) {
        if (string == null || string.trim().equals(""))
            return null;

        final String[] parts = string.split(":");

        if (parts.length == 6)
            return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]));
        return null;
    }

    public static void sendActionbar(Player player, String message) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CHAT);
        packet.getBytes().write(0, (byte) 2);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(message));
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortMapByValues(Map<K, V> map) {
        Comparator<K> valueComp = Comparator.comparing(map::get);
        Map<K, V> sorted = new TreeMap<>(valueComp);
        sorted.putAll(map);
        return sorted;
    }
}
