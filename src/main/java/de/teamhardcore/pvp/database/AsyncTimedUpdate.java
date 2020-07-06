/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.database;

public abstract class AsyncTimedUpdate implements Handler {

    private boolean update;
    private boolean forceUpdate;
    private final long updateDelay;
    private final String handlerName;
    private HandlerGroup group;

    public AsyncTimedUpdate(String handlerName, long updateDelay, boolean update) {
        if (update) {
            if (!HandlerGroups.containsGroup(handlerName))
                HandlerGroups.addGroup(handlerName, updateDelay);
            this.group = HandlerGroups.getHandlerGroup(handlerName);
            this.group.addHandler(this);
        }
        this.updateDelay = updateDelay;
        this.handlerName = handlerName;
        this.update = false;
        this.forceUpdate = false;
    }

    public AsyncTimedUpdate(String handlerName, boolean update) {
        this(handlerName, 10000L, update);
    }


    public AsyncTimedUpdate(String handlerName) {
        this(handlerName, 10000L, true);
    }

    public HandlerGroup getHandlerGroup() {
        return this.group;
    }


    public void setHandlerGroup(HandlerGroup handlerGroup) {
        this.group = handlerGroup;
    }


    public boolean isUpdate() {
        return this.update;
    }


    public void setUpdate(boolean state) {
        this.update = state;
    }


    public boolean isForceUpdate() {
        return this.forceUpdate;
    }


    public void setForceUpdate(boolean state) {
        this.forceUpdate = state;
    }


    public long getUpdateDelay() {
        return this.updateDelay;
    }


    public String getHandlerName() {
        return this.handlerName;
    }

}
