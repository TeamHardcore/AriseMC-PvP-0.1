/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.phases;

import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.utils.StringDefaults;

public class GamePhase extends AbstractDuelPhase {


    public GamePhase(Duel duel) {
        super(duel, 20, 20);

        start(30);
        duel.sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l5 Minuten§e.");
    }

    @Override
    public void run() {
        if (getTime() == 240)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l4 Minuten§e.");

        if (getTime() == 180)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l3 Minuten§e.");

        if (getTime() == 120)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l2 Minuten§e.");

        if (getTime() == 60)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l1 Minute§e.");

        if (getTime() == 30)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l30 Sekunden§e.");

        if (getTime() == 10)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l10 Sekunden§e.");

        if (getTime() == 5)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l5 Sekunden§e.");

        if (getTime() == 4)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l4 Sekunden§e.");

        if (getTime() == 3)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l3 Sekunden§e.");

        if (getTime() == 2)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l2 Sekunden§e.");

        if (getTime() == 1)
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDas Duell endet in §6§l1 Sekunde§e.");

        if (getTime() == 0) {
            stop();
            this.getDuel().sendMessage(StringDefaults.DUEL_PREFIX + "§eDie Zeit ist abgelaufen.");
            this.getDuel().startPhase(new EndPhase(this.getDuel()));
        }
        setTime(getTime() - 1);
        System.out.println(getTime());
    }

    @Override
    public DuelPhase getType() {
        return DuelPhase.INGAME;
    }
}