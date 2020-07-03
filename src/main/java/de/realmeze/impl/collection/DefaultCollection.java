package de.realmeze.impl.collection;

import de.realmeze.api.IIdentifier;
import de.realmeze.api.collection.collection.*;
import de.realmeze.impl.data.collection.DefaultCollectionData;
import de.realmeze.impl.identifier.CollectionId;
import de.realmeze.impl.item.CollectionItem;
import de.realmeze.impl.type.CollectionType;
import de.realmeze.impl.view.DefaultCollectionView;

public class DefaultCollection implements ICollection {

    private final CollectionType collectionType;
    private final DefaultCollectionView collectionView;
    private final CollectionItem collectionItem;
    private final DefaultCollectionData collectionData;
    private final IIdentifier collectionId;

    public DefaultCollection(CollectionType collectionType, DefaultCollectionView collectionView,
                             CollectionItem collectionItem, DefaultCollectionData collectionData, CollectionId collectionId) {
        this.collectionType = collectionType;
        this.collectionView = collectionView;
        this.collectionItem = collectionItem;
        this.collectionData = collectionData;
        this.collectionId = collectionId;
    }

    @Override
    public IIdentifier getIdentifier() {
        return this.collectionId;
    }

    @Override
    public DefaultCollectionView getView() {
        return this.collectionView;
    }

    @Override
    public CollectionItem getCollectable() {
        return this.collectionItem;
    }

    @Override
    public CollectionType getType() {
        return this.collectionType;
    }

    @Override
    public DefaultCollectionData getData() {
        return this.collectionData;
    }
}
