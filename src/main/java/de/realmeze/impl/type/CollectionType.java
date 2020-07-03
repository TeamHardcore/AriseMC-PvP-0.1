package de.realmeze.impl.type;

import de.realmeze.api.collection.collection.ICollectionType;

public class CollectionType implements ICollectionType {

    private String name;

    public CollectionType(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
