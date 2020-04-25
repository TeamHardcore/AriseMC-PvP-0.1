/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.GlobalmuteTier;

public class ChatManager {

    private final Main plugin;

    private GlobalmuteTier globalmuteTier = GlobalmuteTier.NONE;

    public ChatManager(Main plugin) {
        this.plugin = plugin;
    }

    public GlobalmuteTier getGlobalmuteTier() {
        return globalmuteTier;
    }

    public void setGlobalmuteTier(GlobalmuteTier globalmuteTier) {
        this.globalmuteTier = globalmuteTier;
    }

    public Main getPlugin() {
        return plugin;
    }
}
