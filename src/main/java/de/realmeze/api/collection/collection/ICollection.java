package de.realmeze.api.collection.collection;

import de.realmeze.api.IIdentifier;

public interface ICollection {

    IIdentifier getIdentifier();
    ICollectionView getView();
    ICollectable getCollectable();
    ICollectionType getType();
    ICollectionData getData();
    String[] drops();

}
