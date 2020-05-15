/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.GlobalmuteTier;
import de.teamhardcore.pvp.model.Support;
import de.teamhardcore.pvp.model.abuse.Abuse;
import de.teamhardcore.pvp.model.abuse.AbuseType;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.ClanMember;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class AsyncPlayerChat implements Listener {

    private final Main plugin;

    public AsyncPlayerChat(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.contains("̇") || message.equalsIgnoreCase("")) {
            event.setCancelled(true);
            return;
        }

        List<Abuse> abuses = this.plugin.getAbuseManager().getAbuses(player.getUniqueId());
        if (abuses != null) {
            for (Abuse abuse : abuses) {
                if (!abuse.getType().equals(AbuseType.MUTE)) continue;
                if (abuse.isUnbanned()) continue;

                long diff = (abuse.getCreate() + abuse.getEnd()) - System.currentTimeMillis();

                if (diff > 0L || abuse.getEnd() == -1) {
                    event.setCancelled(true);
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist noch gemutet.");
                }
            }
        }

        if (this.plugin.getChatManager().getGlobalmuteTier() != GlobalmuteTier.NONE) {
            event.setCancelled(true);
            GlobalmuteTier tier = this.plugin.getChatManager().getGlobalmuteTier();

            //todo: check if player is verifiziert amk

            if (tier == GlobalmuteTier.ALL_PLAYERS) {
                if (player.hasPermission("arisemc.globalmute.bypass"))
                    event.setCancelled(false);
            }

            if (tier == GlobalmuteTier.COMPLETE) {
                if (player.hasPermission("arisemc.globalmute.team.bypass"))
                    event.setCancelled(false);
            }

            if (event.isCancelled()) {
                player.sendMessage(StringDefaults.PREFIX + "§cDer Chat ist im Moment gemutet, du kannst nicht schreiben.");
            }
        }

        if (this.plugin.getSupportManager().getSupports().containsKey(player)) {
            Support support = this.plugin.getSupportManager().getSupport(player);
            Support.SupportRole role = support.getRoles().get(player);

            event.getRecipients().clear();

            for (Player players : support.getRoles().keySet())
                event.getRecipients().add(players);

            if (role == Support.SupportRole.MEMBER) {
                event.setFormat("§8[§c§lUser§8] §e%1$s: §7%2$s");
            } else {
                event.setFormat("§8[§4§lSup§8] §c%1$s: §7%2$s");
            }
            return;
        }


        ClanMember member = this.plugin.getClanManager().getMember(player.getUniqueId());
        String prefix = "§4§lOWNER"; // get Prefix by permissions system

        if (member == null) {
            event.setFormat(" " + prefix + " §7§l• §r%1$s§8: " + getChatColor(player) + "%2$s");
        } else {
            event.setFormat(" " + prefix + " §7§l• " + member.getClan().getNameColor() + member.getClan().getName() + "§7×§r%1$s§8: " + getChatColor(player) + "%2$s");
        }
    }

    private String getChatColor(Player player) {
        if (player.hasPermission("arisemc.admin"))
            return "§c§o";
        if (player.hasPermission("arisemc.dev"))
            return "§b§o";
        if (player.hasPermission("arisemc.mod"))
            return "§5§o";
        if (player.hasPermission("arisemc.architekt"))
            return "§8§o";
        if (player.hasPermission("arisemc.sup"))
            return "§a§o";
        if (player.hasPermission("arisemc.staff"))
            return "§2§o";
        UserData userData = this.plugin.getUserManager().getUser(player.getUniqueId()).getUserData();
        return (userData.getActiveColor() == null) ? "§7" : userData.getActiveColor().getChatColor().toString();
    }


}
