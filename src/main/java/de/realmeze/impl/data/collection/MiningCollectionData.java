package de.realmeze.impl.data.collection;

import de.realmeze.api.collection.collection.ICollectionMilestone;
import de.realmeze.impl.data.player.DefaultPlayerCollectionData;
import de.realmeze.impl.milestone.DefaultMilestone;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MiningCollectionData extends DefaultCollectionData {
    public MiningCollectionData(ArrayList<ICollectionMilestone> milestones, HashMap<Player, DefaultPlayerCollectionData> playerData) {
        super(milestones, playerData);
    }
}
