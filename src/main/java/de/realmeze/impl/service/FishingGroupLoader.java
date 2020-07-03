package de.realmeze.impl.service;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.service.ICollectionGroupLoader;
import de.realmeze.impl.collection.FishingCollection;
import de.realmeze.impl.collection.group.FishingCollectionGroup;
import de.realmeze.impl.identifier.CollectionGroupId;
import de.realmeze.impl.view.group.CollectionGroupViews;

import java.util.ArrayList;

public class FishingGroupLoader implements ICollectionGroupLoader {

    @Override
    public FishingCollectionGroup load() {
        FishingCollectionGroup fishingCollectionGroup = new FishingCollectionGroup(new ArrayList<ICollection>(),
                CollectionGroupViews.FISHING.RAWFISH.getCollectionGroupView(),
                new CollectionGroupId("fishing"));
        return fishingCollectionGroup;
    }
}
