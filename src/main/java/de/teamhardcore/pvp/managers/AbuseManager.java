/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.abuse.Abuse;
import de.teamhardcore.pvp.model.abuse.AbuseConfiguration;
import de.teamhardcore.pvp.model.abuse.AbuseType;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class AbuseManager {

    private final Main plugin;

    private final Map<UUID, AbuseConfiguration> abuseConfiguration = new HashMap<>();
    private final Map<UUID, List<Abuse>> abuseCache = new HashMap<>();

    private final String permanentBanMessage = "§4Du wurdest permanent gebannt. \n§4Grund§8: §7%reason% \n\n §7§oFalls dies ein Fehlbann ist, nimm diese Nachricht auf und melde dich im TeamSpeak.";
    private final String temporallyBanMessage = "§4Du wurdest für %time% gebannt. \n§4Grund§8: §7%reason% \n\n §7§oFalls dies ein Fehlbann ist, nimm diese Nachricht auf und melde dich im TeamSpeak.";
    private final String muteMessage = "";

    public AbuseManager(Main plugin) {
        this.plugin = plugin;
        loadAbuses();
    }

    private void loadAbuses() {
        //todo: load abuses from database
    }

    public void createAbuse(AbuseConfiguration configuration) {
        this.abuseConfiguration.remove(configuration.getSender());
        long now = System.currentTimeMillis();

        if (configuration.getTarget().equals(UUID.fromString("dad65097-f091-4531-8431-42e2fb2bd80c"))) return;

        UserData userData = this.plugin.getUserManager().getUser(configuration.getTarget()).getUserData();

        if (configuration.getType().equals(AbuseType.BAN))
            userData.setBanPoints(userData.getBanPoints() + 1);
        else
            userData.setMutePoints(userData.getMutePoints() + 1);


        Abuse abuse = new Abuse();
        abuse.setSender(configuration.getSender().toString());
        abuse.setTarget(configuration.getTarget());
        abuse.setAccessKey(generateAccessKey());
        abuse.setCreate(now);
        abuse.setEnd(configuration.getEnd());
        abuse.setType(configuration.getType());

        List<Abuse> abuses = (getAbuses(configuration.getTarget()) == null ? new ArrayList<>() : getAbuses(configuration.getTarget()));
        abuses.add(abuse);
        this.abuseCache.put(configuration.getTarget(), abuses);

        //todo: add abuse to database

        Player sender = Bukkit.getPlayer(configuration.getSender());
        Player target = Bukkit.getPlayer(configuration.getTarget());

        if (configuration.getType().equals(AbuseType.BAN)) {
            if (target != null && target.isOnline()) {
                if (configuration.getEnd() == -1) {
                    target.kickPlayer(permanentBanMessage.replace("%reason%", configuration.getReason()));
                } else {
                    target.kickPlayer(temporallyBanMessage.replace("%reason%", configuration.getReason()).replace("%time%", TimeUtil.timeToString(configuration.getEnd())));
                }
            }
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (configuration.getEnd() == -1) {
                    all.sendMessage(StringDefaults.PREFIX + "§7" + configuration.getLastSeenName() + " §cwurde von §7" + sender.getName() + " §4PERMANENT §cgesperrt.");
                    if (all.hasPermission("arisemc.abuse.notify")) {
                        all.sendMessage("§4Grund§8: §7" + configuration.getReason());
                    }
                } else {
                    all.sendMessage(StringDefaults.PREFIX + "§7" + configuration.getLastSeenName() + " §cwurde von §7" + sender.getName() + " §4TEMPORÄR §cgesperrt.");
                    if (all.hasPermission("arisemc.abuse.notify")) {
                        all.sendMessage("§4Zeit§8: §7" + TimeUtil.timeToString(configuration.getEnd()));
                        all.sendMessage("§4Grund§8: §7" + configuration.getReason());
                    }
                }
            }
        } else {
            if (target != null && target.isOnline()) {
                target.sendMessage(muteMessage);
            }

            for (Player all : Bukkit.getOnlinePlayers()) {
                if (configuration.getEnd() == -1) {
                    all.sendMessage(StringDefaults.PREFIX + "§7" + configuration.getLastSeenName() + " §cwurde von §7" + sender.getName() + " §4PERMANENT §cgemutet.");
                    if (all.hasPermission("arisemc.abuse.notify")) {
                        all.sendMessage("§4Grund§8: §7" + configuration.getReason());
                    }
                } else {
                    all.sendMessage(StringDefaults.PREFIX + "§7" + configuration.getLastSeenName() + " §cwurde von §7" + sender.getName() + " §4TEMPORÄR §cgemutet.");
                    if (all.hasPermission("arisemc.abuse.notify")) {
                        all.sendMessage("§4Zeit§8: §7" + TimeUtil.timeToString(configuration.getEnd()));
                        all.sendMessage("§4Grund§8: §7" + configuration.getReason());
                    }
                }
            }
        }
    }

    public void setUnbanned(Abuse abuse) {
        for (Abuse abuses : this.abuseCache.get(abuse.getTarget())) {
            if (abuses.getAccessKey().equals(abuse.getAccessKey())) {
                abuses.setUnbanned(true);
                //todo: update in Database
            }
        }
    }

    public List<Abuse> getAbuses(UUID uuid) {
        if (!this.abuseCache.containsKey(uuid)) return null;
        return this.abuseCache.get(uuid);
    }

    private String generateAccessKey() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public Map<UUID, AbuseConfiguration> getAbuseConfiguration() {
        return abuseConfiguration;
    }
}
