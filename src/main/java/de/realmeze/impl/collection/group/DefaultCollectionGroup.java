package de.realmeze.impl.collection.group;

import de.realmeze.api.IIdentifier;
import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.group.ICollectionGroup;
import de.realmeze.impl.identifier.CollectionGroupId;
import de.realmeze.impl.view.group.DefaultCollectionGroupView;

import java.util.ArrayList;

public class DefaultCollectionGroup implements ICollectionGroup {

    private ArrayList<ICollection> collections;
    private DefaultCollectionGroupView defaultCollectionGroupView;
    private CollectionGroupId collectionGroupId;

    public DefaultCollectionGroup(ArrayList<ICollection> collections,
                                  DefaultCollectionGroupView defaultCollectionGroupView,
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
    public DefaultCollectionGroupView getView() {
        return this.defaultCollectionGroupView;
    }

    @Override
    public IIdentifier getIdentifier() {
        return this.collectionGroupId;
    }
}
