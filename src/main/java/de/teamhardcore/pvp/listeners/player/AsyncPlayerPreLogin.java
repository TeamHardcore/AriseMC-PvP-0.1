/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.punishment.PunishmentInformation;
import de.teamhardcore.pvp.model.punishment.PunishmentType;
import de.teamhardcore.pvp.utils.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLogin implements Listener {

    private final Main plugin;

    public AsyncPlayerPreLogin(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        if (this.plugin.getPunishmentManager().getPunishment(event.getUniqueId(), PunishmentType.BAN) != null) {

            PunishmentInformation data = this.plugin.getPunishmentManager().getPunishment(event.getUniqueId(), PunishmentType.BAN);

            boolean tmp = !data.isPermanent();

            if (!tmp) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§4§lDu wurdest gebannt. \n§cGrund§8: §4" + data.getReason() + " \n §cDu wurdest von §4" + data.getBy() + " §cgebannt. \n\n§7Du kannst im Discord einen Entbannungsantrag stellen. \n §cDiscord§8: §ehttps://discord.arisemc.de/");
            } else {

                long diff = (data.getCreated() + data.getTimestamp()) - System.currentTimeMillis();

                if (diff > 0L) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                            "§4§lDu wurdest gebannt. " +
                                    "\n§cGrund§8: §4" + data.getReason() + " " +
                                    "\n§cVerbleibende Zeit§8: §4" + TimeUtil.timeToString((diff)) +
                                    "\n§cDu wurdest von §4" + data.getBy() + " §cgebannt. " +
                                    "\n" +
                                    "\n§7Bei temporären Sperren sind keine Entbannungsanträge möglich.");
                    return;
                }

                PunishmentInformation unbanData = new PunishmentInformation(event.getUniqueId(), PunishmentType.UNBAN, "console unban", "console", -1L);
                this.plugin.getPunishmentManager().removePunishment(event.getUniqueId(), PunishmentType.BAN);
                this.plugin.getPunishmentManager().addPunishment(event.getUniqueId(), unbanData);
            }
        }
    }

}
