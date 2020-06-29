package de.realmeze.impl.milestones;

import de.realmeze.api.collection.collection.ICollectionMilestone;

public class FishingMilestone implements ICollectionMilestone {

    private int amount;

    public FishingMilestone(int amount){
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }
}
