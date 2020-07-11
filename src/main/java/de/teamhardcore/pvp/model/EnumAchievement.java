/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model;

import de.teamhardcore.pvp.user.User;

import java.util.function.Consumer;

public enum EnumAchievement {
    ;

    public enum Other {

    }


    private static class Achievement {

        private final String name, displayName, description, rewardString;
        private final Consumer<User> consumer;
        private final boolean secret;

        public Achievement(String name, String displayName, String description, String rewardString, Consumer<User> consumer, boolean secret) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.rewardString = rewardString;
            this.consumer = consumer;
            this.secret = secret;
        }

        public String getName() {
            return name;
        }

        public boolean isSecret() {
            return secret;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getRewardString() {
            return rewardString;
        }

        public String getDescription() {
            return description;
        }

        public Consumer<User> getConsumer() {
            return consumer;
        }
    }

}
