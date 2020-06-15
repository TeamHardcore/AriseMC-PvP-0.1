/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements;

import de.teamhardcore.pvp.user.User;

import java.util.StringTokenizer;
import java.util.function.Consumer;

public class AchievementTier {

    private final String name;
    private final Consumer<User> consumer;

    private String description;
    private String reward;

    public AchievementTier(String name, Consumer<User> consumer) {
        this.name = name;
        this.consumer = consumer;
        this.description = "default description";
        this.reward = "default reward";
    }

    public AchievementTier(String name, String description, String reward, Consumer<User> consumer) {
        this(name, consumer);
        this.description = description;
        this.reward = reward;
    }

    public Consumer<User> getConsumer() {
        return consumer;
    }

    public String[] getDescription() {
        int maxLength = 40;
        int lineLen = 0;

        StringTokenizer tok = new StringTokenizer(this.description, " ");
        StringBuilder output = new StringBuilder(this.description.length());

        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            while (word.length() > maxLength) {
                output.append(word, 0, maxLength - lineLen).append("\n");
                word = word.substring(maxLength - lineLen);
                lineLen = 0;
            }

            if (lineLen + word.length() > maxLength) {
                output.append("\n");
                lineLen = 0;
            }

            output.append(word).append(" ");
            lineLen += word.length() + 1;
        }
        return output.toString().split("\n");
    }

    public String getReward() {
        return reward;
    }

    public String getName() {
        return name;
    }

    public void onAchieve(User user) {
        if (this.consumer != null && user.getPlayer() != null)
            this.consumer.accept(user);
    }

}
