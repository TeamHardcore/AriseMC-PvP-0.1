/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserData {

    private final User user;

    private Set<UUID> friends;

    public UserData(User user) {
        this(user, true);
    }

    public UserData(User user, boolean async) {
        this.user = user;

        this.friends = new HashSet<>();
        saveDefaults(async);
        loadData(async);
    }

    public void addFriend(UUID uuid) {
        if (this.user.getUuid().equals(uuid)) return;
        if (this.friends.contains(uuid)) return;
        this.friends.add(uuid);
        saveData(this.user.getPlayer() != null);
    }

    public void removeFriend(UUID uuid) {
        if (this.user.getUuid().equals(uuid)) return;
        if (!this.friends.contains(uuid)) return;
        this.friends.remove(uuid);
        saveData(this.user.getPlayer() != null);
    }

    private void loadFriends(String json) {
        JSONArray mainArray = new JSONArray(json);
        for (Object friend : mainArray) {
            UUID uuid = UUID.fromString((String) friend);
            this.friends.add(uuid);
        }
    }

    private JSONArray saveFriends() {
        JSONArray array = new JSONArray();
        for (UUID friend : this.friends)
            array.put(friend.toString());
        return array;
    }

    public Set<UUID> getFriends() {
        return friends;
    }

    private void saveDefaults(boolean async) {

    }

    private void loadData(boolean async) {

    }

    private void saveData(boolean async) {

    }

    public User getUser() {
        return user;
    }
}
