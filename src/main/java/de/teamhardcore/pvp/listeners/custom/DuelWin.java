/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.custom;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.AchievementGroups;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementCategory;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.model.duel.event.DuelWinEvent;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AchievementListener(type = Type.PLAYER_DEATH)
@AchievementCategory(category = Category.COMABT)
public class DuelWin implements Listener {

    private final Main plugin;

    public DuelWin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWin(DuelWinEvent event) {
        Player player = event.getWinner();
        Duel duel = event.getDuel();


        UserMoney playerMoney = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney();
        playerMoney.addMoney(duel.getConfiguration().getDeployment().getCoins() * 2);

        duel.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
        duel.sendMessage(StringDefaults.PREFIX + "§c§l" + player.getName() + " §ehat das Duell gewonnen.");
        duel.sendMessage(" ");
        duel.sendMessage(StringDefaults.PREFIX + "§6§lGewinn§8: ");
        if (duel.getConfiguration().getDeployment().getCoins() > 0)
            duel.sendMessage("  §8■ §eMünzen§8: §a§l" + Util.formatNumber((duel.getConfiguration().getDeployment().getCoins() * 2)) + "$");
        duel.sendMessage(" ");
        duel.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

        AchievementGroups.$().getGroup(Category.COMABT).getAchievements().forEach(abstractAchievement -> {
            AchievementListener listener;
            if ((listener = abstractAchievement.getClass().getAnnotation(AchievementListener.class)) != null) {
                if (listener.type() == Type.DUEL_WIN)
                    abstractAchievement.onEvent(event);
            }
        });
    }

    public Main getPlugin() {
        return plugin;
    }
}
