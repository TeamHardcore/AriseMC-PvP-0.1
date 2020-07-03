package de.realmeze.impl.milestone;

import de.realmeze.api.collection.collection.ICollectionMilestone;

public enum Milestones {
    ;
    public enum Fishing {
        RAWFISH0(new FishingMilestone(0)),
        RAWFISH1(new FishingMilestone(10)),
        RAWFISH2(new FishingMilestone(50)),
        RAWFISH3(new FishingMilestone(100)),
        RAWFISH4(new FishingMilestone(250)),
        RAWFISH5(new FishingMilestone(500)),
        RAWFISH6(new FishingMilestone(1000)),
        RAWFISH7(new FishingMilestone(2500)),
        RAWFISH8(new FishingMilestone(5000)),
        RAWFISH9(new FishingMilestone(10000)),
        RAWFISH10(new FishingMilestone(25000));

        private FishingMilestone milestone;

        Fishing(FishingMilestone milestone) {
            this.milestone = milestone;
        }
        public FishingMilestone getMilestone() {
            return milestone;
        }
    }
    public enum Mining {
        COBBLE0(new MiningMilestone(0)),
        COBBLE1(new MiningMilestone(100)),
        COBBLE2(new MiningMilestone(1000)),
        COBBLE3(new MiningMilestone(2500)),
        COBBLE4(new MiningMilestone(5000)),
        COBBLE5(new MiningMilestone(10000)),
        COBBLE6(new MiningMilestone(50000)),
        COBBLE7(new MiningMilestone(100000)),
        COBBLE8(new MiningMilestone(250000)),
        COBBLE9(new MiningMilestone(500000)),
        COBBLE10(new MiningMilestone(1000000));

        private MiningMilestone milestone;
        Mining(MiningMilestone milestone) {
            this.milestone = milestone;
        }
        public MiningMilestone getMilestone() {
            return milestone;
        }
    }
}
