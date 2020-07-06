package de.realmeze.impl.collection;

import de.realmeze.api.IIdentifier;
import de.realmeze.api.collection.collection.*;
import de.realmeze.impl.collection.identifier.CollectionId;

public class Collection implements ICollection {

    private final ICollectionType collectionType;
    private final ICollectionView collectionView;
    private final ICollectable collectionItem;
    private final ICollectionData collectionData;
    private final IIdentifier collectionId;
    private final String[] drops;

    public Collection(ICollectionType collectionType, ICollectionView collectionView,
                      ICollectable collectionItem, ICollectionData collectionData, CollectionId collectionId, String[] drops) {
        this.collectionType = collectionType;
        this.collectionView = collectionView;
        this.collectionItem = collectionItem;
        this.collectionData = collectionData;
        this.collectionId = collectionId;
        this.drops = drops;
    }

    @Override
    public IIdentifier getIdentifier() {
        return this.collectionId;
    }

    @Override
    public ICollectionView getView() {
        return this.collectionView;
    }

    @Override
    public ICollectable getCollectable() {
        return this.collectionItem;
    }

    @Override
    public ICollectionType getType() {
        return this.collectionType;
    }

    @Override
    public ICollectionData getData() {
        return this.collectionData;
    }

    @Override
    public String[] drops() {
        return this.drops;
    }
}
