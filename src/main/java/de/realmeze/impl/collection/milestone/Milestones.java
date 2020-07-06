package de.realmeze.impl.collection.milestone;

import de.realmeze.api.collection.collection.ICollectionMilestone;

public enum Milestones {
    ;
    public enum Fishing {
        FISH0(new Milestone(0, 0)),
        FISH1(new Milestone(10, 1)),
        FISH2(new Milestone(50, 2)),
        FISH3(new Milestone(100, 3)),
        FISH4(new Milestone(250, 4)),
        FISH5(new Milestone(500, 5)),
        FISH6(new Milestone(1000, 6)),
        FISH7(new Milestone(2500, 7)),
        FISH8(new Milestone(5000, 8)),
        FISH9(new Milestone(10000, 9)),
        FISH10(new Milestone(25000, 10));

        private ICollectionMilestone milestone;

        Fishing(ICollectionMilestone milestone) {
            this.milestone = milestone;
        }
        public ICollectionMilestone getMilestone() {
            return milestone;
        }
    }
    public enum Mining {
        COBBLE0(new Milestone(0, 0)),
        COBBLE1(new Milestone(100, 1)),
        COBBLE2(new Milestone(1000, 2)),
        COBBLE3(new Milestone(2500, 3)),
        COBBLE4(new Milestone(5000, 4)),
        COBBLE5(new Milestone(10000, 5)),
        COBBLE6(new Milestone(50000, 6)),
        COBBLE7(new Milestone(100000, 7)),
        COBBLE8(new Milestone(250000, 8)),
        COBBLE9(new Milestone(500000, 9)),
        COBBLE10(new Milestone(1000000, 10));

        private ICollectionMilestone milestone;
        Mining(ICollectionMilestone milestone) {
            this.milestone = milestone;
        }
        public ICollectionMilestone getMilestone() {
            return milestone;
        }
    }
}
