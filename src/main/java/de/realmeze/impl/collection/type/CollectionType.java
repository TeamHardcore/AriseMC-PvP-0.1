package de.realmeze.impl.collection.type;

import de.realmeze.api.collection.collection.ICollectionType;
import de.realmeze.api.collection.collection.IRarity;

public class CollectionType implements ICollectionType {

    private String groupName;
    private String typeName;
    private IRarity rarity;

    public CollectionType(String groupName, String typeName, IRarity rarity){
        this.groupName = groupName;
        this.typeName = typeName;
        this.rarity = rarity;
    }

    @Override
    public String getGroupType() {
        return this.groupName;
    }

    @Override
    public String getName() {
        return this.typeName;
    }

    @Override
    public IRarity getRarity() {
        return this.rarity;
    }
}
