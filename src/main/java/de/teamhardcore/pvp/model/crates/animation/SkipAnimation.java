/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates.animation;

import de.teamhardcore.pvp.model.crates.CrateItem;
import de.teamhardcore.pvp.model.crates.CrateOpening;

import java.util.Random;

public class SkipAnimation extends AbstractAnimation {

    public SkipAnimation(CrateOpening opening) {
        super(opening);
    }

    @Override
    public void startAnimation() {
        CrateItem item = getOpening().getCrateItems().get(new Random().nextInt(getOpening().getCrateItems().size()));
        getOpening().giveReward(item);
    }

    @Override
    public void stopAnimation() {

    }
}
