package de.realmeze.impl.collection.group;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.impl.collection.FishingCollection;
import de.realmeze.impl.identifier.CollectionGroupId;
import de.realmeze.impl.view.group.DefaultCollectionGroupView;

import java.util.ArrayList;

public class FishingCollectionGroup extends DefaultCollectionGroup {
    public FishingCollectionGroup(ArrayList<ICollection> collections, DefaultCollectionGroupView defaultCollectionGroupView, CollectionGroupId collectionGroupId) {
        super(collections, defaultCollectionGroupView, collectionGroupId);
    }

    public void addCollection(FishingCollection fishingCollection){
        getCollections().add(fishingCollection);
    }

}
