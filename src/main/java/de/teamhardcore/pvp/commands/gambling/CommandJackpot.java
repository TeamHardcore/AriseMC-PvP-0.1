/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.gambling;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.jackpot.JackpotMember;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandJackpot implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (args.length == 0) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                if (Main.getInstance().getJackpotManager().getCurrentJackpot() == null) {
                    player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§cIm Moment läuft kein Jackpot.");
                    return true;
                }

                Main.getInstance().getJackpotManager().getCurrentJackpot().getJackpotInventory().openInventory(player);
                return true;
            }

            if (args[0].equalsIgnoreCase("stop")) {
                if (!player.hasPermission("arisemc.jackpot.admin")) {
                    sendHelp(player, label);
                    return true;
                }

                if (Main.getInstance().getJackpotManager().getCurrentJackpot() == null) {
                    player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§cIm Moment läuft kein Jackpot.");
                    return true;
                }

                for (JackpotMember member : Main.getInstance().getJackpotManager().getCurrentJackpot().getMemberList().getMembers().values()) {
                    OfflinePlayer opPlayer = Bukkit.getOfflinePlayer(member.getUuid());
                    if (opPlayer == null || !opPlayer.hasPlayedBefore()) continue;
                    User opUser = opPlayer.isOnline() ? Main.getInstance().getUserManager().getUser(
                            opPlayer.getUniqueId()) : new User(opPlayer.getUniqueId());
                    opUser.getUserMoney().addMoney(member.getAmount());
                    if (opPlayer.isOnline())
                        opPlayer.getPlayer().sendMessage(
                                StringDefaults.JACKPOT_PREFIX + "§cDer Jackpot wurde abgebrochen, du hast deinen Beitrag zurück erhalten.");
                }
                Main.getInstance().getJackpotManager().stopJackpot();
                player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§eDer Jackpot wurde abgebrochen.");
                return true;
            }

            sendHelp(player, label);
        }

        if (args.length == 2) {
            long amount = 0;
            try {
                amount = Long.parseLong(args[1]);
                if (amount <= 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                player.sendMessage(StringDefaults.PREFIX + "§cBitte wähle einen richtigen Betrag.");
                return true;
            }

            if (args[0].equalsIgnoreCase("start")) {
                if (!player.hasPermission("arisemc.jackpot.admin")) {
                    sendHelp(player, label);
                    return true;
                }

                if (Main.getInstance().getJackpotManager().getCurrentJackpot() != null) {
                    player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§cEs läuft bereits ein Jackpot.");
                    return true;
                }

                Main.getInstance().getJackpotManager().startJackpot(amount);
                player.sendMessage(
                        StringDefaults.JACKPOT_PREFIX + "§eDu hast einen Jackpot in Höhe von §7" + Util.formatNumber(
                                amount) + "$ §egestartet!");
                return true;
            }

            if (args[0].equalsIgnoreCase("teilnehmen")) {
                if (!player.hasPermission("arisemc.jackpot.admin")) {
                    sendHelp(player, label);
                    return true;
                }

                if (Main.getInstance().getJackpotManager().getCurrentJackpot() == null) {
                    player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§cIm Moment läuft kein Jackpot.");
                    return true;
                }

                if (Main.getInstance().getJackpotManager().getCurrentJackpot().getMemberList().getMember(
                        player.getUniqueId()) != null) {
                    player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§cDu hast bereits am Jackpot teilgenommen.");
                    return true;
                }

                UserMoney playerMoney = Main.getInstance().getUserManager().getUser(
                        player.getUniqueId()).getUserMoney();

                if (playerMoney.getMoney() < amount) {
                    player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§cDu besitzt zu wenig Münzen.");
                    return true;
                }
                playerMoney.removeMoney(amount);

                Main.getInstance().getJackpotManager().getCurrentJackpot().getMemberList().addMember(
                        player.getUniqueId(), amount);
                Main.getInstance().getJackpotManager().getCurrentJackpot().getJackpotInventory().addMemberToInventory(
                        player);
                Main.getInstance().getJackpotManager().getCurrentJackpot().getJackpotInventory().openInventory(player);

                player.sendMessage(StringDefaults.JACKPOT_PREFIX + "§eDu hast am Jackpot mit §7" + Util.formatNumber(
                        amount) + "$ §eteilgenommen.");
                return true;
            }

        }

        return true;
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " teilnehmen <Betrag>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " info");
        if (player.hasPermission("arisemc.jackpot.admin")) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " start <Max. Betrag>");
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/" + label + " stop");
        }
    }
}
