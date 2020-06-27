/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.howaner.FakeMobs.event.PlayerInteractFakeMobEvent;
import de.howaner.FakeMobs.util.FakeMob;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.fakeentity.FakeEntity;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionBase;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractFakeMob implements Listener {

    private final Main plugin;

    public PlayerInteractFakeMob(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractFakeMob(PlayerInteractFakeMobEvent event) {
        Player player = event.getPlayer();
        FakeMob fakeMob = event.getMob();

        FakeEntity entity = this.plugin.getFakeEntityManager().getEntityByFakemob(fakeMob);

        if (entity == null) return;

        if (entity.getInteractCooldowns().containsKey(player)) {
            long diff = System.currentTimeMillis() - entity.getInteractCooldowns().get(player);
            if (diff < 800L) return;
        }
        entity.getInteractCooldowns().put(player, System.currentTimeMillis());
        if (!entity.getEntityOptions().isEmpty()) {
            for (FakeEntityOptionImpl option : entity.getEntityOptions()) {
                if (option.getExecutingState() != FakeEntityOptionBase.ExecutingState.CLICK) continue;
                option.executeOnClick(player);
            }
        }
    }

}
