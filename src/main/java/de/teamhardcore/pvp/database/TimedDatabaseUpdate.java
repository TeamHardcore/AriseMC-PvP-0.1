/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.database;

import de.teamhardcore.pvp.Main;

import java.util.List;

public abstract class TimedDatabaseUpdate extends AsyncTimedUpdate implements CompletionState {

    private boolean timedUpdate;
    private CompletionState completionState;

    public TimedDatabaseUpdate(String handlerName, boolean timedUpdate) {
        this(handlerName, timedUpdate, 10000L);
    }


    public TimedDatabaseUpdate(String handlerName, boolean timedUpdate, long updateDelay) {
        super(handlerName, updateDelay, timedUpdate);
        this.completionState = new CompletionStateImpl();
        this.timedUpdate = timedUpdate;
    }


    public boolean isTimedUpdate() {
        return this.timedUpdate;
    }


    public void setTimedUpdate(boolean timedUpdate) {
        if (!this.timedUpdate && timedUpdate) {
            if (!HandlerGroups.containsGroup(getHandlerName()))
                HandlerGroups.addGroup(getHandlerName(), getUpdateDelay());
            setHandlerGroup(HandlerGroups.getHandlerGroup(getHandlerName()));
            getHandlerGroup().addHandler(this);
        }
        if (this.timedUpdate && !timedUpdate &&
                getHandlerGroup().getHandlers().contains(this)) {
            getHandlerGroup().removeHandler(this);
        }
        this.timedUpdate = timedUpdate;
    }


    public void setUpdate(boolean state) {
        if (state && !this.timedUpdate) {
            saveData();
            return;
        }
        super.setUpdate(state);
    }


    public void saveDataAsync() {
        Main.getInstance().getDatabaseManager().getService().execute(this::saveData);
    }


    public void loadDataAsync() {
        Main.getInstance().getDatabaseManager().getService().execute(this::loadData);
    }


    public void deleteDataAsync() {
        Main.getInstance().getDatabaseManager().getService().execute(this::deleteData);
    }


    public void execute() {
        if (!isUpdate() && !isForceUpdate())
            return;
        saveData();
        setUpdate(false);
    }


    public void addReadyExecutor(Runnable exec) {
        this.completionState.addReadyExecutor(exec);
    }


    public List<Runnable> getReadyExecutors() {
        return this.completionState.getReadyExecutors();
    }


    public boolean isReady() {
        return this.completionState.isReady();
    }


    public void setReady(boolean ready) {
        this.completionState.setReady(ready);
    }

    public abstract void saveData();

    public abstract void loadData();

    public abstract void deleteData();
}
