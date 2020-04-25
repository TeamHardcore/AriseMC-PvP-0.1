/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.database;

import java.util.List;

public interface CompletionState {

    List<Runnable> getReadyExecutors();

    void addReadyExecutor(Runnable paramRunnable);

    boolean isReady();

    void setReady(boolean paramBoolean);

}
