/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.configuration;

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

    public void updateMaxHealStacks() {
        if (getMaxHealStacks() == -1)
            setMaxHealStacks(1);
        else if (getMaxHealStacks() == 1)
            setMaxHealStacks(2);
        else if (getMaxHealStacks() == 2)
            setMaxHealStacks(4);
        else if (getMaxHealStacks() == 4)
            setMaxHealStacks(6);
        else if (getMaxHealStacks() == 6)
            setMaxHealStacks(10);
        else if (getMaxHealStacks() == 10)
            setMaxHealStacks(-1);
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

    public boolean isUseGoldenApple() {
        return useGoldenApple;
    }

    public boolean isUsePoison() {
        return usePoison;
    }

}
