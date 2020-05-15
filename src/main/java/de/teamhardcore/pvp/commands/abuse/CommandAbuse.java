/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.abuse;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.abuse.AbuseConfiguration;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandAbuse implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.abuse")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 0) {

        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("confirm")) {
                if (!Main.getInstance().getAbuseManager().getAbuseConfiguration().containsKey(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/abuse <Spieler> <Grund>");
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
                    System.out.println("UUID: " + uuid);
                    System.out.println("TARGET: " + configuration.getTarget());
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast für diesen Spieler keinen Abuse konfiguriert.");
                    return true;
                }

                Main.getInstance().getAbuseManager().createAbuse(configuration);
                player.sendMessage(StringDefaults.PREFIX + "§eDein Abuse-Eintrag wurde in die Datenbank übernommen.");
                return true;
            }
        }
        return true;
    }
}
