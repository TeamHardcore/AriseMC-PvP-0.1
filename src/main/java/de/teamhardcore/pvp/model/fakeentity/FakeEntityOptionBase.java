/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.fakeentity;

public class FakeEntityOptionBase extends AbstractOption {

    private final ExecutingState executingState;

    public FakeEntityOptionBase(ExecutingState executingState, String category, String optionName) {
        super(category, optionName);
        this.executingState = executingState;
    }

    public ExecutingState getExecutingState() {
        return executingState;
    }

    public boolean validateParams() {
        return true;
    }

    @Override
    public String toString() {
        return getExecutingState().name() + ": " + getCategory() + ":" + getOptionName();
    }

    public enum ExecutingState {
        CLICK,
        DESIGN;

        public static ExecutingState getByName(String name) {
            for (ExecutingState executingState : values()) {
                if (executingState.name().equalsIgnoreCase(name))
                    return executingState;
            }
            return null;
        }
    }
}
