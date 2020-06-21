/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.event;

import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.utils.CustomEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class DuelWinEvent extends CustomEvent implements Cancellable {

    private final Player winner, loser;
    private final Duel duel;
    private boolean canceled;

    public DuelWinEvent(Player winner, Player loser, Duel duel) {
        this.winner = winner;
        this.loser = loser;
        this.duel = duel;
    }

    public Player getWinner() {
        return winner;
    }

    public Player getLoser() {
        return loser;
    }

    public Duel getDuel() {
        return duel;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }
}
