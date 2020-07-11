/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot.phases;

import de.teamhardcore.pvp.model.gambling.jackpot.Jackpot;
import de.teamhardcore.pvp.model.gambling.jackpot.JackpotState;

public class JackpotRollingPhase extends JackpotPhaseBase {

    public JackpotRollingPhase(Jackpot parent) {
        super(parent, 20, 20);
        start(100);
    }

    @Override
    public JackpotState getState() {
        return JackpotState.ROLLING;
    }

    @Override
    public void run() {
        if (getTime() == 0)
            stop();

        setTime(getTime() - 1);
    }
}
