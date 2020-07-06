/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.trade;

import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TradeDefaults {

    public static final List<Integer> OWN_SLOTS = new ArrayList<>();
    public static final List<Integer> TARGET_SLOTS = new ArrayList<>();

    public static final ItemStack CANCEL;
    public static final ItemStack CONFIRM;
    public static final ItemStack CONFIRMED;
    public static final ItemStack NOT_CONFIRM;
    public static final ItemStack NOT_CONFIRMED;
    public static final ItemStack ADD_MONEY;
    public static final ItemStack NO_MONEY_ADD;


    static {
        OWN_SLOTS.addAll(Arrays.asList(9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39));
        TARGET_SLOTS.addAll(Arrays.asList(14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44));

        CANCEL = new ItemBuilder(Material.STAINED_CLAY).setDurability(14).setDisplayName("§4§lHandel abbrechen").build();
        CONFIRM = new ItemBuilder(Material.STAINED_CLAY).setDurability(5).setDisplayName("§a§lBestätigen").build();
        CONFIRMED = new ItemBuilder(Material.STAINED_CLAY).setDurability(5).setDisplayName("§a§lVom Spieler bestätigt").build();
        NOT_CONFIRM = new ItemBuilder(Material.STAINED_CLAY).setDurability(14).setDisplayName("§4§lBestätigung zurückziehen").build();
        NOT_CONFIRMED = new ItemBuilder(Material.STAINED_CLAY).setDurability(14).setDisplayName("§4§lVom Spieler nicht bestätigt").build();
        ADD_MONEY = new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§lKein Geld hinzugefügt").setLore("§eKlicke§7, §eum Geld hinzuzufügen.").build();
        NO_MONEY_ADD = new ItemBuilder(Material.DOUBLE_PLANT).setDisplayName("§e§lKein Geld hinzuegfügt").build();
    }

}
