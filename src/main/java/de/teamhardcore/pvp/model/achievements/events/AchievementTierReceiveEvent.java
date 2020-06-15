/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.events;

import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.utils.BaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class AchievementTierReceiveEvent extends BaseEvent implements Cancellable {

    private final Player player;
    private final AbstractAchievement achievement;
    private final AchievementTier receivedTier;
    private boolean canceled;

    public AchievementTierReceiveEvent(Player player, AchievementTier receivedTier, AbstractAchievement achievement) {
        this.player = player;
        this.receivedTier = receivedTier;
        this.achievement = achievement;
    }

    public AchievementTier getReceivedTier() {
        return receivedTier;
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
