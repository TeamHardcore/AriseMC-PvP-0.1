/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.abuse;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.abuse.AbuseConfiguration;
import de.teamhardcore.pvp.model.abuse.AbuseType;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandMute implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.abuse")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length < 2 || args.length > 3) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/mute <Spieler> <Grund>");
            return true;
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("confirm")) {
                if (!Main.getInstance().getAbuseManager().getAbuseConfiguration().containsKey(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/mute <Spieler> <Grund>");
                    return true;
                }

                UUID uuid = UUIDFetcher.getUUID(args[0]);

                if (uuid == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer angegebene Spieler konnte nicht gefunden werden.");
                    return true;
                }

                AbuseConfiguration configuration = Main.getInstance().getAbuseManager().getAbuseConfiguration().get(player.getUniqueId());

                if (configuration == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEin Fehler in der Konfiguration ist aufgetreten, bitte konfiguriere deinen Abuse erneut.");
                    return true;
                }

                if (!configuration.getTarget().equals(uuid)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast für diesen Spieler keinen Abuse konfiguriert.");
                    return true;
                }

                Main.getInstance().getAbuseManager().createAbuse(configuration);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Spieler erfolgreich vom Server gemutet.");

                return true;
            }

            String reason = args[1];

            UUIDFetcher.getUUID(args[0], uuid -> {

                if (uuid == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                    return;
                }
                long time = -1;

                AbuseConfiguration configuration = new AbuseConfiguration();
                configuration.setEnd(time);
                configuration.setSender(player.getUniqueId());
                configuration.setTarget(uuid);
                configuration.setReason(reason);
                configuration.setType(AbuseType.MUTE);
                configuration.setLastSeenName(args[0]);

                Main.getInstance().getAbuseManager().getAbuseConfiguration().put(player.getUniqueId(), configuration);

                String targetName = UUIDFetcher.getName(uuid);

                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(StringDefaults.PREFIX + "§cDu möchtest §7" + targetName + "§c vom Chat sperren.");
                player.sendMessage(StringDefaults.PREFIX + "§cZeitraum§8: §7" + "Permanent");
                player.sendMessage(StringDefaults.PREFIX + "§cGrund§8: §7" + reason);
                new JSONMessage(StringDefaults.PREFIX + "§c§oKlicke hier, um die Sperrung zu bestätigen.").tooltip("§7" + targetName + " §cfür " + "Permanent" + " sperren.").runCommand("/mute " + targetName + " confirm").send(player);
                player.sendMessage(" ");
                player.sendMessage(" ");
            });
        }

        if (args.length == 3) {
            String reason = args[1];
            UUIDFetcher.getUUID(args[0], uuid -> {
                if (uuid == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler wurde nicht gefunden.");
                    return;
                }
                long time = TimeUtil.parseTime(args[2]);

                AbuseConfiguration configuration = new AbuseConfiguration();
                configuration.setEnd(time);
                configuration.setSender(player.getUniqueId());
                configuration.setTarget(uuid);
                configuration.setReason(reason);
                configuration.setType(AbuseType.MUTE);
                configuration.setLastSeenName(args[0]);

                Main.getInstance().getAbuseManager().getAbuseConfiguration().put(player.getUniqueId(), configuration);
                String targetName = UUIDFetcher.getName(uuid);

                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(StringDefaults.PREFIX + "§cDu möchtest §7" + targetName + "§c vom Chat sperren.");
                player.sendMessage(StringDefaults.PREFIX + "§cZeitraum§8: §7" + (time == -1 ? "Permanent" : TimeUtil.timeToString(time)));
                player.sendMessage(StringDefaults.PREFIX + "§cGrund§8: §7" + reason);
                new JSONMessage(StringDefaults.PREFIX + "§c§oKlicke hier, um die Sperrung zu bestätigen.").tooltip("§7" + targetName + " §cfür " + (time == -1 ? "Permanent" : TimeUtil.timeToString(time)) + " sperren.").runCommand("/mute " + targetName + " confirm").send(player);
                player.sendMessage(" ");
                player.sendMessage(" ");
            });
        }

        return true;
    }
}
