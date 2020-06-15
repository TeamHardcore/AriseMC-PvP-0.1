/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements;

import com.google.common.collect.ImmutableSet;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementCategory;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementLoadId;
import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.utils.ClassUtil;

import java.util.*;

public class AchievementGroup {

    private final Category category;
    private final List<AbstractAchievement> achievements;

    public AchievementGroup(Category category) {
        this.category = category;
        this.achievements = new ArrayList<>();
        registerAchievements();
    }

    private void registerAchievements() {
        List<AbstractAchievement> achievements = new ArrayList<>();

        Collection<Class<?>> classes = new ArrayList<>();
        classes.addAll(ClassUtil.getClassesInPackage(Main.getInstance(), "de.teamhardcore.pvp.model.achievements.impl." + category.getPackageName() + ".tiered"));
        classes.addAll(ClassUtil.getClassesInPackage(Main.getInstance(), "de.teamhardcore.pvp.model.achievements.impl." + category.getPackageName() + ".challenges"));

        for (Class<?> clazz : classes) {
            AchievementCategory achievementCategory;
            if ((achievementCategory = clazz.getAnnotation(AchievementCategory.class)) != null && (clazz.getAnnotation(AchievementLoadId.class)) != null) {
                if (achievementCategory.category() == this.category) {
                    try {
                        achievements.add((AbstractAchievement) clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        achievements.sort(Comparator.comparingInt(value -> value.getClass().getAnnotation(AchievementLoadId.class).id()));
        achievements.forEach(this::registerAchievement);
    }

    public void registerAchievement(AbstractAchievement achievement) {
        this.achievements.add(achievement);
    }

    public void unregisterAchievement(Class<AbstractAchievement> clazz) {
        this.achievements.removeIf(abstractAchievement -> (abstractAchievement.getClass() == clazz));
    }

    public Category getCategory() {
        return category;
    }

    public List<AbstractAchievement> getAchievements() {
        return achievements;
    }
}
