/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot;

public class JackpotStatistics {

    private final Jackpot parent;

    private long maxAmount;
    private long collectedMoney;

    public JackpotStatistics(Jackpot parent) {
        this.parent = parent;
        this.collectedMoney = 0L;
        this.maxAmount = 0L;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public long getCollectedMoney() {
        return collectedMoney;
    }

    public void setCollectedMoney(long collectedMoney) {
        this.collectedMoney = collectedMoney;
    }

    public Jackpot getParent() {
        return parent;
    }
}
