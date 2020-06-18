/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.impl.combat.tiered;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementLoadId;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementCategory;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.base.AbstractTieredAchievement;
import de.teamhardcore.pvp.model.achievements.events.AchievementTierReceiveEvent;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.model.duel.event.DuelWinEvent;
import de.teamhardcore.pvp.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;

@AchievementListener(type = Type.DUEL_WIN)
@AchievementCategory(category = Category.COMABT)
@AchievementLoadId(id = 1)
public class DuelWinsTieredAchievement extends AbstractTieredAchievement {

    @Override
    public Map<Long, AchievementTier> getTiers() {
        return new HashMap<Long, AchievementTier>() {{
            put(10L, new AchievementTier("Duel Wins I", "Gewinne 10 Duelle", "2.000$", user -> user.getUserMoney().addMoney(2000)));
            put(50L, new AchievementTier("Duel Wins II", "Gewinne 50 Duelle", "4.000$", user -> user.getUserMoney().addMoney(4000)));
            put(100L, new AchievementTier("Duel Wins III", "Gewinne 100 Duelle", "6.000$", user -> user.getUserMoney().addMoney(6000)));
            put(500L, new AchievementTier("Duel Wins IV", "Gewinne 500 Duelle", "12.000$", user -> user.getUserMoney().addMoney(12000)));
            put(1000L, new AchievementTier("Duel Wins V", "Gewinne 1000 Duelle", "24.000$", user -> user.getUserMoney().addMoney(24000)));
        }};
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof DuelWinEvent)) return;

        DuelWinEvent duelWinEvent = (DuelWinEvent) event;

        Player player = duelWinEvent.getWinner();
        if (player == null) return;
        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());

        if (this.hasRemainingTiers(user)) {
            long wins = (Long) (!user.getUserAchievements().containsProgress(this)
                    ? user.getUserAchievements().putProgress(this, 0L)
                    : user.getUserAchievements().getProgress(this));
            user.getUserAchievements().putProgress(this, wins + 1L);

            AchievementTier receivedTier = this.getCurrentTier(user);

            long statsWins = wins + 1;
            if (statsWins == 10 || statsWins == 50 || statsWins == 100 || statsWins == 500 || statsWins == 1000)
                Main.getInstance().getServer().getPluginManager().callEvent(new AchievementTierReceiveEvent(player, receivedTier, this));
        }
    }
}
