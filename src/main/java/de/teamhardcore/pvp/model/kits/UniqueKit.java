/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.kits;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UniqueKit extends Kit {

    public UniqueKit(String name, List<ItemStack> content) {
        super(name, content);
    }

    @Override
    public void giveKit(Player player) {
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();
        if (userData.getClaimedUniqueKits().contains(this.getName())) {
            player.sendMessage(StringDefaults.PREFIX + "§cDu hast die einmalige Belohnung bereits abgeholt.");
            return;
        }

        userData.addClaimedUniqueKit(this.getName());
        giveItems(player);
        player.sendMessage(StringDefaults.PREFIX + "§eDu hast die einmalige Belohnung §7" + getName() + " §eabgeholt.");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
    }
}
