package de.realmeze.impl.milestone;

import de.realmeze.api.collection.collection.ICollectionMilestone;

public class DefaultMilestone implements ICollectionMilestone {
    private int amount;

    public DefaultMilestone(int amount){
        this.amount = amount;
    }

    @Override
    public int getAmount() {
        return amount;
    }
}
