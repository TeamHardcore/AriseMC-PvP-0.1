package de.teamhardcore.pvp.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.*;

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

    public static boolean isInventoryEmpty(Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents())
            if (itemStack != null)
                return false;
        return true;
    }

    public static int getAvailableItems(Inventory inv, ItemStack item) {
        int counted = 0;
        for (ItemStack content : inv.getContents()) {
            if (content != null && content.getType() != Material.AIR &&
                    content.getType() == item.getType() && content.getDurability() == item.getDurability()) {
                counted += content.getAmount();
            }
        }
        return counted;
    }

    public static boolean hasEnoughItems(Inventory inv, ItemStack item, int amount) {
        return (getAvailableItems(inv, item) >= amount);
    }

    public static boolean removeItems(Inventory inv, ItemStack item, int amount) {
        if (!hasEnoughItems(inv, item, amount)) {
            return false;
        }
        int toRemove = amount;

        HashMap<Integer, ItemStack> slots = new HashMap<>();

        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack slotItem = inv.getItem(slot);
            if (slotItem != null && slotItem.getType() != Material.AIR) {
                if (slotItem.getType() == item.getType() && slotItem.getDurability() == item.getDurability()) {
                    slots.put(slot, slotItem);
                }
            }
        }
        for (Map.Entry<Integer, ItemStack> entrySlots : slots.entrySet()) {
            if ((entrySlots.getValue()).getAmount() <= toRemove) {
                inv.setItem(entrySlots.getKey(), new ItemStack(Material.AIR));
                toRemove -= (entrySlots.getValue()).getAmount();
                continue;
            }
            ItemStack invItem = inv.getItem(entrySlots.getKey());
            invItem.setAmount(invItem.getAmount() - toRemove);
        }


        return true;
    }

    public static void addItem(Player p, ItemStack item) {
        if (p.getInventory().firstEmpty() == -1) {
            p.getWorld().dropItemNaturally(p.getLocation(), item);
        } else {
            p.getInventory().addItem(item);
        }
    }

    public static void addItems(Player p, List<ItemStack> items) {
        for (ItemStack item : items)
            addItem(p, item);
    }

    public static void addItems(Player p, ItemStack... items) {
        for (ItemStack item : items) {
            addItem(p, item);
        }
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortMapByValues(Map<K, V> map) {
        Comparator<K> valueComp = Comparator.comparing(map::get);
        Map<K, V> sorted = new TreeMap<>(valueComp);
        sorted.putAll(map);
        return sorted;
    }
}
