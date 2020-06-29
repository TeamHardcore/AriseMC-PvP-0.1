package de.realmeze.api.collection.collection;

import de.realmeze.api.collection.player.IPlayer;
import de.realmeze.api.collection.player.IPlayerCollectionData;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICollectionData {
    ArrayList<ICollectionMilestone> getMilestones();
    HashMap<IPlayer, IPlayerCollectionData> getStatsForPlayer();
}
