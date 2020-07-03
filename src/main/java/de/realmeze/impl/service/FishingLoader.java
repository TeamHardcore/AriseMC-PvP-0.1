package de.realmeze.impl.service;

import de.realmeze.api.collection.collection.ICollectionMilestone;
import de.realmeze.api.collection.service.ICollectionLoader;
import de.realmeze.impl.collection.FishingCollection;
import de.realmeze.impl.data.collection.FishingCollectionData;
import de.realmeze.impl.data.player.FishingPlayerCollectionData;
import de.realmeze.impl.identifier.CollectionId;
import de.realmeze.impl.item.ItemBuilder;
import de.realmeze.impl.item.CollectionItem;
import de.realmeze.impl.milestone.FishingMilestone;
import de.realmeze.impl.milestone.Milestones;
import de.realmeze.impl.type.CollectionType;
import de.realmeze.impl.type.CollectionTypes;
import de.realmeze.impl.view.CollectionViews;
import de.realmeze.impl.view.DefaultCollectionView;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class FishingLoader implements ICollectionLoader {

    public FishingLoader(){
    }

    @Override
    public ArrayList<FishingCollection> load() {
        ArrayList<FishingCollection> collections = new ArrayList<FishingCollection>();
        ItemBuilder collectionItemBuilder = new ItemBuilder();
        CollectionItem rawFishCollectionItem = collectionItemBuilder.setMaterial(Material.RAW_FISH).buildCollectionItem();
        CollectionType rawFishType = CollectionTypes.FISHING.getCollectionType();
        DefaultCollectionView rawFishView = CollectionViews.Fishing.RAWFISH.getCollectionView();
        CollectionId rawFishId = new CollectionId(rawFishType, rawFishView.getDisplayName());
        ArrayList<ICollectionMilestone> fishingMilestones = new ArrayList<ICollectionMilestone>(){{
            add(Milestones.Fishing.RAWFISH0.getMilestone());
            add(Milestones.Fishing.RAWFISH1.getMilestone());
            add(Milestones.Fishing.RAWFISH2.getMilestone());
            add(Milestones.Fishing.RAWFISH3.getMilestone());
            add(Milestones.Fishing.RAWFISH4.getMilestone());
            add(Milestones.Fishing.RAWFISH5.getMilestone());
            add(Milestones.Fishing.RAWFISH6.getMilestone());
            add(Milestones.Fishing.RAWFISH7.getMilestone());
            add(Milestones.Fishing.RAWFISH8.getMilestone());
            add(Milestones.Fishing.RAWFISH9.getMilestone());
            add(Milestones.Fishing.RAWFISH10.getMilestone());
        }};

        FishingCollection rawFish = new FishingCollection(rawFishType,
                CollectionViews.Fishing.RAWFISH.getCollectionView(),
                rawFishCollectionItem,
                new FishingCollectionData(fishingMilestones, null), rawFishId);
        collections.add(rawFish);
        return collections;
    }
}
