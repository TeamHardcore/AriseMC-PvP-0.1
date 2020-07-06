package de.realmeze.impl.collection.identifier;

import de.realmeze.api.IIdentifier;
import de.realmeze.api.collection.collection.ICollectionType;

public class CollectionId implements IIdentifier {

    private String collectionId;

    public CollectionId(ICollectionType collectionType, String collectionName) {
        this.collectionId = collectionType.getName() + "-" +  collectionName.length();
    }

    @Override
    public String get() {
        return this.collectionId;
    }
}
