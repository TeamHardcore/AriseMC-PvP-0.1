package de.realmeze.impl.collection.type;

import de.realmeze.api.collection.collection.ICollectionType;

public enum CollectionTypes {
    FISHING("fishing"),
    MINING("mining"),
    TREASURE("treasure"),
    ;
    private String groupName;

    CollectionTypes(String groupName){
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public enum FISHINGTYPES{

        RAWFISH(new CollectionType("fishing", "rawfish", new Rarity("POOR", 0))),
        SALMON(new CollectionType("fishing", "salmon", new Rarity("POOR", 0))),
        CLOWNFISH(new CollectionType("fishing", "clownfish",new Rarity("POOR", 0))),
        PUFFERFISH(new CollectionType("fishing", "pufferfish", new Rarity("POOR", 0)));

        private ICollectionType collectionType;

        FISHINGTYPES(CollectionType collectionType) {
            this.collectionType = collectionType;
        }
        public ICollectionType getCollectionType() {
            return collectionType;
        }

    }
    public enum TREASURETYPES {

        DIVINECRATE(new CollectionType("treasure", "divine", new Rarity("RARE", 2))),;

        private ICollectionType collectionType;

        TREASURETYPES(CollectionType collectionType) {
            this.collectionType = collectionType;
        }
        public ICollectionType getCollectionType() {
            return collectionType;
        }

    }

}
