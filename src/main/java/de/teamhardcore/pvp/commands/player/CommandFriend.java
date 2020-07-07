/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.FriendInventory;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFriend implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;
        UserData userData = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();


        if (args.length != 1 && args.length != 2) {
            FriendInventory.openFriendInventory(player, 0);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                FriendInventory.openFriendInventory(player, 0);
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                UUIDFetcher.getUUID(args[0], uuid -> {
                    if (uuid == null) {
                        player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDieser Spieler war noch nie auf dem Server.");
                        return;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                    if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                        player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDieser Spieler war noch nie auf dem Server.");
                        return;
                    }

                    if (userData.getFriends().contains(offlinePlayer.getUniqueId())) {
                        UserData offlineUserData = new User(offlinePlayer.getUniqueId()).getUserData();
                        userData.removeFriend(offlinePlayer.getUniqueId());
                        offlineUserData.removeFriend(player.getUniqueId());
                        player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDu hast die Freundschaft mit §7" + offlinePlayer.getName() + " §caufgelöst.");
                        return;
                    }

                    player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDu kannst offline Spielern keine Freundschaftsanfrage senden.");
                });
                return true;
            }

            UserData targetData = Main.getInstance().getUserManager().getUser(target.getUniqueId()).getUserData();

            if (userData.getFriends().contains(target.getUniqueId())) {
                userData.removeFriend(target.getUniqueId());
                targetData.removeFriend(player.getUniqueId());
                player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDu hast die Freundschaft mit §7" + target.getName() + " §caufgelöst.");
                target.sendMessage(StringDefaults.FRIEND_PREFIX + "§7" + player.getName() + " §chat die Freundschaft mit dir aufgelöst.");
                return true;
            }

            if (userData.getFriends().size() >= 10) {
                player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDu hast bereits die maximale Anzahl an Freunden erreicht.");
                return true;
            }

            if (targetData.getFriends().size() >= 10) {
                player.sendMessage(StringDefaults.FRIEND_PREFIX + "§7" + target.getName() + " §chat bereits die maximale Anzahl an Freunden erreicht.");
                return true;
            }

            if (targetData.getFriendRequests().contains(player.getUniqueId())) {
                player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDu hast §7" + target.getName() + " §cbereits eine Freundschaftsanfrage gesendet.");
                return true;
            }

            targetData.addFriendRequest(player.getUniqueId());
            target.playSound(target.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            target.sendMessage(StringDefaults.FRIEND_PREFIX + "§eDu hast eine Freundschaftsanfrage von §7" + player.getName() + " §eerhalten.");
            new JSONMessage(StringDefaults.FRIEND_PREFIX).then("§a§lAnnehmen ").tooltip("§aFreundschaftsanfrage annehmen").runCommand("/friend accept " + player.getName()).then("§8§l/ ").then("§c§lAblehnen").tooltip("§cFreundschaftsanfrage ablehnen").runCommand("/friend deny " + player.getName()).send(target);
            player.sendMessage(StringDefaults.FRIEND_PREFIX + "§eDu hast eine Freundschaftsanfrage an §7" + player.getName() + " §egesendet.");
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            UUIDFetcher.getUUID(args[0], uuid -> {
                if (uuid == null) {
                    player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDieser Spieler war noch nie auf dem Server.");
                    return;
                }

                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

                if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
                    player.sendMessage(StringDefaults.FRIEND_PREFIX + "§cDieser Spieler war noch nie auf dem Server.");
                    return;
                }

                UserData offlineUserData = new User(offlinePlayer.getUniqueId()).getUserData();
                if (!userData.getFriendRequests().contains(uuid)) {
                    player.sendMessage(StringDefaults.FRIEND_PREFIX + "§7" + offlinePlayer.getName() + " §chat dir keine Freundschaftsanfrage gesendet.");
                    return;
                }

                boolean accept = args[0].equalsIgnoreCase("accept");

                userData.getFriendRequests().remove(uuid);

                if (accept) {
                    offlineUserData.addFriend(uuid);
                    userData.addFriend(offlinePlayer.getUniqueId());
                    player.sendMessage(StringDefaults.FRIEND_PREFIX + "§eDu hast die Freundschaftsanfrage von §7" + offlinePlayer.getName() + " §eangenommen.");
                } else {
                    player.sendMessage(StringDefaults.FRIEND_PREFIX + "§eDu hast die Freundschaftsanfrage von §7" + offlinePlayer.getName() + " §eabgelehnt.");
                }
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
            });
            return true;
        }

        UserData targetData = Main.getInstance().getUserManager().getUser(target.getUniqueId()).getUserData();

        boolean accept = args[0].equalsIgnoreCase("accept");
        userData.getFriendRequests().remove(target.getUniqueId());

        if (accept) {
            targetData.addFriend(player.getUniqueId());
            userData.addFriend(target.getUniqueId());
            player.sendMessage(StringDefaults.FRIEND_PREFIX + "§eDu hast die Freundschaftsanfrage von §7" + target.getName() + " §eangenommen.");
            target.sendMessage(StringDefaults.FRIEND_PREFIX + "§7" + player.getName() + " §ehat deine Freundschaftsanfrage angenommen.");

        } else {
            player.sendMessage(StringDefaults.FRIEND_PREFIX + "§eDu hast die Freundschaftsanfrage von §7" + target.getName() + " §eabgelehnt.");
            target.sendMessage(StringDefaults.FRIEND_PREFIX + "§7" + player.getName() + " §ehat deine Freundschaftsanfrage abgelehnt.");
        }
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
        target.playSound(target.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
        return true;


    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " list");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " <Spieler>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + "  accept <Spieler>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + "  deny <Spieler>");
    }
}
