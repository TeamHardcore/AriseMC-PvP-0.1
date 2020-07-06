package de.realmeze.api.collection.event;

import de.realmeze.api.collection.collection.ICollection;

import java.util.ArrayList;

public interface ICollectionEvent {
    void execute();
    void awardCollections();
    ArrayList<ICollection> getCollections();
}
