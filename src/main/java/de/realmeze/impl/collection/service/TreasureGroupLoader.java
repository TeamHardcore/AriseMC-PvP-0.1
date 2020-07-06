package de.realmeze.impl.collection.service;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.group.ICollectionGroup;
import de.realmeze.api.collection.service.ICollectionGroupLoader;
import de.realmeze.impl.collection.group.CollectionGroup;
import de.realmeze.impl.collection.group.view.CollectionGroupViews;
import de.realmeze.impl.collection.identifier.CollectionGroupId;
import de.realmeze.impl.collection.type.CollectionTypes;

import java.util.ArrayList;

public class TreasureGroupLoader implements ICollectionGroupLoader {

    @Override
    public ICollectionGroup load() {
        ICollectionGroup treasureCollectionGroup = new CollectionGroup(new ArrayList<ICollection>(),
                CollectionGroupViews.TREASURE.getCollectionGroupView(),
                new CollectionGroupId(CollectionTypes.TREASURE.getGroupName()));
        return treasureCollectionGroup;
    }
}

