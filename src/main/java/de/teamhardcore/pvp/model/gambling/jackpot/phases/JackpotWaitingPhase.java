/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot.phases;

import de.teamhardcore.pvp.model.gambling.jackpot.Jackpot;
import de.teamhardcore.pvp.model.gambling.jackpot.JackpotState;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;

public class JackpotWaitingPhase extends JackpotPhaseBase {

    public JackpotWaitingPhase(Jackpot parent) {
        super(parent, 20, 20);

        start(60);
    }

    @Override
    public JackpotState getState() {
        return JackpotState.WAITING_FOR_BETS;
    }

    @Override
    public void run() {

        if (getTime() == 0) {
            stop();
            Bukkit.broadcastMessage(StringDefaults.JACKPOT_PREFIX + "§6Der Jackpot wurde geschlossen.");
            this.getParent().startPhase(new JackpotRollingPhase(this.getParent()));
            this.getParent().startDrawing();
            return;
        }

        if (getTime() == 120 || getTime() == 60 || getTime() == 30 || getTime() == 15 || getTime() == 10 || getTime() == 5 || getTime() == 3 || getTime() == 2 || getTime() == 1)
            Bukkit.broadcastMessage(
                    StringDefaults.JACKPOT_PREFIX + "§6Der Jackpot wird in §e" + formatTime(
                            getTime()) + " §6geschlossen.");

        setTime(getTime() - 1);
    }

    private String formatTime(int time) {
        String timeStr;

        if (time / 60 > 0) {
            timeStr = (getTime() / 60) + (getTime() / 60 == 1 ? " Minute" : " Minuten");
        } else {
            timeStr = getTime() + (getTime() == 1 ? " Sekunde" : " Sekunden");
        }

        return timeStr;
    }
}
