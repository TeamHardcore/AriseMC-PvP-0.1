/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.achievements.base;

import de.teamhardcore.pvp.user.User;

import java.util.StringTokenizer;
import java.util.function.Consumer;

public abstract class AbstractChallengeAchievement extends AbstractAchievement {

    public AbstractChallengeAchievement() {
    }

    public String[] getDescription() {
        int maxLength = 40;
        StringTokenizer tok = new StringTokenizer(getRawDescription(), " ");
        StringBuilder output = new StringBuilder(getRawDescription().length());
        int lineLen = 0;

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

    public abstract String getName();

    public abstract String getRawDescription();

    public abstract Consumer<User> getRewardConsumer();

    public abstract String getRewardString();
}

