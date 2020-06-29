package de.realmeze.impl.type;

public enum CollectionTypes {

    FISHING(new CollectionType("fishing")),
    MINING(new CollectionType("mining"));

    private CollectionType collectionType;

    CollectionTypes(CollectionType collectionType) {
        this.collectionType = collectionType;
    }
    public CollectionType getCollectionType() {
        return collectionType;
    }
}
