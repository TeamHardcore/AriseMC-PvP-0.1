/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.events;

import de.teamhardcore.pvp.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public abstract class BaseEvent implements Runnable {

    private final Main plugin;
    private final String eventName;
    private final String description;

    private ArrayList<UUID> participants;

    public BaseEvent(Main plugin, String eventName, String description) {
        this.plugin = plugin;
        this.eventName = eventName;
        this.description = description;
        this.participants = new ArrayList<>();

        run();
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void start();

    public abstract void stop();

    public Main getPlugin() {
        return plugin;
    }

    public String getEventName() {
        return eventName;
    }

    public ArrayList<UUID> getParticipants() {
        return participants;
    }
}
