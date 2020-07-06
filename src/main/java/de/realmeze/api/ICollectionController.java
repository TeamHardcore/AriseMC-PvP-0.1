package de.realmeze.api;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.group.ICollectionGroup;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import de.realmeze.api.collection.service.ICollectionGroupLoader;
import de.realmeze.api.collection.service.ICollectionLoader;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface ICollectionController {
    void registerPlayerInCollections(Player player);
    void initCollections();
    HashMap<String, ICollectionGroup> getCollectionGroupMap();
    ArrayList<ICollectionGroup> getCollectionGroupsAsList();
    ICollectionGroup getCollectionGroup(String groupType);
    ArrayList<ICollection> getCollections();
    ArrayList<IPlayerCollectionData> getAllPlayerData(Player player);
    ArrayList<ICollectionGroupLoader> getDefaultCollectionGroupLoaders();
    ArrayList<ICollectionLoader> getDefaultCollectionLoaders();
}
