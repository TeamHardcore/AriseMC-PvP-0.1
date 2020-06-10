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
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.ItemBuilder;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.Array;
import java.util.*;

public class KitManager {

    private final Main plugin;

    private final Map<String, Kit> kits = new HashMap<String, Kit>() {{
        put("member", new TimedKit("member", Arrays.asList(new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.GOLDEN_APPLE, 16)), 1800000L));
        put("einmaligVip", new CustomUniqueKit("einmaligVip", Collections.singletonList(new ItemStack(Material.DIAMOND, 16))) {
            @Override
            public boolean giveKit(Player player) {
                UserMoney userMoney = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney();
                userMoney.addMoney(100000);
                return super.giveKit(player);
            }
        });

    }};

    private final Map<Player, Integer> inventoryCache = new HashMap<>();
    private final Map<Player, Integer> previewCache = new HashMap<>();

    public KitManager(Main plugin) {
        this.plugin = plugin;
        startUpdater();
    }

    private void startUpdater() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            for (Map.Entry<Player, Integer> entry : this.inventoryCache.entrySet()) {
                if (entry.getValue() == 2) {
                    updateInventory(entry.getKey(), entry.getKey().getOpenInventory().getTopInventory(), entry.getValue());
                }
            }
        }, 20L, 20L);
    }

    public Kit getKit(String name) {
        if (!this.kits.containsKey(name))
            return null;
        return this.kits.get(name);
    }

    public void openKitPreview(Player player, String name) {
        Kit kit = getKit(name.toLowerCase());

        if (kit == null) {
            player.sendMessage(StringDefaults.PREFIX + "§cDieses Kit existiert nicht.");
            return;
        }

        if (!this.inventoryCache.containsKey(player))
            System.out.println("not contains");
        else System.out.println("contains: " + this.inventoryCache.get(player));

        int current = this.inventoryCache.getOrDefault(player, 1);
        System.out.println(current);
        this.previewCache.put(player, current);
        player.openInventory(kit.getPreview());
    }

    public void openKitInventory(Player player, int state) {
        Inventory inventory;

        if (state <= 1) {
            inventory = Bukkit.createInventory(null, 9 * 3, "§c§lKits");
        } else if (state == 2) {
            inventory = Bukkit.createInventory(null, 9 * 3, "§c§lKits - Rang");
        } else {
            inventory = Bukkit.createInventory(null, 9 * 3, "§c§lKits - Einmalig");
        }

        System.out.println(state + " open");
        updateInventory(player, inventory, state);
        player.openInventory(inventory);
        this.inventoryCache.put(player, state);
    }

    private void updateInventory(Player player, Inventory inventory, int state) {
        if (!inventory.getTitle().startsWith("§c§lKits"))
            return;

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        if (state <= 1) {
            inventory.setItem(12, new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName("§a§lRang-Kits").build());
            inventory.setItem(14, new ItemBuilder(Material.DIAMOND).setDisplayName("§b§lEinmalige Belohnung").build());

        } else if (state == 2) {
            UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

            boolean hasMemberCooldown = userData.hasKitCooldown("member");
            boolean hasVipCooldown = userData.hasKitCooldown("vip");
            boolean hasProCooldown = userData.hasKitCooldown("pro");
            boolean hasEliteCooldown = userData.hasKitCooldown("elite");
            boolean hasDivineCooldown = userData.hasKitCooldown("divine");
            boolean hasImmortalCooldown = userData.hasKitCooldown("immortal");

            String memberTime = getWaitingTime(userData, "member");
            String vipTime = player.hasPermission("airsemc.kit.vip") ? getWaitingTime(userData, "vip") : "§c§lNicht freigeschaltet.";
            String proTime = player.hasPermission("airsemc.kit.pro") ? getWaitingTime(userData, "pro") : "§c§lNicht freigeschaltet.";
            String eliteTime = player.hasPermission("airsemc.kit.elite") ? getWaitingTime(userData, "elite") : "§c§lNicht freigeschaltet.";
            String divineTime = player.hasPermission("airsemc.kit.divine") ? getWaitingTime(userData, "divine") : "§c§lNicht freigeschaltet.";
            String immortalTime = player.hasPermission("airsemc.kit.immortal") ? getWaitingTime(userData, "immortal") : "§c§lNicht freigeschaltet.";

            inventory.setItem(10, new ItemBuilder((hasMemberCooldown ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§9§lMitglied").setLore("", memberTime, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(12, new ItemBuilder((hasVipCooldown ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§a§lVIP").setLore("", vipTime, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(13, new ItemBuilder((hasProCooldown ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§7§lPro").setLore("", proTime, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(14, new ItemBuilder((hasEliteCooldown ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§e§lElite").setLore("", eliteTime, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(15, new ItemBuilder((hasDivineCooldown ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§3§lDivine").setLore("", divineTime, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(16, new ItemBuilder((hasImmortalCooldown ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§b§lImmortal").setLore("", immortalTime, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
        } else {
            UserData userData = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData();

            boolean hasVipClaimed = userData.getClaimedUniqueKits().contains("einmaligVip");
            boolean hasProClaimed = userData.getClaimedUniqueKits().contains("einmaligPro");
            boolean hasEliteClaimed = userData.getClaimedUniqueKits().contains("einmaligElite");
            boolean hasDivineClaimed = userData.getClaimedUniqueKits().contains("einmaligDivine");
            boolean hasImmortalClaimed = userData.getClaimedUniqueKits().contains("einmaligImmortal");

            String claimedVip = player.hasPermission("arisemc.kit.vip") ? (hasVipClaimed ? "§c§lBereits abgeholt" : "§a§lAbholbar") : "§c§lNicht freigeschaltet";
            String claimedPro = player.hasPermission("arisemc.kit.pro") ? (hasProClaimed ? "§c§lBereits abgeholt" : "§a§lAbholbar") : "§c§lNicht freigeschaltet";
            String claimedElite = player.hasPermission("arisemc.kit.elite") ? (hasEliteClaimed ? "§c§lBereits abgeholt" : "§a§lAbholbar") : "§c§lNicht freigeschaltet";
            String claimedDivine = player.hasPermission("arisemc.kit.divine") ? (hasDivineClaimed ? "§c§lBereits abgeholt" : "§a§lAbholbar") : "§c§lNicht freigeschaltet";
            String claimedImmortal = player.hasPermission("arisemc.kit.immortal") ? (hasImmortalClaimed ? "§c§lBereits abgeholt" : "§a§lAbholbar") : "§c§lNicht freigeschaltet";

            inventory.setItem(11, new ItemBuilder((hasVipClaimed ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§a§lVIP").setLore("", claimedVip, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(12, new ItemBuilder((hasProClaimed ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§7§lPRO").setLore("", claimedPro, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(13, new ItemBuilder((hasEliteClaimed ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§e§lElite").setLore("", claimedElite, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(14, new ItemBuilder((hasDivineClaimed ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§3§lDivine").setLore("", claimedDivine, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
            inventory.setItem(15, new ItemBuilder((hasImmortalClaimed ? Material.MINECART : Material.STORAGE_MINECART)).setDisplayName("§b§lImmortal").setLore("", claimedImmortal, "", "§eLinksklicke§7, §eum das Kit zu erhalten.", "§eRechtsklicke§7, §eum das Kit zu betrachten.").build());
        }

        if (state > 1)
            inventory.setItem(18, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
    }

    private static String getWaitingTime(UserData data, String kitName) {
        long diff = data.hasKitCooldown(kitName) ? (data.getKitCooldowns().get(kitName) - System.currentTimeMillis()) : -1L;
        if (diff / 1000L > 0L)
            return "§c§lAbholbar in §7" + TimeUtil.timeToString(diff);
        return "§a§lAbholbar";
    }

    public Map<String, Kit> getKits() {
        return kits;
    }

    public Map<Player, Integer> getInventoryCache() {
        return inventoryCache;
    }

    public Map<Player, Integer> getPreviewCache() {
        return previewCache;
    }
}
