package de.realmeze.impl.collection.treasure.type;

import de.realmeze.api.collection.collection.treasure.ITreasureType;

public enum TreasureTypes {

    CRATE(new TreasureType("crate")),
    MONEY(new TreasureType("money"));

    private ITreasureType treasureType;

    TreasureTypes(TreasureType treasureType) {
        this.treasureType = treasureType;
    }

    public ITreasureType getTreasureType() {
        return treasureType;
    }
}
