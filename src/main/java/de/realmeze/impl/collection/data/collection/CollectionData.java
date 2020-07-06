package de.realmeze.impl.collection.data.collection;

import de.realmeze.api.collection.collection.ICollectionData;
import de.realmeze.api.collection.collection.ICollectionMilestone;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class CollectionData implements ICollectionData {

    private final ArrayList<ICollectionMilestone> milestones;
    private final HashMap<Player, IPlayerCollectionData> playerCollectionDataHashMap;

    public CollectionData(ArrayList<ICollectionMilestone> milestones, HashMap<Player, IPlayerCollectionData> playerCollectionDataHashMap) {
        this.milestones = milestones;
        this.playerCollectionDataHashMap = playerCollectionDataHashMap;
    }

    @Override
    public ArrayList<ICollectionMilestone> getMilestones() {
        return this.milestones;
    }

    @Override
    public HashMap<Player, IPlayerCollectionData> getPlayerCollectionDataHashMap() {
        return this.playerCollectionDataHashMap;
    }
}
