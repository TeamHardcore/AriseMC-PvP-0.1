package de.realmeze.impl;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.collection.ICollectionMilestone;
import de.realmeze.api.collection.group.ICollectionGroup;
import de.realmeze.impl.collection.FishingCollection;
import de.realmeze.impl.collection.group.DefaultCollectionGroup;
import de.realmeze.impl.collection.group.FishingCollectionGroup;
import de.realmeze.impl.service.FishingGroupLoader;
import de.realmeze.impl.service.FishingLoader;

import java.util.ArrayList;

public class CollectionController {

    private ArrayList<ICollectionGroup> collectionGroups;

    public CollectionController(){
    }

    public void initCollections(){
        collectionGroups = new ArrayList<>();
        FishingCollectionGroup fishingCollectionGroup = getDefaultFishingGroupLoader().load();
        ArrayList<FishingCollection> fishingCollections = getDefaultFishLoader().load();
        fishingCollections.forEach(fishingCollectionGroup::addCollection);
        this.collectionGroups.add(fishingCollectionGroup);
    }

    public String show(){
        StringBuilder sb = new StringBuilder();
        for (ICollectionGroup collectionGroup: collectionGroups) {
                for (ICollection collection: collectionGroup.getCollections()) {
                        sb.append("Collection: " + collection.getView().getDisplayName()
                                + "Description: " + collection.getView().getDescription()
                                + "item: " + collection.getView().getDisplayItem().getVanillaItemStack().getType());
                        sb.append("Milestone");
                    for (ICollectionMilestone milestone: collection.getData().getMilestones()) {
                        sb.append("Amount Needed:" + milestone.getAmount());
                    }
                }
            }

        return sb.toString();
    }

    private FishingGroupLoader getDefaultFishingGroupLoader(){
        return new FishingGroupLoader();
    }

    private FishingLoader getDefaultFishLoader(){
        return new FishingLoader();
    }
}
