/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.base;


import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.user.User;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractTieredAchievement extends AbstractAchievement {

    public AbstractTieredAchievement() {
    }

    public TreeMap<Long, AchievementTier> getTiersSorted() {
        return new TreeMap<>(getTiers());
    }

    public AchievementTier getCurrentTier(User user) {

        Object progress = user.getUserAchievements().getProgress(this);
        AchievementTier currentTier = null;

        for (Map.Entry<Long, AchievementTier> entry : this.getTiers().entrySet()) {
            if (entry.getKey() != null && progress instanceof Long) {
                if (currentTier == null || currentTier != entry.getValue() && (entry.getKey() <= (long) progress))
                    currentTier = entry.getValue();
            }
        }
        return currentTier;
    }

    public boolean hasRemainingTiers(User user) {

        Object progress = user.getUserAchievements().getProgress(this);

        for (Map.Entry<Long, AchievementTier> entry : this.getTiers().entrySet()) {
            if (entry.getKey() != null && progress instanceof Long) {
                if (entry.getKey() > (long) progress)
                    return true;
            }
        }
        return false;
    }

    public boolean hasTierUnlocked(User user, AchievementTier tier) {
        Object progress = user.getUserAchievements().getProgress(this);

        for (Map.Entry<Long, AchievementTier> entry : this.getTiers().entrySet())
            if (entry.getKey() != null && progress instanceof Long)
                if (entry.getValue().getName().equals(tier.getName()))
                    if (entry.getKey() <= (long) progress)
                        return true;
        return false;
    }

    public abstract Map<Long, AchievementTier> getTiers();
}
