package de.realmeze.impl.collection;

import de.realmeze.impl.data.collection.FishingCollectionData;
import de.realmeze.impl.identifier.CollectionId;
import de.realmeze.impl.item.CollectionItem;
import de.realmeze.impl.type.CollectionType;
import de.realmeze.impl.view.DefaultCollectionView;

public class FishingCollection extends DefaultCollection {

    public FishingCollection(CollectionType collectionType, DefaultCollectionView collectionView, CollectionItem collectionItem, FishingCollectionData fishingCollectionData, CollectionId collectionId) {
        super(collectionType, collectionView, collectionItem, fishingCollectionData, collectionId);
    }
}
