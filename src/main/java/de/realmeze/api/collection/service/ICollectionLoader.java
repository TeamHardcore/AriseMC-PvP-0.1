package de.realmeze.api.collection.service;

import de.realmeze.api.collection.collection.ICollection;

import java.util.ArrayList;

public interface ICollectionLoader {
    ArrayList<? extends ICollection> load();
}
