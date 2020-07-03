package de.realmeze.impl.collection;

import de.realmeze.impl.data.collection.MiningCollectionData;
import de.realmeze.impl.identifier.CollectionId;
import de.realmeze.impl.item.CollectionItem;
import de.realmeze.impl.type.CollectionType;
import de.realmeze.impl.view.DefaultCollectionView;

public class MiningCollection extends DefaultCollection {
    public MiningCollection(CollectionType collectionType, DefaultCollectionView collectionView, CollectionItem collectionItem, MiningCollectionData miningCollectionData, CollectionId collectionId) {
        super(collectionType, collectionView, collectionItem, miningCollectionData, collectionId);
    }
}
