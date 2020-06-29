/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena.options;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.arena.Arena;
import de.teamhardcore.pvp.model.arena.ArenaOptionBase;
import de.teamhardcore.pvp.model.arena.ArenaOptionImpl;
import de.teamhardcore.pvp.model.clan.Clan;
import org.bukkit.entity.Player;

public class ArenaClanCoinBonusOption extends ArenaOptionImpl {

    private double clanCoinBonus = 0.0D;

    public ArenaClanCoinBonusOption(ArenaOptionBase optionBase, Arena arena, String... params) {
        super(optionBase, arena, params);

        try {
            this.clanCoinBonus = Double.parseDouble(params[0]);
        } catch (NumberFormatException ex) {
            System.out.println("Failure by parsing param to double");
        }
    }

    @Override
    public void executeOnKill(Player player) {
        Clan clan = Main.getInstance().getClanManager().getClan(player.getUniqueId());

        if (clan == null) return;

        clan.setCoinBoost(clan.getCoinBoost() + this.clanCoinBonus);
    }

    @Override
    public String toString() {
        return super.toString() + ("(" + this.clanCoinBonus + "%)");
    }
}
