/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.model.achievements.base.AbstractChallengeAchievement;
import de.teamhardcore.pvp.model.achievements.events.AchievementReceiveEvent;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class AchievementReceive implements Listener {
    private final Main plugin;

    public AchievementReceive(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReceive(AchievementReceiveEvent event) {
        Player player = event.getPlayer();
        AbstractAchievement achievement = event.getAchievement();
        if (!(achievement instanceof AbstractChallengeAchievement)) return;

        User user = this.plugin.getUserManager().getUser(player.getUniqueId());

        ((AbstractChallengeAchievement) achievement).getRewardConsumer().accept(user);

        ArrayList<String> tooltip = new ArrayList<>();
        tooltip.add("§a" + ((AbstractChallengeAchievement) achievement).getName());
        tooltip.add("");
        for (String description : ((AbstractChallengeAchievement) achievement).getDescription())
            tooltip.add("§f" + description);
        tooltip.add("");
        tooltip.add("§7Belohnung§8: ");
        tooltip.add("§e" + ((AbstractChallengeAchievement) achievement).getRewardString());

        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lACHIEVEMENTS §8§l§m*-*-*-*-*-*-*-*-*");
        new JSONMessage(StringDefaults.PREFIX + "§eNeues Achievement freigeschaltet§8: §7" + ((AbstractChallengeAchievement) achievement).getName()).tooltip(tooltip).send(player);
        player.sendMessage(" ");
        new JSONMessage(StringDefaults.PREFIX + "§7§oKlicke hier, um alle Achievements zu öffnen.").tooltip("§eAlle Achievements öffnen").runCommand("/achievements").send(player);
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lACHIEVEMENTS §8§l§m*-*-*-*-*-*-*-*-*");
    }

    public Main getPlugin() {
        return plugin;
    }
}
