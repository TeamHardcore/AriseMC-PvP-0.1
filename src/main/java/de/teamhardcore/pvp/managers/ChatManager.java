/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.GlobalmuteTier;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ChatManager {

    private final Main plugin;

    private GlobalmuteTier globalmuteTier = GlobalmuteTier.NONE;

    private Map<Player, Player> lastMessageContacts = new HashMap<>();

    public ChatManager(Main plugin) {
        this.plugin = plugin;
    }

    public GlobalmuteTier getGlobalmuteTier() {
        return globalmuteTier;
    }

    public void setGlobalmuteTier(GlobalmuteTier globalmuteTier) {
        this.globalmuteTier = globalmuteTier;
    }

    public Map<Player, Player> getLastMessageContacts() {
        return lastMessageContacts;
    }

    public Main getPlugin() {
        return plugin;
    }
}
