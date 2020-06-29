package de.realmeze.impl.milestones;

import de.realmeze.api.collection.collection.ICollectionMilestone;

public class MiningMilestone implements ICollectionMilestone {
    private int amount;

    public MiningMilestone(int amount){
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }
}
