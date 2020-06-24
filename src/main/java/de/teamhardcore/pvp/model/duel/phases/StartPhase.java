/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.phases;

import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;

public class StartPhase extends AbstractDuelPhase {

    public StartPhase(Duel duel) {
        super(duel, 0, 20);
        start(10);
    }

    @Override
    public DuelPhase getType() {
        return DuelPhase.START;
    }

    @Override
    public void run() {
        if (getTime() <= 0) {
            stop();
            this.getDuel().startPhase(new GamePhase(this.getDuel()));
            this.getDuel().sendTitle("Los gehts!", 5, 10, 5);
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell hat begonnen.");
            return;
        }

        if (getTime() == 5)
            teleportPlayers();

        if (getTime() <= 3) {
            this.getDuel().sendTitle(String.valueOf(getTime()), 5, 10, 5);
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell beginnt in §7" + getTime() + " §e" + (getTime() == 1 ? "Sekunde" : "Sekunden"));
        }

        if (getTime() == 10 || getTime() == 8 || getTime() == 7 || getTime() == 6)
            this.getDuel().sendMessage("Du wirst in " + (getTime() - 5) + " " + (getTime() - 5 == 1 ? "Sekunde" : "Sekunden") + " teleportiert.");

        setTime(getTime() - 1);
    }

    private void teleportPlayers() {
        if (this.getDuel().getMap().getLocations().isEmpty() || this.getDuel().getMap().getLocations().size() < 2) {
            System.out.println("map doesn't contains two locations");
            return;
        }

        int place = 0;
        for (Player player : this.getDuel().getAlive()) {
            player.teleport(this.getDuel().getMap().getLocations().get(place));
            place++;
        }
    }
}
