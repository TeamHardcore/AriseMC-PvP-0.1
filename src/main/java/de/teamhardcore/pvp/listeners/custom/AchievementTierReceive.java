/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.custom;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.model.achievements.base.AbstractChallengeAchievement;
import de.teamhardcore.pvp.model.achievements.base.AbstractTieredAchievement;
import de.teamhardcore.pvp.model.achievements.events.AchievementTierReceiveEvent;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class AchievementTierReceive implements Listener {

    private final Main plugin;

    public AchievementTierReceive(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTierReceive(AchievementTierReceiveEvent event) {
        Player player = event.getPlayer();
        AbstractAchievement achievement = event.getAchievement();
        if (!(achievement instanceof AbstractTieredAchievement)) return;
        User user = this.plugin.getUserManager().getUser(player.getUniqueId());

        ((AbstractTieredAchievement) achievement).getCurrentTier(user).onAchieve(user);

        ArrayList<String> tooltip = new ArrayList<>();
        tooltip.add("§a" + event.getReceivedTier().getName());
        tooltip.add("");
        for (String description : event.getReceivedTier().getDescription())
            tooltip.add("§f" + description);
        tooltip.add("");
        tooltip.add("§7Belohnung§8: ");
        tooltip.add(" §e" + event.getReceivedTier().getReward());

        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lACHIEVEMENTS §8§l§m*-*-*-*-*-*-*-*-*");
        new JSONMessage(StringDefaults.PREFIX + "§eNeues Achievement freigeschaltet§8: §7" + event.getReceivedTier().getName()).tooltip(tooltip).send(player);
        player.sendMessage(" ");
        new JSONMessage(StringDefaults.PREFIX + "§7§oKlicke hier, um alle Achievements zu öffnen.").tooltip("§eAlle Achievements öffnen").runCommand("/achievements").send(player);
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lACHIEVEMENTS §8§l§m*-*-*-*-*-*-*-*-*");


    }

    public Main getPlugin() {
        return plugin;
    }
}
