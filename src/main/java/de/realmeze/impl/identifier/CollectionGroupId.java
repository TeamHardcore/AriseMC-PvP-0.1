package de.realmeze.impl.identifier;

import de.realmeze.api.IIdentifier;
import de.realmeze.impl.type.CollectionType;

public class CollectionGroupId implements IIdentifier {

    private String collectionGroupId;

    public CollectionGroupId(String collectionGroupName) {
        this.collectionGroupId =  "group-" + collectionGroupName;
    }

    @Override
    public String getIdentifier() {
        return this.collectionGroupId;
    }
}