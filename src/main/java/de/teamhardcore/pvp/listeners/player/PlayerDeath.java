/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.achievements.AchievementGroups;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.model.arena.Arena;
import de.teamhardcore.pvp.model.arena.ArenaOptionBase;
import de.teamhardcore.pvp.model.arena.ArenaOptionImpl;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeath implements Listener {

    private final Main plugin;

    public PlayerDeath(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        event.setDeathMessage(null);

        User user = this.plugin.getUserManager().getUser(player.getUniqueId());

        if (killer != null && killer != player && killer.isOnline()) {
            killer.setHealth(20);
            killer.setFoodLevel(30);

            player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest von §7" + killer.getName() + " §egetötet.");
            killer.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + player.getName() + " §egetötet.");

            User userKiller = this.plugin.getUserManager().getUser(killer.getUniqueId());

            if (!Util.isInventoryEmpty(player.getInventory())) {
                user.getUserStats().addDeaths(1);

                Arena arena = this.plugin.getArenaManager().getArenaByLocation(killer.getLocation());
                if (arena == null)
                    return;

                for (ArenaOptionImpl option : arena.getOptions())
                    option.executeOnKill(killer);


                if (this.plugin.getClanManager().hasClan(player.getUniqueId())) {
                    Clan clan = this.plugin.getClanManager().getClan(player.getUniqueId());
                    clan.setDeaths(clan.getDeaths() + 1);
                }

                userKiller.getUserStats().addKills(1);


                if (this.plugin.getClanManager().hasClan(killer.getUniqueId())) {
                    Clan clan = this.plugin.getClanManager().getClan(killer.getUniqueId());
                    clan.setKills(clan.getKills() + 1);

                    double coinsToAdd = (clan.getCoinBoost() == 0 ? 1 : clan.getCoinBoost() * 1);
                    clan.addMoney((long) coinsToAdd);
                    clan.setCoinBoost(0.0D);
                    clan.getMemberList().sendMessageToMembers("§6" + killer.getName() + " §7hat §e§l" + ((long) coinsToAdd) + " " + (coinsToAdd == 1 ? "Coin" : "Coins") + " §7gesammelt!");
                }

                AchievementGroups.$().getGroup(Category.COMABT).getAchievements().forEach(abstractAchievement -> {
                    AchievementListener listener;
                    if ((listener = abstractAchievement.getClass().getAnnotation(AchievementListener.class)) != null) {
                        if (listener.type() == Type.PLAYER_DEATH)
                            abstractAchievement.onEvent(event);
                    }
                });

                ArrayList<Item> drops = new ArrayList<>();
                event.getDrops().stream().filter(itemStack -> itemStack != null && itemStack.getType() != Material.AIR).forEach(itemStack -> {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

                    lore.add(this.plugin.getLootProtectionManager().getRandomKey());
                    itemMeta.setLore(lore);
                    itemStack.setItemMeta(itemMeta);

                    Item item = player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    drops.add(item);
                });
                event.getDrops().clear();
                this.plugin.getLootProtectionManager().createLootProtection(killer.getUniqueId(), drops);
            }
        } else {
            player.sendMessage(StringDefaults.PREFIX + "§eDu bist gestorben.");
            user.getUserStats().addDeaths(1);

            if (this.plugin.getClanManager().hasClan(player.getUniqueId())) {
                Clan clan = this.plugin.getClanManager().getClan(player.getUniqueId());
                clan.setDeaths(clan.getDeaths() + 1);
            }
        }

    }

}
