/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.animation;

import de.teamhardcore.pvp.model.crates.CrateOpening;

public abstract class AbstractAnimation {

    private CrateOpening opening;

    public AbstractAnimation(CrateOpening opening) {
        this.opening = opening;
    }

    public abstract void startAnimation();

    public abstract void stopAnimation();

    public CrateOpening getOpening() {
        return opening;
    }
}
