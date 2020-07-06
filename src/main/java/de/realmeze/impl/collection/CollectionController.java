package de.realmeze.impl.collection;

import de.realmeze.api.ICollectionController;
import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.collection.ICollectionView;
import de.realmeze.api.collection.group.ICollectionGroup;
import de.realmeze.api.collection.group.ICollectionGroupView;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import de.realmeze.api.collection.service.ICollectionGroupLoader;
import de.realmeze.api.collection.service.ICollectionLoader;
import de.realmeze.impl.collection.data.player.PlayerCollectionData;
import de.realmeze.impl.collection.service.FishingGroupLoader;
import de.realmeze.impl.collection.service.FishingLoader;
import de.realmeze.impl.collection.service.TreasureGroupLoader;
import de.realmeze.impl.collection.service.TreasureLoader;
import de.realmeze.impl.collection.type.CollectionTypes;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CollectionController implements ICollectionController {

    private HashMap<String, ICollectionGroup> collectionGroupMap;

    public CollectionController() {
    }

    @Override
    public void registerPlayerInCollections(Player player) {
        for (ICollectionGroup collectionGroup : getCollectionGroupsAsList()) {
            for (ICollection collection : collectionGroup.getCollections()) {
                collection.getData().getPlayerCollectionDataHashMap().put(player, new PlayerCollectionData(player, collection.getType()));
            }
        }
    }

    public void showCollectionsInChat(Player player) {
        for (ICollectionGroup collectionGroup : getCollectionGroupsAsList()) {
            ICollectionGroupView collectionGroupView = collectionGroup.getView();
            player.sendMessage(collectionGroupView.getDisplayName());
            player.sendMessage(collectionGroupView.getDescription());
            for (ICollection collection : collectionGroup.getCollections()) {
                ICollectionView collectionView = collection.getView();
                player.sendMessage(collectionView.getDisplayName());
                player.sendMessage(collectionView.getDescription());
            }
        }
    }

    public ICollectionGroup getCollectionGroup(String groupType){
        return getCollectionGroupMap().get("group-" + groupType);
    }

    @Override
    public void initCollections() {
       this.collectionGroupMap = new HashMap<String, ICollectionGroup>();
       for (ICollectionGroupLoader collectionGroupLoader : getDefaultCollectionGroupLoaders()){
           ICollectionGroup collectionGroup = collectionGroupLoader.load();
           this.collectionGroupMap.put(collectionGroup.getIdentifier().get(), collectionGroup);
       }
       ArrayList<ICollection> collectionsToLoad = new ArrayList<>();
       for (ICollectionLoader collectionLoader : getDefaultCollectionLoaders()){
           collectionsToLoad.addAll(collectionLoader.load());
       }

       for(ICollection collection : collectionsToLoad){
           String groupType = collection.getType().getGroupType();
           getCollectionGroup(groupType).add(collection);
       }
    }

    @Override
    public HashMap<String, ICollectionGroup> getCollectionGroupMap() {
        return this.collectionGroupMap;
    }

    @Override
    public ArrayList<ICollectionGroup> getCollectionGroupsAsList() {
        return new ArrayList<ICollectionGroup>(getCollectionGroupMap().values());
    }

    @Override
    public ArrayList<ICollection> getCollections() {
        ArrayList<ICollection> collections = new ArrayList<>();
        for (ICollectionGroup collectionGroup : getCollectionGroupsAsList()) {
            collections.addAll(collectionGroup.getCollections());
        }
        return collections;
    }

    @Override
    public ArrayList<IPlayerCollectionData> getAllPlayerData(Player player) {
        ArrayList<IPlayerCollectionData> data = new ArrayList<>();
        for (ICollectionGroup collectionGroup : getCollectionGroupsAsList()) {
            for (ICollection collection : collectionGroup.getCollections()) {
                data.add(collection.getData().getPlayerCollectionDataHashMap().get(player));
            }
        }
        return data;
    }


    @Override
    public ArrayList<ICollectionGroupLoader> getDefaultCollectionGroupLoaders() {
        ArrayList<ICollectionGroupLoader> collectionGroupLoaders = new ArrayList<ICollectionGroupLoader>(){{
            add(getDefaultFishingGroupLoader());
            add(getDefaultTreasureGroupLoader());
        }};
        return collectionGroupLoaders;
    }

    @Override
    public ArrayList<ICollectionLoader> getDefaultCollectionLoaders() {
        ArrayList<ICollectionLoader> collectionLoaders = new ArrayList<ICollectionLoader>(){{
            add(getDefaultFishLoader());
            add(getDefaultTreasureLoader());
        }};
        return collectionLoaders;
    }

    private FishingGroupLoader getDefaultFishingGroupLoader() {
        return new FishingGroupLoader();
    }

    private FishingLoader getDefaultFishLoader() {
        return new FishingLoader();
    }

    private TreasureLoader getDefaultTreasureLoader(){
        return new TreasureLoader();
    }
    private TreasureGroupLoader getDefaultTreasureGroupLoader(){
        return new TreasureGroupLoader();
    }
}
