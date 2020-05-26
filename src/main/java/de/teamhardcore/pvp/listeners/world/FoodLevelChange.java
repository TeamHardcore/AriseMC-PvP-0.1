/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.world;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.extras.EnumPerk;
import de.teamhardcore.pvp.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    private final Main plugin;

    public FoodLevelChange(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        UserData userData = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData();

        if (userData.getUnlockedPerks().contains(EnumPerk.NO_HUNGER) && userData.getActivatedPerks().contains(EnumPerk.NO_HUNGER)) {
            event.setCancelled(true);
            if (player.getFoodLevel() < 20)
                player.setFoodLevel(20);
        }

    }

}
