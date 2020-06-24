/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.duel.request;

import de.teamhardcore.pvp.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DuelRequest {

    private final List<Player> players;
    private final List<String> categories;
    private final List<String> goldenAppleOptions;
    private final List<String> debuffOptions;
    private final List<Integer> potionLimitations;

    private final List<String> armorOptions;

    private long expire, coins;
    private int currentPotionLimitation, debuffOption, goldenAppleOption, armorOption;
    private String currentCategory;

    public DuelRequest(Player player) {
        this.players = new ArrayList<>(Collections.singletonList(player));
        this.potionLimitations = Collections.unmodifiableList(Arrays.asList(-1, 1, 2, 4, 7, 10));
        this.goldenAppleOptions = Collections.unmodifiableList(Arrays.asList("An", "Aus"));
        this.debuffOptions = Collections.unmodifiableList(Arrays.asList("An", "Aus"));
        this.armorOptions = Collections.unmodifiableList(Arrays.asList("An", "Aus"));

        this.debuffOption = 0;
        this.goldenAppleOption = 0;
        this.coins = 0;
        this.armorOption = 1;

        this.categories = new ArrayList<>(Collections.singletonList("random"));
        Main.getInstance().getDuelManager().getAvailableMaps().keySet().forEach(category -> {
            if (!this.categories.contains(category))
                this.categories.add(category);
        });

        this.currentCategory = this.categories.get(0);
        this.currentPotionLimitation = this.potionLimitations.get(0);
        this.expire = System.currentTimeMillis() + 120000;
    }

    public void switchArmorOption() {
        if (this.armorOption <= 0)
            this.armorOption = 1;
        else this.armorOption--;
    }

    public void switchDebuffOption() {
        if (this.debuffOption >= this.debuffOptions.size() - 1)
            this.debuffOption = 0;
        else this.debuffOption++;
    }

    public void switchGoldenAppleOption() {
        if (this.goldenAppleOption >= this.goldenAppleOptions.size() - 1)
            this.goldenAppleOption = 0;
        else this.goldenAppleOption++;
    }

    public void switchCategory() {
        int currentIndex = this.categories.indexOf(this.currentCategory);

        for (String category : this.categories) {
            if (this.categories.indexOf(category) > currentIndex && this.categories.size() > currentIndex + 1) {
                this.currentCategory = this.categories.get(currentIndex + 1);
            } else this.currentCategory = this.categories.get(0);
        }
    }

    public void switchPotionLimitation() {
        int currentIndex = this.potionLimitations.indexOf(this.currentPotionLimitation);

        for (Integer limitation : this.potionLimitations) {
            if (this.potionLimitations.indexOf(limitation) > currentIndex && this.potionLimitations.size() > currentIndex + 1) {
                this.currentPotionLimitation = this.potionLimitations.get(currentIndex + 1);
            } else this.currentPotionLimitation = this.potionLimitations.get(0);
        }

        System.out.println(this.currentPotionLimitation);
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public long getCoins() {
        return coins;
    }

    public List<Integer> getPotionLimitations() {
        return potionLimitations;
    }

    public int getCurrentPotionLimitation() {
        return currentPotionLimitation;
    }

    public String getOption(int type) {
        switch (type) {
            case 0:
                return this.debuffOptions.get(this.debuffOption);
            case 1:
                return this.goldenAppleOptions.get(this.goldenAppleOption);
            case 2:
                return this.armorOptions.get(this.armorOption);
        }
        return "";
    }

    public int getOptionIndex(int type, String option) {
        switch (type) {
            case 0:
                return this.debuffOptions.indexOf(option);
            case 1:
                return this.goldenAppleOptions.indexOf(option);
            case 2:
                return this.armorOptions.indexOf(option);
        }
        return 0;
    }

    public int getDebuffOption() {
        return debuffOption;
    }

    public int getGoldenAppleOption() {
        return goldenAppleOption;
    }

    public List<String> getDebuffOptions() {
        return debuffOptions;
    }

    public List<String> getGoldenAppleOptions() {
        return goldenAppleOptions;
    }

    public int getArmorOption() {
        return armorOption;
    }

    public List<String> getArmorOptions() {
        return armorOptions;
    }


    public String getCurrentCategory() {
        return currentCategory;
    }

    public List<String> getCategories() {
        return categories;
    }

    public long getExpire() {
        return expire;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
