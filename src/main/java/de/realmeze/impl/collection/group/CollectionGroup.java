package de.realmeze.impl.collection.group;

import de.realmeze.api.IIdentifier;
import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.group.ICollectionGroup;
import de.realmeze.api.collection.group.ICollectionGroupView;
import de.realmeze.impl.collection.identifier.CollectionGroupId;

import java.util.ArrayList;

public class CollectionGroup implements ICollectionGroup {

    private ArrayList<ICollection> collections;
    private ICollectionGroupView defaultCollectionGroupView;
    private CollectionGroupId collectionGroupId;

    public CollectionGroup(ArrayList<ICollection> collections,
                           ICollectionGroupView defaultCollectionGroupView,
                           CollectionGroupId collectionGroupId) {
        this.collections = collections;
        this.defaultCollectionGroupView = defaultCollectionGroupView;
        this.collectionGroupId = collectionGroupId;
    }

    @Override
    public ArrayList<ICollection> getCollections() {
        return this.collections;
    }

    @Override
    public ICollectionGroupView getView() {
        return this.defaultCollectionGroupView;
    }

    @Override
    public IIdentifier getIdentifier() {
        return this.collectionGroupId;
    }

    @Override
    public void add(ICollection collection) {
        getCollections().add(collection);
    }
}
