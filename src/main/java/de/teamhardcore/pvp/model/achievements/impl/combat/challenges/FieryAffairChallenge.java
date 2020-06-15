/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.impl.combat.challenges;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.listeners.player.PlayerDeath;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementCategory;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementLoadId;
import de.teamhardcore.pvp.model.achievements.base.AbstractChallengeAchievement;
import de.teamhardcore.pvp.model.achievements.events.AchievementReceiveEvent;
import de.teamhardcore.pvp.model.achievements.events.AchievementTierReceiveEvent;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.function.Consumer;

@AchievementListener(type = Type.PLAYER_DEATH)
@AchievementCategory(category = Category.COMABT)
@AchievementLoadId(id = 2)
public class FieryAffairChallenge extends AbstractChallengeAchievement {
    @Override
    public String getName() {
        return "Feurige Angelegenheit";
    }

    @Override
    public String getRawDescription() {
        return "Töte einen Spieler mit Lava oder Feuer";
    }

    @Override
    public Consumer<User> getRewardConsumer() {
        return null;
    }

    @Override
    public String getRewardString() {
        return "20.000$";
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof PlayerDeathEvent)) return;
        PlayerDeathEvent playerDeathEvent = (PlayerDeathEvent) event;

        Player player = playerDeathEvent.getEntity();
        Player killer = player.getKiller();

        if (killer == null || killer == player) return;
        User user = Main.getInstance().getUserManager().getUser(killer.getUniqueId());

        if (!user.getUserAchievements().hasAchievement(this)) {
            if ((player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE
                    || player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                    || player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA)) {
                Main.getInstance().getServer().getPluginManager().callEvent(new AchievementReceiveEvent(killer, this));

            }
        }
    }
}
