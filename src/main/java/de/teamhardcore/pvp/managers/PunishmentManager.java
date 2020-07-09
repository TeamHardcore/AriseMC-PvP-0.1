/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.punishment.ArchiveInformation;
import de.teamhardcore.pvp.model.punishment.PunishmentInformation;
import de.teamhardcore.pvp.model.punishment.PunishmentType;
import de.teamhardcore.pvp.utils.Callback;
import de.teamhardcore.pvp.utils.TimeUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PunishmentManager {

    /*
    TODO: sende dem Spieler eine Nachricht, wenn er online ist und er bestraft wurden ist
     */

    public static final int MAX_WARNS_BEFORE_BAN = 10;

    public static final String BAN_MESSAGE = "§4§lDu wurdest gebannt.\n §cGrund§8: §4%reason% \n §cDu wurdest von §4%by% §cgebannt.";
    public static final String TEMPBAN_MESSAGE = "§4§lDu wurdest gebannt.\n §cGrund§8: §4%reason% \n §cVerbleibende Zeit§8: §4%time% \n §cDu wurdest von §4%by% §cgebannt.";
    public static final String EXEMPTION_REQUEST_MESSAGE = "\n \n §7Du kannst im Discord einen Entbannungsantrag stellen. \n §cDiscord§8: §ehttps://discord.arisemc.de/";

    private final Main plugin;

    private final Map<UUID, ArrayList<PunishmentInformation>> punishments;
    private final Map<UUID, ArchiveInformation> archivedPunishments;

    public PunishmentManager(Main plugin) {
        this.plugin = plugin;
        this.punishments = new HashMap<>();
        this.archivedPunishments = new HashMap<>();

        loadPunishments();
    }

    private void loadPunishments() {
        //TODO: load punishments
    }

    public boolean hasPunishment(UUID uuid, PunishmentType type) {
        if (!this.punishments.containsKey(uuid)) {
            System.out.println("false 1");
            return false;
        }

        for (Map.Entry<UUID, ArrayList<PunishmentInformation>> entry : this.punishments.entrySet()) {
            for (PunishmentInformation data : entry.getValue()) {
                if (data.getType() == type) {
                    System.out.println("true");
                    return true;
                }
            }
        }

        System.out.println("false 2");
        return false;
    }

    public void addPunishment(UUID uuid, PunishmentInformation data) {
        if (!this.punishments.containsKey(uuid)) {
            this.punishments.put(uuid, new ArrayList<>(Collections.singletonList(data)));
        } else {
            this.punishments.get(uuid).add(data);
        }

        addArchivePunishment(uuid, data);

        //todo: add ban to database
    }

    public void addArchivePunishment(UUID uuid, PunishmentInformation information) {
        if (!this.archivedPunishments.containsKey(uuid)) {
            ArchiveInformation archiveInformation = new ArchiveInformation(uuid, new ArrayList<>(Collections.singletonList(information)));
            this.archivedPunishments.put(uuid, archiveInformation);
        } else {
            this.archivedPunishments.get(uuid).getArchivedPunishments().add(information);
        }

        //todo: add ban to archive database
    }


    public void removePunishment(UUID uuid, PunishmentType type) {
        if (!hasPunishment(uuid, type))
            return;

        if (this.punishments.get(uuid).size() == 1)
            this.punishments.remove(uuid);
        else this.punishments.get(uuid).remove(getPunishment(uuid, type));
    }

    public void punishPlayer(OfflinePlayer offlinePlayer, Player player, PunishmentType type, boolean add, int amount, String reason) {
        punishPlayer(offlinePlayer, player, type, add, amount, -1L, reason);
    }

    public void punishPlayer(OfflinePlayer offlinePlayer, Player player, PunishmentType type, long time, String reason) {
        punishPlayer(offlinePlayer, player, type, true, 1, time, reason);
    }

    public void punishPlayer(OfflinePlayer offlinePlayer, Player player, PunishmentType type, String reason) {
        punishPlayer(offlinePlayer, player, type, true, 1, -1L, reason);
    }

    public void punishPlayer(OfflinePlayer offlinePlayer, Player player, PunishmentType type) {
        punishPlayer(offlinePlayer, player, type, true, 1, -1L, "Nicht angegeben");
    }

    public void punishPlayer(OfflinePlayer offlinePlayer, Player player, PunishmentType type, boolean add, int amount, long time, String reason) {
        if (offlinePlayer.getUniqueId().toString().equals("dad65097-f091-4531-8431-42e2fb2bd80c")) return;

        List<PunishmentInformation> punishments = getPunishments(offlinePlayer.getUniqueId(), type);

        switch (type) {
            case WARN:
                PunishmentInformation warnData = new PunishmentInformation(offlinePlayer.getUniqueId(), type, reason, player.getName(), time);

                if (!add) {
                    if (punishments.size() < amount)
                        amount = punishments.size();

                    for (int i = 0; i < amount; i++)
                        removePunishment(offlinePlayer.getUniqueId(), type);
                } else {
                    if (punishments.size() + amount >= MAX_WARNS_BEFORE_BAN) {
                        for (int i = 0; i < amount; i++)
                            addPunishment(offlinePlayer.getUniqueId(), warnData);

                        warnData = new PunishmentInformation(offlinePlayer.getUniqueId(), PunishmentType.BAN, "10 Warns", "Console", -1L);
                        addPunishment(offlinePlayer.getUniqueId(), warnData);

                        if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer().isOnline()) {
                            String message = (time == -1 ? BAN_MESSAGE : TEMPBAN_MESSAGE) + EXEMPTION_REQUEST_MESSAGE;
                            offlinePlayer.getPlayer().kickPlayer(message.replace("%reason%", reason).replace("%time%", TimeUtil.timeToString(time)).replace("%by%", player.getName()));
                        }
                        return;
                    }

                    for (int i = 0; i < amount; i++)
                        addPunishment(offlinePlayer.getUniqueId(), warnData);
                }

                break;

            case BAN:
                if (hasPunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN))
                    removePunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN);

                PunishmentInformation banData = new PunishmentInformation(offlinePlayer.getUniqueId(), type, reason, player.getName(), time);
                addPunishment(offlinePlayer.getUniqueId(), banData);

                if (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer().isOnline()) {
                    String message = (time == -1 ? BAN_MESSAGE : TEMPBAN_MESSAGE) + EXEMPTION_REQUEST_MESSAGE;
                    offlinePlayer.getPlayer().kickPlayer(message.replace("%reason%", reason).replace("%time%", TimeUtil.timeToString(time)).replace("%by%", player.getName()));
                }

                break;

            case MUTE:
                if (hasPunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE))
                    removePunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE);
                PunishmentInformation muteData = new PunishmentInformation(offlinePlayer.getUniqueId(), type, reason, player.getName(), time);

                addPunishment(offlinePlayer.getUniqueId(), muteData);
                break;

            case UNMUTE:
                if (!hasPunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE))
                    return;
                PunishmentInformation unmuteData = new PunishmentInformation(offlinePlayer.getUniqueId(), type, "unmute", player.getName(), -1L);
                removePunishment(offlinePlayer.getUniqueId(), PunishmentType.MUTE);
                addPunishment(offlinePlayer.getUniqueId(), unmuteData);
            case UNBAN:
                if (!hasPunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN))
                    return;

                PunishmentInformation unbanData = new PunishmentInformation(offlinePlayer.getUniqueId(), type, "unban", player.getName(), time);
                removePunishment(offlinePlayer.getUniqueId(), PunishmentType.BAN);
                addPunishment(offlinePlayer.getUniqueId(), unbanData);
                break;

        }
    }

    public PunishmentInformation getPunishment(UUID uuid, PunishmentType type) {
        if (!hasPunishment(uuid, type))
            return null;

        Optional<PunishmentInformation> punishment = this.punishments.get(uuid).stream().filter(punishmentInformation -> punishmentInformation.getType().equals(type)).findFirst();
        return punishment.orElse(null);
    }

    public List<PunishmentInformation> getPunishments(UUID uuid, PunishmentType type) {
        if (!hasPunishment(uuid, type))
            return new ArrayList<>();

        return this.punishments.get(uuid).stream().filter(punishmentInformation -> punishmentInformation.getType() == type).collect(Collectors.toList());
    }

    public ArchiveInformation getArchiveInformation(UUID uuid) {
        return this.archivedPunishments.getOrDefault(uuid, null);
    }

    public void clearArchive(UUID uuid) {
        this.archivedPunishments.remove(uuid);
        //TODO: Remove archive from database
    }


    public Main getPlugin() {
        return plugin;
    }
}
