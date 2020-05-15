/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.abuse.Abuse;
import de.teamhardcore.pvp.model.abuse.AbuseType;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.util.List;

public class AsyncPlayerPreLogin implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        List<Abuse> abuses = Main.getInstance().getAbuseManager().getAbuses(event.getUniqueId());

        if (abuses != null) {
            for (Abuse abuse : abuses) {
                if (!abuse.getType().equals(AbuseType.BAN)) continue;
                if (abuse.isUnbanned()) continue;

                long diff = (abuse.getCreate() + abuse.getEnd()) - System.currentTimeMillis();

                if (diff <= 0L && abuse.getEnd() != -1) {
                    Main.getInstance().getAbuseManager().setUnbanned(abuse);
                    return;
                }

                boolean temporallyBan = abuse.getEnd() == -1;
                if (temporallyBan)
                    event.setKickMessage("§cDu bist permanent gesperrt. \n§cGrund§8: §7" + abuse.getReason());
                else
                    event.setKickMessage("§cDu bist noch für §7" + TimeUtil.timeToString(diff) + " §cgesperrt. \n§cGrund§8: §7" + abuse.getReason());
                event.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
            }
        }

    }

}
