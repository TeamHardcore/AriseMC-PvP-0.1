/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

import de.teamhardcore.pvp.database.CompletionState;
import de.teamhardcore.pvp.database.CompletionStateImpl;
import de.teamhardcore.pvp.model.Home;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHomes implements CompletionState {

    private User user;
    private boolean asyncLoad;
    private Map<String, Home> homes;

    private CompletionState completionState;

    public UserHomes(User user) {
        this(user, true);
    }

    public UserHomes(User user, boolean asyncLoad) {
        this.user = user;
        this.asyncLoad = asyncLoad;
        this.completionState = new CompletionStateImpl();

        this.homes = new HashMap<>();
        loadHomes(asyncLoad);
    }

    public Home getHome(String name) {
        if (this.homes == null || !this.homes.containsKey(name))
            return null;
        return this.homes.get(name);
    }

    public void addHome(String name, Location location, boolean async) {
        if (this.homes.containsKey(name)) return;
        Home home = new Home(this.user.getUuid(), name, location, System.currentTimeMillis(), -1L);
        this.homes.put(name, home);
        //todo: do database stuff
    }

    public void removeHome(String name, boolean async) {
        if (!this.homes.containsKey(name)) return;
        this.homes.remove(name);
        //todo: do database stuff
    }

    public void updateLastTeleportTime(String name, boolean async) {
        if (!this.homes.containsKey(name)) return;
        Home home = getHome(name);
        //todo: do database stuff
    }

    private void loadHomes(boolean async) {
        this.completionState.setReady(true);
    }

    public User getUser() {
        return user;
    }

    public Map<String, Home> getHomes() {
        return homes;
    }

    @Override
    public List<Runnable> getReadyExecutors() {
        return this.completionState.getReadyExecutors();
    }

    @Override
    public void addReadyExecutor(Runnable paramRunnable) {
        this.completionState.addReadyExecutor(paramRunnable);
    }

    @Override
    public boolean isReady() {
        return this.completionState.isReady();
    }

    @Override
    public void setReady(boolean paramBoolean) {
        this.completionState.setReady(true);
    }
}
