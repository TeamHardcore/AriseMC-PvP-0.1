/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.inventories;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.AchievementGroups;
import de.teamhardcore.pvp.model.achievements.AchievementTier;
import de.teamhardcore.pvp.model.achievements.base.AbstractAchievement;
import de.teamhardcore.pvp.model.achievements.base.AbstractChallengeAchievement;
import de.teamhardcore.pvp.model.achievements.base.AbstractTieredAchievement;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserAchievements;
import de.teamhardcore.pvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class AchievementInventory {

    private static HashMap<UUID, Category> categoryCache = new HashMap<>();

    public static void openMainInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lErfolge");

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        inventory.setItem(2, new ItemBuilder(Material.BOOK).setDisplayName("§c§lAllgemeine Erfolge").build());
        inventory.setItem(3, new ItemBuilder(Material.DIAMOND_BLOCK).setDisplayName("§c§lLegacy Erfolge").build());
        inventory.setItem(4, new ItemBuilder(Material.MAP).setDisplayName("§c§lEntdecker Erfolge").build());
        inventory.setItem(5, new ItemBuilder(Material.CHEST).setDisplayName("§c§lShop Erfolge").build());
        inventory.setItem(6, new ItemBuilder(Material.BARRIER).setDisplayName("§c§lComing soon").build());

        inventory.setItem(21, new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§c§lPvP Erfolge").build());
        inventory.setItem(22, new ItemBuilder(Material.DIAMOND_HELMET).setDisplayName("§c§lClan Erfolge").build());
        inventory.setItem(23, new ItemBuilder(Material.DIAMOND_PICKAXE).setDisplayName("§c§lMine Erfolge").build());

        inventory.setItem(49, new ItemBuilder(Material.SKULL_ITEM).setDurability(3).setSkullOwner(player.getName()).setDisplayName("§c§lDeine Statistiken").build());
        player.openInventory(inventory);
    }

    public static void openChallengeInventory(Player player) {
        if (!getCategoryCache().containsKey(player.getUniqueId()))
            getCategoryCache().put(player.getUniqueId(), Category.GENERAL);

        Category category = getCategoryCache().get(player.getUniqueId());

        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
        UserAchievements userAchievements = user.getUserAchievements();

        String inventoryName = "§c§l" + category.getCategoryName() + " Herausforderungserfolge";

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, inventoryName);
        inventory.setItem(45, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        inventory.setItem(49, new ItemBuilder(Material.SIGN).setDisplayName("§c§l" + category.getCategoryName() + " Herausforderungserfolge").build());

        List<AbstractAchievement> allAchievements = new ArrayList<>(AchievementGroups.$().getGroup(category).getAchievements());

        System.out.println(allAchievements);

        int row = 1;
        int column = 0;

        for (int i = 0; i < 21; ++i) {
            int slot = column + row * 10 - (row - 1);

            if (allAchievements.isEmpty() || allAchievements.size() - 1 < i) break;

            AbstractAchievement achievement = allAchievements.get(i);
            if (!(achievement instanceof AbstractChallengeAchievement)) continue;
            System.out.println(((AbstractChallengeAchievement) achievement).getName());

            boolean achieved = userAchievements.hasAchievement(achievement);

            String title = (achieved ? "§a" : "§c") + ((AbstractChallengeAchievement) achievement).getName();
            List<String> lore = new ArrayList<>();

            for (String description : ((AbstractChallengeAchievement) achievement).getDescription())
                lore.add("§f" + description);

            lore.add(" ");
            lore.add("§e§lBelohnung§8: ");
            lore.add(" §8- §7" + ((AbstractChallengeAchievement) achievement).getRewardString());
            lore.add(" ");
            lore.add((achieved ? "§aErfolg freigeschaltet" : "§cErfolg gesperrt"));
            inventory.setItem(slot, new ItemBuilder((Material.INK_SACK)).setDisplayName(title).setLore(lore).setDurability((achieved ? 10 : 8)).build());

            if (++column < 7)
                continue;
            column = 0;
            ++row;
        }

        player.openInventory(inventory);
    }

    public static void openTieredInventory(Player player) {
        if (!getCategoryCache().containsKey(player.getUniqueId()))
            getCategoryCache().put(player.getUniqueId(), Category.GENERAL);

        Category category = getCategoryCache().get(player.getUniqueId());

        User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
        UserAchievements userAchievements = user.getUserAchievements();

        String inventoryName = "§c§l" + category.getCategoryName() + " Stufenerfolge";

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, inventoryName);

        inventory.setItem(45, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        inventory.setItem(49, new ItemBuilder(Material.SIGN).setDisplayName("§c§l" + category.getCategoryName() + " Stufenerfolge").build());

        List<AbstractAchievement> allAchievements = new ArrayList<>(AchievementGroups.$().getGroup(Category.COMABT).getAchievements());

        int column = 1;
        int slot = column;

        for (int i = 0; i < 5; i++) {
            if (allAchievements.isEmpty() || allAchievements.size() - 1 < i) break;
            AbstractAchievement achievement = allAchievements.get(i);
            if (!(achievement instanceof AbstractTieredAchievement)) continue;

            for (Map.Entry<Long, AchievementTier> entry : ((AbstractTieredAchievement) achievement).getTiersSorted().descendingMap().entrySet()) {
                AchievementTier tier = entry.getValue();

                boolean hasTier = ((AbstractTieredAchievement) achievement).hasTierUnlocked(user, tier);
                String title = (hasTier ? "§a" : "§c") + tier.getName();

                List<String> lore = new ArrayList<>();
                for (String description : tier.getDescription())
                    lore.add("§f" + description);

                lore.add(" ");
                lore.add("§e§lFortschritt§8: §7" + userAchievements.getProgress(achievement) + "§8/§7" + entry.getKey());
                lore.add("§e§lBelohnung§8: ");
                lore.add(" §8- §7" + tier.getReward());
                lore.add(" ");
                lore.add((hasTier ? "§aStufe freigeschaltet" : "§cStufe gesperrt"));

                inventory.setItem(slot, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(title).setDurability((hasTier ? 3 : 7)).setLore(lore).build());
                slot += 9;
            }
            column++;
            slot = column;
        }

        player.openInventory(inventory);
    }

    public static void openChooseInventory(Player player, Category category) {
        String inventoryName = "§c§l" + category.getCategoryName() + " Erfolge";
        Inventory inventory = Bukkit.createInventory(null, 9 * 4, inventoryName);

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

        inventory.setItem(11, new ItemBuilder(Material.DIAMOND).setDisplayName("§c§lHerausforderungserfolge")
                .setLore(
                        "§e§lFreigeschaltet§8: §7%player_pvp_achievements%§8/§7%total_pvp_achievements%",
                        "",
                        "§eKlicke hier§7, §eum die Erfolge anzusehen.").build());

        inventory.setItem(15, new ItemBuilder(Material.DIAMOND_BLOCK).setDisplayName("§c§lStufenerfolge")
                .setLore(
                        "§e§lFreigeschaltet§8: §7%player_pvp_achievements%§8/§7%total_pvp_achievements%",
                        "",
                        "§eKlicke hier§7, §eum die Erfolge anzusehen.").build());

        inventory.setItem(27, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
        inventory.setItem(31, new ItemBuilder(Material.SIGN).setDisplayName("§c§l" + category.getCategoryName() + " Erfolge").build());

        player.openInventory(inventory);
        getCategoryCache().put(player.getUniqueId(), category);
    }

    public static HashMap<UUID, Category> getCategoryCache() {
        return categoryCache;
    }


        /*  public static void openPvPInventory(Player player, int type) {
          User user = Main.getInstance().getUserManager().getUser(player.getUniqueId());
          UserAchievements userAchievements = user.getUserAchievements();

          if (type == 0) {
              Inventory inventory = Bukkit.createInventory(null, 9 * 4, "§c§lPvP Erfolge");

              for (int i = 0; i < inventory.getSize(); i++)
                  inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7).build());

              inventory.setItem(11, new ItemBuilder(Material.DIAMOND).setDisplayName("§c§lHerausforderungserfolge")
                      .setLore(
                              "§e§lFreigeschaltet§8: §7%player_pvp_achievements%§8/§7%total_pvp_achievements%",
                              "",
                              "§eKlicke hier§7, §eum die Erfolge anzusehen.").build());

              inventory.setItem(15, new ItemBuilder(Material.DIAMOND_BLOCK).setDisplayName("§c§lStufenerfolge")
                      .setLore(
                              "§e§lFreigeschaltet§8: §7%player_pvp_achievements%§8/§7%total_pvp_achievements%",
                              "",
                              "§eKlicke hier§7, §eum die Erfolge anzusehen.").build());

              inventory.setItem(27, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
              inventory.setItem(31, new ItemBuilder(Material.SIGN).setDisplayName("§c§lPvP Erfolge").build());

              player.openInventory(inventory);
          }

          if (type == 1) {
              Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lPvP Herausforderungserfolge");
              inventory.setItem(45, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
              inventory.setItem(49, new ItemBuilder(Material.SIGN).setDisplayName("§c§lPvP Herausforderungserfolge").build());

              List<AbstractAchievement> allAchievements = new ArrayList<>(AchievementGroups.$().getGroup(Category.COMABT).getAchievements());

              System.out.println(allAchievements);

              int row = 1;
              int column = 0;

              for (int i = 0; i < 21; ++i) {
                  int slot = column + row * 10 - (row - 1);

                  if (allAchievements.isEmpty() || allAchievements.size() - 1 < i) break;

                  System.out.println("search achievements");

                  AbstractAchievement achievement = allAchievements.get(i);
                  if (!(achievement instanceof AbstractChallengeAchievement)) continue;

                  System.out.println("achievement found:");
                  System.out.println(((AbstractChallengeAchievement) achievement).getName());

                  boolean achieved = userAchievements.hasAchievement(achievement);

                  String title = (achieved ? "§a" : "§c") + ((AbstractChallengeAchievement) achievement).getName();
                  List<String> lore = new ArrayList<>();

                  for (String description : ((AbstractChallengeAchievement) achievement).getDescription())
                      lore.add("§f" + description);

                  lore.add(" ");
                  lore.add("§e§lBelohnung§8: ");
                  lore.add(" §8- §7" + ((AbstractChallengeAchievement) achievement).getRewardString());
                  lore.add(" ");
                  lore.add((achieved ? "§aErfolg freigeschaltet" : "§cErfolg gesperrt"));
                  inventory.setItem(slot, new ItemBuilder((Material.INK_SACK)).setDisplayName(title).setLore(lore).setDurability((achieved ? 10 : 8)).build());

                  if (++column < 7)
                      continue;
                  column = 0;
                  ++row;
              }

              player.openInventory(inventory);
          }

          if (type == 2) {
              Inventory inventory = Bukkit.createInventory(null, 9 * 6, "§c§lPvP Stufenerfolge");
              inventory.setItem(45, new ItemBuilder(Material.WOOD_DOOR).setDisplayName("§c§lZurück").build());
              inventory.setItem(49, new ItemBuilder(Material.SIGN).setDisplayName("§c§lPvP Stufenerfolge").build());

              List<AbstractAchievement> allAchievements = new ArrayList<>(AchievementGroups.$().getGroup(Category.COMABT).getAchievements());

              int column = 1;
              int slot = column;

              for (int i = 0; i < 5; i++) {
                  if (allAchievements.isEmpty() || allAchievements.size() - 1 < i) break;
                  AbstractAchievement achievement = allAchievements.get(i);
                  if (!(achievement instanceof AbstractTieredAchievement)) continue;

                  for (Map.Entry<Long, AchievementTier> entry : ((AbstractTieredAchievement) achievement).getTiersSorted().descendingMap().entrySet()) {
                      AchievementTier tier = entry.getValue();

                      boolean hasTier = ((AbstractTieredAchievement) achievement).hasTierUnlocked(user, tier);
                      String title = (hasTier ? "§a" : "§c") + tier.getName();

                      List<String> lore = new ArrayList<>();
                      for (String description : tier.getDescription())
                          lore.add("§f" + description);

                      lore.add(" ");
                      lore.add("§e§lFortschritt§8: §7" + userAchievements.getProgress(achievement) + "§8/§7" + entry.getKey());
                      lore.add("§e§lBelohnung§8: ");
                      lore.add(" §8- §7" + tier.getReward());
                      lore.add(" ");
                      lore.add((hasTier ? "§aStufe freigeschaltet" : "§cStufe gesperrt"));

                      inventory.setItem(slot, new ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(title).setDurability((hasTier ? 3 : 7)).setLore(lore).build());
                      slot += 9;
                  }
                  column++;
                  slot = column;
              }

              player.openInventory(inventory);
          }
      }*/
}
