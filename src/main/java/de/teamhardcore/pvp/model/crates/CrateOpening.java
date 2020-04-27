/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.crates;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.crates.animation.AbstractAnimation;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

public class CrateOpening {

    private final ArrayList<CrateItem> crateItems = new ArrayList<>();

    private Player player;
    private AbstractCrate crate;

    private AbstractAnimation animation;

    public CrateOpening(Player player, AbstractCrate crate) {
        this.player = player;
        this.crate = crate;

        this.animation = null;

        this.crate.getCrateItems().forEach(item -> IntStream.range(0, (int) Math.round(item.getWeight())).forEach(value -> crateItems.add(item)));
        Collections.shuffle(this.crateItems);
    }

    public void startOpening(AbstractAnimation animation) {
        this.animation = animation;

        animation.startAnimation();
    }

    public void stopOpening() {
        if (this.animation != null) this.animation.stopAnimation();
    }

    public void giveReward(CrateItem item) {
        item.getAction().doAction(getPlayer());
    }

    public ArrayList<CrateItem> getCrateItems() {
        return crateItems;
    }

    public AbstractCrate getCrate() {
        return crate;
    }

    public Player getPlayer() {
        return player;
    }

}
