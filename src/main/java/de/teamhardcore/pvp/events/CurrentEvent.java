/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.events;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;

public class CurrentEvent {

    private BaseEvent event;

    public CurrentEvent() {
        this.event = null;
    }

    public boolean isEventRunning() {
        return this.event != null;
    }

    public BaseEvent getRunningEvent() {
        return this.event;
    }

    public void startRunningEvent(BaseEvent event) {
        this.event = event;
        this.event.start();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(StringDefaults.GLOBAL_PREFIX + "§e§lEin neues Event wurde gestartet!");
            player.sendMessage(StringDefaults.GLOBAL_PREFIX + "§e§lBenutze §7§o/event §e§lum beizutreten.");
        });


    }

    public void stopRunningEvent() {
        if (!isEventRunning()) return;
        this.event.stop();
    }
}
