/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.phases;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.model.Warp;
import org.bukkit.entity.Player;

public class EndPhase extends AbstractDuelPhase {

    public EndPhase(Duel duel) {
        super(duel, 20, 20);
        start(3);
    }

    @Override
    public DuelPhase getType() {
        return DuelPhase.END;
    }

    @Override
    public void run() {
        if (getTime() <= 0) {
            stop();
            for (Player alive : this.getDuel().getAlive()) {
                if (alive == null || !alive.isOnline()) continue;

                Warp spawn = Main.getInstance().getWarpManager().getWarp("spawn");

                if (spawn == null) {
                    System.out.println("[Duel] Could not found spawn warp");
                    return;
                }

                alive.teleport(spawn.getLocation());
            }

            Main.getInstance().getDuelManager().stopDuel(this.getDuel());
            return;
        }
        setTime(getTime() - 1);
    }
}
