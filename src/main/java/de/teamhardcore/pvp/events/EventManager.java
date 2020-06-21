/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.events;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final List<BaseEvent> availableEvents;

    private final Main plugin;
    private final CurrentEvent event;

    private Inventory eventInventory;

    public EventManager(Main plugin) {
        this.plugin = plugin;
        this.event = new CurrentEvent();
        this.availableEvents = new ArrayList<>();

        registerEvents();
        registerEventInventory();
        startUpdater();
    }

    private void registerEvents() {
        this.availableEvents.add(new TestEvent(this.plugin));
    }

    private void registerEventInventory() {
        this.eventInventory = Bukkit.createInventory(null, 9 * 3, "§c§lEvent");

        if (this.event.isEventRunning()) {
            return;
        }

    }

    private void startUpdater() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            if (!this.event.isEventRunning()) return;
            updateInventory(this.eventInventory);
        }, 20L, 20L);
    }

    public void openInventory(Player player) {
        player.openInventory(this.eventInventory);
        updateInventory(this.eventInventory);
    }

    private void updateInventory(Inventory inventory) {
        if (this.event.isEventRunning()) {
            inventory.setItem(14, new ItemBuilder(Material.SIGN).setDisplayName("§6§lINFORMATIONEN").setLore(
                    "§7Verbleibende Zeit§8: §e§l",
                    "§7Teilnehmer§8: §e§l" + this.event.getRunningEvent().getParticipants().size() + " Spieler",
                    "",
                    "§7Event§8: §c§l" + this.event.getRunningEvent().getEventName()).build());

            inventory.setItem(12, new ItemBuilder(Material.INK_SACK).setDurability(1).setDisplayName("§c§lAktuelles Event beenden").build());
            return;
        }

        inventory.setItem(17, new ItemBuilder(Material.SIGN).setDisplayName("§c§lHOW TO").setLore("§7Klicke auf ein Event-Item,", "§7um ein neues Event zu starten.", "", "§7Das Event startet danach automatisch,", "§7es wird keine Bestätigung benötigt.").build());

        int slot = 10;
        for (BaseEvent event : this.availableEvents) {
            if (slot >= 14) break;
            inventory.setItem(slot, new ItemBuilder(Material.PAINTING).setDisplayName("§a§l" + event.getEventName()).build());
            slot++;
        }
    }

    public CurrentEvent getCurrentEvent() {
        return event;
    }
}
