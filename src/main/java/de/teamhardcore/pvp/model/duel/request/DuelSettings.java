/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.request;

public class DuelSettings {

    private long maxHealStacks;
    private boolean useGoldenApple;
    private boolean usePoison;

    public DuelSettings() {
        this.maxHealStacks = -1;
        this.useGoldenApple = true;
        this.usePoison = true;
    }

    public void setMaxHealStacks(long maxHealStacks) {
        this.maxHealStacks = maxHealStacks;
    }

    public void setUseGoldenApple(boolean useGoldenApple) {
        this.useGoldenApple = useGoldenApple;
    }

    public void setUsePoison(boolean usePoison) {
        this.usePoison = usePoison;
    }

    public long getMaxHealStacks() {
        return maxHealStacks;
    }

    public boolean canUseGoldenApple() {
        return useGoldenApple;
    }

    public boolean isUsePoison() {
        return usePoison;
    }
}
