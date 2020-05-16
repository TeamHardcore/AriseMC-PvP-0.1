/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.kits.CustomUniqueKit;
import de.teamhardcore.pvp.model.kits.Kit;
import de.teamhardcore.pvp.model.kits.TimedKit;
import de.teamhardcore.pvp.user.UserMoney;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private final Main plugin;

    private final Map<String, Kit> kits = new HashMap<String, Kit>() {{
        put("member", new TimedKit("member", Arrays.asList(new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.GOLDEN_APPLE, 16)), 1800000L));
        put("heroEinmalig", new CustomUniqueKit("EinmaligHero", null) {
            @Override
            public void giveKit(Player player) {
                UserMoney userMoney = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney();
                userMoney.addMoney(100000);
                super.giveKit(player);
            }
        });

    }};

    public KitManager(Main plugin) {
        this.plugin = plugin;
    }

    public Kit getKit(String name) {
        if (!this.kits.containsKey(name))
            return null;
        return this.kits.get(name);
    }

    public void openKitPreview(Player player, String name) {
        Kit kit = getKit(name.toLowerCase());

        if (kit == null) return;

        //todo: cache players previously kit menu

        player.openInventory(kit.getPreview());
    }

    public Map<String, Kit> getKits() {
        return kits;
    }

}
