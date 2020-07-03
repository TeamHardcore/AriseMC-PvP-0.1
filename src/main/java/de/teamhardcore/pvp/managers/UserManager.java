/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final Main plugin;

    private final Map<UUID, User> users = new HashMap<>();

    public UserManager(Main plugin) {
        this.plugin = plugin;
        startCheckStateTask();
    }

    private void startCheckStateTask() {
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            for (Player all : Bukkit.getOnlinePlayers()) {
                UserData userData = Main.getInstance().getUserManager().getUser(all.getUniqueId()).getUserData();
                userData.checkRewardState();
            }
        }, 20L, 20L);
    }

    public void addToCache(UUID uuid) {
        if (this.users.containsKey(uuid)) return;
        this.users.put(uuid, new User(uuid));
    }

    public void removeFromCache(UUID uuid) {
        if (!this.users.containsKey(uuid))
            return;

        User user = getUser(uuid);
        user.unload();

        //TODO: SAVE ALL
        this.users.remove(uuid);
    }

    public User getUser(UUID uuid) {
        if (!this.users.containsKey(uuid))
            addToCache(uuid);
        return this.users.get(uuid);
    }

    public Map<UUID, User> getUsers() {
        return users;
    }
}
