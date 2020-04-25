/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.database;

import java.util.HashMap;
import java.util.Map;

public class HandlerGroups {
    private static HashMap<String, HandlerGroup> handlerGroups = new HashMap<>();

    public static void addGroup(String name, long executeDelay) {
        if (handlerGroups.containsKey(name))
            return;
        HandlerGroup handlerGroup = new HandlerGroup(name, executeDelay);
        handlerGroups.put(name, handlerGroup);
    }

    public static void removeGroup(String name) {
        if (!handlerGroups.containsKey(name))
            return;
        HandlerGroup handlerGroup = handlerGroups.get(name);
        handlerGroup.stopThread();
        handlerGroups.remove(name);
    }

    public static boolean containsGroup(String name) {
        return handlerGroups.containsKey(name);
    }

    public static HandlerGroup getHandlerGroup(String name) {
        if (!handlerGroups.containsKey(name))
            addGroup(name, 15000L);
        return handlerGroups.get(name);
    }

    public static void stopAll() {
        for (Map.Entry<String, HandlerGroup> entryGroups : handlerGroups.entrySet()) {
            entryGroups.getValue().stopThread();
        }
    }

    public static HashMap<String, HandlerGroup> getHandlerGroups() {
        return handlerGroups;
    }
}
