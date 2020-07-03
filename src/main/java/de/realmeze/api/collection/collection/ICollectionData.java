package de.realmeze.api.collection.collection;

import de.realmeze.api.collection.player.IPlayer;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICollectionData {
    ArrayList<? extends ICollectionMilestone> getMilestones();
    HashMap<Player, ? extends IPlayerCollectionData> getStatsForPlayer();
}
