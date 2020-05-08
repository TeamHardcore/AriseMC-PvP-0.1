/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop.upgrades;

import de.teamhardcore.pvp.model.clan.shop.upgrades.actions.AbstractPurchaseAction;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.AbstractRequirement;
import de.teamhardcore.pvp.utils.StringDefaults;

public class ClanChestUpgrade extends AbstractUpgrade{

    public ClanChestUpgrade(AbstractPurchaseAction action, int level, AbstractRequirement... requirements) {
        super(action, level, StringDefaults.CHEST_UPGRADE, requirements);
    }
}
