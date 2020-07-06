package de.realmeze.impl.collection.treasure.type;

import de.realmeze.api.collection.collection.treasure.ITreasureType;

public class TreasureType implements ITreasureType {

    private String type;

    public TreasureType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
