package de.realmeze.api.collection.group;

import de.realmeze.api.IIdentifier;
import de.realmeze.api.collection.collection.ICollection;

import java.util.ArrayList;

public interface ICollectionGroup {
    ArrayList<ICollection> getCollections();
    ICollectionGroupView getView();
    IIdentifier getIdentifier();
    void add(ICollection collection);
}
