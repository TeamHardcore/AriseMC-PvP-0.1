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
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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

            if (this.plugin.getDuelManager().getDuelCache().containsKey(player.getUniqueId()) && this.plugin.getDuelManager().getDuelCache().containsKey(killer.getUniqueId())) {
                Duel duel = this.plugin.getDuelManager().getDuelCache().get(player.getUniqueId());
                duel.performDeath(player);
                event.setKeepInventory(true);
                event.setKeepLevel(true);
                event.getDrops().clear();
                return;
            }

            player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest von §7" + killer.getName() + " §egetötet.");
            killer.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + player.getName() + " §egetötet.");

            User userKiller = this.plugin.getUserManager().getUser(killer.getUniqueId());
            user.getUserStats().addDeaths(1);

            if (this.plugin.getClanManager().hasClan(player.getUniqueId())) {
                Clan clan = this.plugin.getClanManager().getClan(player.getUniqueId());
                clan.setDeaths(clan.getDeaths() + 1);
            }

            userKiller.getUserStats().addKills(1);


            if (this.plugin.getClanManager().hasClan(killer.getUniqueId())) {
                Clan clan = this.plugin.getClanManager().getClan(killer.getUniqueId());
                clan.setDeaths(clan.getDeaths() + 1);
                clan.addMoney(1);
            }

            AchievementGroups.$().getGroup(Category.COMABT).getAchievements().forEach(abstractAchievement -> {
                AchievementListener listener;
                if ((listener = abstractAchievement.getClass().getAnnotation(AchievementListener.class)) != null) {
                    if (listener.type() == Type.PLAYER_DEATH)
                        abstractAchievement.onEvent(event);
                }
            });

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
