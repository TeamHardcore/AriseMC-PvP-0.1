package de.realmeze.impl.collection.type;

import de.realmeze.api.collection.collection.IRarity;

public class Rarity implements IRarity {

    private String name;
    private int tier;

    public Rarity(String name, int tier) {
        this.name = name;
        this.tier = tier;
    }

    @Override
    public String getRarityName() {
        return this.name;
    }

    @Override
    public int getRarityTier() {
        return this.tier;
    }
}
