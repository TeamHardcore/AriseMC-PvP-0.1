/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;

import de.teamhardcore.pvp.database.TimedDatabaseUpdate;
import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.model.achievements.base.AbstractTieredAchievement;
import de.teamhardcore.pvp.model.achievements.events.AchievementReceiveEvent;
import org.bukkit.Bukkit;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserAchievements extends TimedDatabaseUpdate {

    private final User user;
    private final Map<AbstractAchievement, Map<AchievementTier, Long>> achievementCache = new HashMap<>();
    private final Map<AbstractAchievement, Object> progress = new HashMap<>();

    public UserAchievements(User user) {
        this(user, true, true);
    }

    public UserAchievements(User user, boolean timedUpdate, boolean asyncLoad) {
        super("UserAchievements", timedUpdate, 30000L);
        setForceUpdate(true);

        this.user = user;

       /* if (asyncLoad)
            loadDataAsync();
        else loadData();*/
    }

    public void addAchievement(AbstractAchievement achievement) {
    }

    public boolean hasAchievement(AbstractAchievement achievement) {
        return this.achievementCache.containsKey(achievement);
    }

    public Object getProgress(AbstractAchievement achievement) {
        if (!this.progress.containsKey(achievement)) return 0L;
        return this.progress.get(achievement);
    }

    public boolean containsProgress(AbstractAchievement achievement) {
        return this.progress.containsKey(achievement);
    }

    public Object putProgress(AbstractAchievement achievement, Object object) {
        if (!(achievement instanceof AbstractTieredAchievement)) return null;
        if (!(object instanceof Serializable)) {
            try {
                throw new NotSerializableException("Object " + achievement.getClass().getSimpleName() + " ist not serializeable");
            } catch (NotSerializableException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        setUpdate(true);
        this.progress.put(achievement, object);
        return this.progress.get(achievement);
    }

    public Object removeProgress(AbstractAchievement achievement) {
        Object object = this.progress.remove(achievement);

        if (object != null) setUpdate(true);
        return object;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void saveData() {

    }

    @Override
    public void loadData() {

    }

    @Override
    public void deleteData() {

    }
}
