/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.database;

import java.util.ArrayList;
import java.util.List;

public class CompletionStateImpl implements CompletionState {
    private boolean ready;
    private List<Runnable> readyExecutors = new ArrayList<>();


    public List<Runnable> getReadyExecutors() {
        return this.readyExecutors;
    }


    public void addReadyExecutor(Runnable exec) {
        if (this.ready) {
            exec.run();
            return;
        }
        this.readyExecutors.add(exec);
    }


    public boolean isReady() {
        return this.ready;
    }


    public void setReady(boolean ready) {
        this.ready = ready;

        if (ready) {
            for (Runnable exec : this.readyExecutors) {
                exec.run();
            }
            this.readyExecutors.clear();
        }
    }
}
