package de.realmeze.impl.collection.identifier;

import de.realmeze.api.IIdentifier;

public class CollectionGroupId implements IIdentifier {

    private String collectionGroupId;

    public CollectionGroupId(String collectionGroupName) {
        this.collectionGroupId =  "group-" + collectionGroupName;
    }

    @Override
    public String get() {
        return this.collectionGroupId;
    }
}