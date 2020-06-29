package de.realmeze.impl.milestones;

import de.realmeze.api.collection.collection.ICollectionMilestone;

public enum Milestones {
    ;
    public enum Fishing {
        RAWFISH0(new FishingMilestone(0)),
        RAWFISH1(new FishingMilestone(10)),
        RAWFISH2(new FishingMilestone(50));

        private FishingMilestone milestone;
        Fishing(FishingMilestone milestone) {
            this.milestone = milestone;
        }
        public ICollectionMilestone getMilestone() {
            return milestone;
        }
    }
    public enum Mining {
        COBBLE0(new MiningMilestone(0)),
        COBBLE1(new MiningMilestone(100)),
        COBBLE2(new MiningMilestone(1000));

        private MiningMilestone milestone;
        Mining(MiningMilestone milestone) {
            this.milestone = milestone;
        }
        public ICollectionMilestone getMilestone() {
            return milestone;
        }
    }
}
