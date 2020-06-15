/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.base;

import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.user.User;
import org.bukkit.event.Event;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.function.Consumer;

public abstract class AbstractAchievement {
    public abstract void onEvent(Event event);

}
