package de.realmeze.impl.collection.treasure;

import de.realmeze.api.collection.collection.treasure.ITreasure;
import de.realmeze.api.collection.collection.treasure.ITreasureType;
import org.bukkit.entity.Player;

public abstract class AbstractTreasure implements ITreasure {

    private ITreasureType treasureType;

    public AbstractTreasure(ITreasureType treasureType) {
        this.treasureType = treasureType;
    }

    @Override
    public ITreasureType getType() {
        return this.treasureType;
    }
}
