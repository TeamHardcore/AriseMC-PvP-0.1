/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades;

import de.teamhardcore.pvp.model.clan.shop.upgrades.actions.AbstractPurchaseAction;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.AbstractRequirement;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractUpgrade {

    private List<AbstractRequirement> requirements;
    private AbstractPurchaseAction action;
    private int level;
    private String identifier;

    public AbstractUpgrade(AbstractPurchaseAction action, int level, String identifier, AbstractRequirement... requirements) {
        this.requirements = Arrays.asList(requirements);
        this.action = action;
        this.level = level;
        this.identifier = identifier;
    }

    public int getLevel() {
        return level;
    }

    public String getIdentifier() {
        return identifier;
    }

    public AbstractPurchaseAction getAction() {
        return action;
    }

    public boolean hasMoreRequirements() {
        return this.requirements.size() > 1;
    }

    public List<AbstractRequirement> getRequirements() {
        return requirements;
    }
}
