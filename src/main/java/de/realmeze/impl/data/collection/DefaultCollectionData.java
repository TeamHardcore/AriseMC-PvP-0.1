package de.realmeze.impl.data.collection;

import de.realmeze.api.collection.collection.ICollectionData;
import de.realmeze.api.collection.collection.ICollectionMilestone;
import de.realmeze.impl.data.player.DefaultPlayerCollectionData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultCollectionData implements ICollectionData {

    private ArrayList<ICollectionMilestone> milestones;
    private HashMap<Player, ? extends DefaultPlayerCollectionData> playerData;

    public DefaultCollectionData(ArrayList<ICollectionMilestone> milestones, HashMap<Player, DefaultPlayerCollectionData> playerData) {
        this.milestones = milestones;
        this.playerData = playerData;
    }

    @Override
    public ArrayList<ICollectionMilestone> getMilestones() {
        return this.milestones;
    }

    @Override
    public HashMap<Player, ? extends DefaultPlayerCollectionData> getStatsForPlayer() {
        return this.playerData;
    }
}
