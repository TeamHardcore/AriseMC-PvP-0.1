package de.realmeze.api.collection.collection;

import de.realmeze.api.collection.player.IPlayerCollectionData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICollectionData {
    ArrayList<ICollectionMilestone> getMilestones();
    HashMap<Player, IPlayerCollectionData> getPlayerCollectionDataHashMap();
}
