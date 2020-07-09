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
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.ClanMember;
import de.teamhardcore.pvp.model.punishment.PunishmentInformation;
import de.teamhardcore.pvp.model.punishment.PunishmentType;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

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

        if (message.startsWith("#") && this.plugin.getClanManager().hasClan(player.getUniqueId())) {
            message = message.substring(1);
            message = message.trim();
            event.setMessage(message);

            Clan clan = this.plugin.getClanManager().getClan(player.getUniqueId());
            ClanMember member = clan.getMemberList().getMember(player.getUniqueId());

            event.getRecipients().clear();

            for (UUID clanMemberUUID : clan.getMemberList().getMembers().keySet()) {
                Player clanMember = Bukkit.getPlayer(clanMemberUUID);
                if (clanMember == null || !clanMember.isOnline()) continue;
                event.getRecipients().add(clanMember);
            }

            event.setFormat("§8[§6§lClanChat§8] " + member.getRank().getColor() + "§l%1$s§8: §7%2$s");
            return;
        }

        if (this.plugin.getPunishmentManager().getPunishment(player.getUniqueId(), PunishmentType.MUTE) != null) {
            PunishmentInformation data = this.plugin.getPunishmentManager().getPunishment(player.getUniqueId(), PunishmentType.MUTE);
            long diff = (data.getCreated() + data.getTimestamp()) - System.currentTimeMillis();

            if (data.isPermanent()) {
                event.setCancelled(true);
                player.sendMessage(StringDefaults.PREFIX + "§cDein Chat ist permanent gesperrt.");
                player.sendMessage(StringDefaults.PREFIX + "§7Du kannst im Discord einen Entbannungsantrag stellen.");
                player.sendMessage(StringDefaults.PREFIX + "§cDiscord: §ehttps://arisemc.discord.de");
                return;
            }

            if (diff > 0L) {
                event.setCancelled(true);
                player.sendMessage(StringDefaults.PREFIX + "§cDein Chat ist noch für §7" + TimeUtil.timeToString((diff)) + " §cgesperrt.");
                return;
            }

            this.plugin.getPunishmentManager().removePunishment(player.getUniqueId(), PunishmentType.MUTE);
            PunishmentInformation unmuteData = new PunishmentInformation(player.getUniqueId(), PunishmentType.UNMUTE, "unmute", "console", -1L);
            this.plugin.getPunishmentManager().addPunishment(player.getUniqueId(), unmuteData);
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
                player.sendMessage(StringDefaults.PREFIX + "§cDer Chat ist derzeit deaktiviert.");
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

        if (this.plugin.isLuckPermsEnabled()) {
            User luckPermsUser = LuckPerms.getApi().getUser(player.getUniqueId());
            MetaData metaData = luckPermsUser != null ? luckPermsUser.getCachedData().getMetaData(Contexts.allowAll()) : null;
            if (metaData == null)
                return;

            ClanMember member = this.plugin.getClanManager().getMember(player.getUniqueId());
            String prefix = (metaData.getPrefix() == null ? "" : ChatColor.translateAlternateColorCodes('&', metaData.getPrefix()));

            if (member == null) {
                event.setFormat("§8[" + prefix + "§8] §r%1$s§8: " + getChatColor(player) + "%2$s");
            } else {
                event.setFormat("§8[" + prefix + "§8] §r" + member.getClan().getNameColor() + member.getClan().getName() + "§7" + member.getRank().getColor() + "×§r%1$s§8: " + getChatColor(player) + "%2$s");
            }
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
