/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.events;

import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.utils.BaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class AchievementReceiveEvent extends BaseEvent implements Cancellable {

    private final Player player;
    private final AbstractAchievement achievement;
    private boolean canceled;

    public AchievementReceiveEvent(Player player, AbstractAchievement achievement) {
        this.player = player;
        this.achievement = achievement;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractAchievement getAchievement() {
        return achievement;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }
}
