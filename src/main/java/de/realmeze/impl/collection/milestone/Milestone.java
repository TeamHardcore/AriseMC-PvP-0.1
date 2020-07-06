package de.realmeze.impl.collection.milestone;

import de.realmeze.api.collection.collection.treasure.ITreasure;
import de.realmeze.api.collection.collection.ICollectionMilestone;

public class Milestone implements ICollectionMilestone {

    private long amount;
    private int tier;
    private ITreasure reward;

    public Milestone(long amount, int tier){
        this.amount = amount;
        this.tier = tier;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public int getTier() {
        return this.tier;
    }
}
