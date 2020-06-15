/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.impl.combat.tiered;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementCategory;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementLoadId;
import de.teamhardcore.pvp.model.achievements.base.AbstractTieredAchievement;
import de.teamhardcore.pvp.model.achievements.events.AchievementTierReceiveEvent;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;

@AchievementListener(type = Type.PLAYER_DEATH)
@AchievementCategory(category = Category.COMABT)
@AchievementLoadId(id = 0)
public class KillsTieredAchievement extends AbstractTieredAchievement {

    @Override
    public Map<Long, AchievementTier> getTiers() {
        return new HashMap<Long, AchievementTier>() {{
            put(10L, new AchievementTier("Total Kills I", "Sammel 10 Kills", "2.000$", user -> user.getUserMoney().addMoney(2000)));
            put(50L, new AchievementTier("Total Kills II", "Sammel 50 Kills", "4.000$", user -> user.getUserMoney().addMoney(4000)));
            put(150L, new AchievementTier("Total Kills III", "Sammel 150 Kills", "6.000$", user -> user.getUserMoney().addMoney(6000)));
            put(250L, new AchievementTier("Total Kills IV", "Sammel 250 Kills", "12.000$", user -> user.getUserMoney().addMoney(12000)));
            put(500L, new AchievementTier("Total Kills V", "Sammel 500 Kills", "24.000$", user -> user.getUserMoney().addMoney(24000)));
        }};
    }

    @Override
    public void onEvent(Event event) {
        if (!(event instanceof PlayerDeathEvent)) return;

        PlayerDeathEvent playerDeathEvent = (PlayerDeathEvent) event;

        Player player = playerDeathEvent.getEntity();
        Player killer = player.getKiller();

        if (killer == null || killer == player) return;
        User user = Main.getInstance().getUserManager().getUser(killer.getUniqueId());

        if (this.hasRemainingTiers(user)) {
            long kills = (Long) (!user.getUserAchievements().containsProgress(this)
                    ? user.getUserAchievements().putProgress(this, 0L)
                    : user.getUserAchievements().getProgress(this));
            user.getUserAchievements().putProgress(this, kills + 1L);

            AchievementTier receivedTier = this.getCurrentTier(user);

            long statsKills = kills + 1;
            if (statsKills == 10 || statsKills == 50 || statsKills == 150 || statsKills == 250 || statsKills == 500)
                Main.getInstance().getServer().getPluginManager().callEvent(new AchievementTierReceiveEvent(killer, receivedTier, this));
        }
    }

}
