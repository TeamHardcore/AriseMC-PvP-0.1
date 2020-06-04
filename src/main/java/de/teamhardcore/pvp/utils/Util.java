package de.teamhardcore.pvp.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Util {

    private static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

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

    public static String formatNumber(double number) {
        return numberFormat.format(number);
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

    public static void sendHeaderFooter(Player player, String header, String footer) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        WrappedChatComponent wrapHeader = WrappedChatComponent.fromText(header);
        WrappedChatComponent wrapFooter = WrappedChatComponent.fromText(footer);

        packet.getChatComponents().write(0, wrapHeader);
        packet.getChatComponents().write(1, wrapFooter);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void sendTitle(Player player, String title, EnumWrappers.TitleAction action, int fadeIn, int stay, int fadeOut) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TITLE);
        packet.getModifier().writeDefaults();
        packet.getTitleActions().write(0, action);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
        packet.getIntegers().write(0, fadeIn);
        packet.getIntegers().write(1, stay);
        packet.getIntegers().write(2, fadeOut);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortMapByValues(Map<K, V> map) {
        Comparator<K> valueComp = Comparator.comparing(map::get);
        Map<K, V> sorted = new TreeMap<>(valueComp);
        sorted.putAll(map);
        return sorted;
    }
}
