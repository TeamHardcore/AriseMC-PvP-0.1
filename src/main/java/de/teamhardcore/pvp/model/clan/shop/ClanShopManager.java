/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.clan.shop;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.shop.upgrades.AbstractUpgrade;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.AbstractRequirement;
import de.teamhardcore.pvp.model.clan.shop.upgrades.requirements.RequirementType;
import de.teamhardcore.pvp.user.User;
import org.bukkit.entity.Player;

public class ClanShopManager {

    public void purchaseUpgrade(Clan clan, Player player, AbstractUpgrade upgrade) {
        if (!canPurchaseUpgrade(clan, player, upgrade))
            return;

        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());

        for (AbstractRequirement requirement : upgrade.getRequirements()) {
            if (requirement.getType() == RequirementType.MONEY) {
                user.removeMoney(requirement.getNeeded());
            } else if (requirement.getType() == RequirementType.COINS) {
                clan.removeMoney(requirement.getNeeded());
            }
        }

        upgrade.getAction().doAction(clan, player);
        clan.setUpgrade(upgrade.getIdentifier(), upgrade);
    }

    public boolean canPurchaseUpgrade(Clan clan, Player player, AbstractUpgrade upgrade) {
        for (AbstractRequirement requirement : upgrade.getRequirements()) {
            if (!requirement.isFulfilled(clan, player)) {
                return false;
            }
        }
        return true;
    }

}
