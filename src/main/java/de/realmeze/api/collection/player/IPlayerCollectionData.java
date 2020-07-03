package de.realmeze.api.collection.player;

public interface IPlayerCollectionData extends IPlayer {
    long getCollectedAmount();
    int getCurrentMilestoneTier();

}
