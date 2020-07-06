package de.realmeze.api.collection.player;

import de.realmeze.api.collection.collection.ICollectionType;

public interface IPlayerCollectionData extends IPlayer {
    ICollectionType getCollectionType();
    long getCollectedAmount();
    int getCurrentMilestoneTier();
    void countUp(long amount);
    void countDown(long amount);

}
