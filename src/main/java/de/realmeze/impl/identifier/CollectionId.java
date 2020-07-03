package de.realmeze.impl.identifier;

import de.realmeze.api.IIdentifier;
import de.realmeze.impl.type.CollectionType;

public class CollectionId implements IIdentifier {

    private String collectionId;

    public CollectionId(CollectionType collectionType, String collectionName) {
        this.collectionId = collectionType.getName() + "-" +  collectionName;
    }

    @Override
    public String getIdentifier() {
        return this.collectionId;
    }
}
