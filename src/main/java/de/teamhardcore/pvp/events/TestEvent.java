/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.events;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class TestEvent extends BaseEvent implements CommandExecutor {

    public TestEvent(Main plugin) {
        super(plugin, "Test", "");
        Bukkit.getServer().getPluginCommand("testevent").setExecutor(this);
    }

    @Override
    public void run() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (getPlugin().getEventManager().getCurrentEvent().getRunningEvent() == null || !getPlugin().getEventManager().getCurrentEvent().getRunningEvent().equals(this)) {
            return true;
        }

        Player player = (Player) commandSender;
        player.sendMessage("Test");

        return true;
    }
}
