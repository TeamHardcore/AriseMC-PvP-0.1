/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.pvp;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.ClanShopInventory;
import de.teamhardcore.pvp.model.clan.Clan;
import de.teamhardcore.pvp.model.clan.ClanMember;
import de.teamhardcore.pvp.model.clan.ClanRank;
import de.teamhardcore.pvp.model.teleport.TPDelay;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.UUIDFetcher;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class CommandClan implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (args.length == 0 || args.length > 3) {
            sendHelp(player, label, 1);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("1")) {
                sendHelp(player, label, 1);
            } else if (args[0].equalsIgnoreCase("2")) {
                sendHelp(player, label, 2);
            } else if (args[0].equalsIgnoreCase("3")) {
                sendHelp(player, label, 3);

            } else if (args[0].equalsIgnoreCase("chest")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                Inventory chest = clan.getClanChest().getClanChest();

                if (chest == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEin Fehler ist aufgetreten, bitte kontaktiere einen Admin.");
                    return true;
                }

                player.openInventory(chest);
                //todo: add player to clan chest cache
            } else if (args[0].equalsIgnoreCase("löschen") || args[0].equalsIgnoreCase("delete")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                Clan clan = Main.getInstance().getClanManager().getClan(player.getUniqueId());
                ClanMember member = clan.getMemberList().getMember(player.getUniqueId());

                if (member.getRank() != ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cNur der Clan-Owner kann den Clan löschen.");
                    return true;
                }


                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Clan erfolgreich gelöscht.");
                player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player) continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§cDer Clan wurde von §7" + player.getName() + " §cgelöscht.");
                    offlinePlayer.getPlayer().playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                }

                Main.getInstance().getClanManager().deleteClan(clan.getName());
            } else if (args[0].equalsIgnoreCase("shop")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }
                Clan clan = Main.getInstance().getClanManager().getClan(player.getUniqueId());

                ClanShopInventory.openClanShop(player, clan);
            } else if (args[0].equalsIgnoreCase("setbase")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() != ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst die Clanbase nicht setzen.");
                    return true;
                }

                Location pos = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5D, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 0.5D);
                clan.setBase(pos);

                player.sendMessage(StringDefaults.PREFIX + "§eDu hast die Clanbase erfolgreich gesetzt.");

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player) continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat den Clanbase gesetzt.");
                }

            } else if (args[0].equalsIgnoreCase("base")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() == ClanRank.MEMBER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst dich erst ab §e§lTRUSTED §cin die Clanbase teleportieren.");
                    return true;
                }

                if (clan.getBase() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat noch keine Clanbase gesetzt.");
                    return true;
                }

                Location location = clan.getBase();
                if (player.hasPermission("arisemc.teleport.nodelay")) {
                    Main.getInstance().getManager().getLastPositions().put(player, player.getLocation());
                    player.teleport(location);
                    player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest zur Clanbase teleportiert.");
                } else {
                    player.sendMessage(StringDefaults.PREFIX + "§eDu wirst in §72 Sekunden §eteleportiert.");

                    TPDelay tpDelay = new TPDelay(player, 0, 2) {
                        @Override
                        public boolean onTick() {
                            ClanMember clanMember = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                            if (clanMember == null || clanMember.getClan() != member.getClan()) return true;

                            return (clanMember.getClan().getBase() == null);
                        }

                        @Override
                        public void onEnd() {
                            Main.getInstance().getManager().getLastPositions().put(player, player.getLocation());
                            player.teleport(location);
                            player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest zur Clanbase teleportiert.");
                        }
                    };
                    Main.getInstance().getManager().getTeleportDelays().put(player, tpDelay);
                }
            } else if (args[0].equalsIgnoreCase("rang") || args[0].equalsIgnoreCase("rank")) {
                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
                player.sendMessage(StringDefaults.PREFIX + "§c§lRang §4§lOWNER");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan löschen");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan changeowner");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan setbase");
                player.sendMessage("");
                player.sendMessage(StringDefaults.PREFIX + "§c§lRang §5§lMOD");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan einladen");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan kick");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan setrang");
                player.sendMessage("");
                player.sendMessage(StringDefaults.PREFIX + "§c§lRang §e§lTRUSTED");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan base");
                player.sendMessage("");
                player.sendMessage(StringDefaults.PREFIX + "§c§lRang §9§lMEMBER");
                player.sendMessage(StringDefaults.PREFIX + "§7/clan shop");
                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");

            } else if (args[0].equalsIgnoreCase("stats")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }
                Clan clan = Main.getInstance().getClanManager().getClan(player.getUniqueId());
                sendStats(player, clan);
            } else if (args[0].equalsIgnoreCase("verlassen") || args[0].equalsIgnoreCase("leave")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() == ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBenutze /" + label + " löschen, um den Clan zu löschen.");
                    return true;
                }

                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Clan verlassen.");

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player) continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat den Clan verlassen.");
                }

                Main.getInstance().getClanManager().removeMember(player.getUniqueId());


            } else if (args[0].equalsIgnoreCase("warp")) {
                if (!Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (clan.getUpgradeLevel(StringDefaults.WARP_UPGRADE) == 0) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat die Clan-Warps noch nicht freigeschaltet.");
                    return true;
                }

                if (clan.getWarps().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan besitzt noch keinen Clan-Warp§.");
                    return true;
                }

                String warp = "";

                if (clan.getWarps().size() == 1) {
                    warp = clan.getWarps().keySet().stream().findFirst().orElse("");
                } else if (clan.getWarp("warp") != null) {
                    warp = "warp";
                } else {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan besitzt mehrere Clan-Warps.");
                    player.sendMessage(StringDefaults.PREFIX + "§cBenutze §7/clan warp <Name> §cum dich zum Clan-Warp zu teleportieren.");
                    return true;
                }

                Location location = clan.getWarp(warp);
                player.teleport(location);
                //todo: add tp delay

            } else if (args[0].equalsIgnoreCase("warps")) {
                if (!Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (clan.getUpgradeLevel(StringDefaults.WARP_UPGRADE) == 0) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat die Clan-Warps noch nicht freigeschaltet.");
                    return true;
                }

                if (clan.getWarps().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan besitzt noch keinen Clan-Warp§.");
                    return true;
                }


                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
                player.sendMessage("§eDein Clan besitzt §7" + clan.getWarps().size() + " §evon §7" + clan.getMaxWarps() + "§e Clan-Warps.");
                player.sendMessage(" ");

                JSONMessage message = new JSONMessage(" ");
                Iterator<String> iterator = clan.getWarps().keySet().iterator();

                while (iterator.hasNext()) {
                    String warp = iterator.next();

                    message.then("§7" + warp).tooltip("§eTeleportiere dich zum Clan-Warp §7" + warp).runCommand("/clan warp " + warp);
                    if (iterator.hasNext()) {
                        message.then("§e, ");
                    }
                }
                message.send(player);
                player.sendMessage(" ");
                player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");

            } else {
                sendHelp(player, label, 1);
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("erstellen") || args[0].equalsIgnoreCase("create")) {
                String name = args[1];

                if (name.length() > 6) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Clan-Name darf nicht länger als 6 Zeichen sein.");
                    return true;
                }

                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) != null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist bereits in einem Clan.");
                    return true;
                }

                if (Main.getInstance().getClanManager().getClan(name) != null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert bereits ein Clan mit diesem Namen.");
                    return true;
                }

                Main.getInstance().getClanManager().createClan(player.getUniqueId(), player.getName(), name);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast erfolgreich den Clan §7" + name + " §eerstellt.");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            } else if (args[0].equalsIgnoreCase("stats")) {
                String name = args[1];

                if (Main.getInstance().getClanManager().getClan(name) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein Clan mit diesem Namen.");
                    return true;
                }

                Clan clan = Main.getInstance().getClanManager().getClan(name);
                sendStats(player, clan);
            } else if (args[0].equalsIgnoreCase("changeowner")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() != ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst den Clan-Owner erst ab §4§lOWNER §cwechseln.");
                    return true;
                }

                String targetName = args[1];
                ClanMember targetMember = null;

                for (Map.Entry<UUID, ClanMember> entryMembers : clan.getMemberList().getMembers().entrySet()) {
                    if (entryMembers.getValue().getLastName().equalsIgnoreCase(targetName)) {
                        targetMember = entryMembers.getValue();
                        break;
                    }
                }
                if (targetMember == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler befindest sich nicht im Clan.");
                    return true;
                }

                if (targetMember == member) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist bereits Owner.");
                    return true;
                }

                member.setRank(ClanRank.MOD);
                targetMember.setRank(ClanRank.OWNER);

                player.sendMessage(StringDefaults.CLAN_PREFIX + "§eDu hast §7" + targetMember.getLastName() + " §ezum §4§lOWNER §egemacht.");

                Player target = Bukkit.getPlayer(targetMember.getUuid());

                if (target != null)
                    target.sendMessage(StringDefaults.CLAN_PREFIX + "§eDu wurdest zum §4§lOWNER §eernannt.");

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player || offlinePlayer == target)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ewurde zum §4§lOWNER §eernannt.");
                }
            } else if (args[0].equalsIgnoreCase("einladen") || args[0].equalsIgnoreCase("invite")) {
                if (Main.getInstance().getClanManager().getClan(player.getUniqueId()) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() == ClanRank.MEMBER || member.getRank() == ClanRank.TRUSTED) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst erst ab §5§lMOD §cSpieler einladen.");
                    return true;
                }

                if (clan.getMemberList().getMembers().size() >= clan.getMaxMembers()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat bereits die maximale Anzahl an Mitglieder erreicht.");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(StringDefaults.NOT_ONLINE);
                    return true;
                }

                if (Main.getInstance().getClanManager().hasClan(target.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler ist bereits in einem Clan.");
                    return true;
                }

                List<Clan> requests = Main.getInstance().getClanManager().getClanRequests(target.getUniqueId());
                if (requests.contains(clan)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler hat bereits eine Einladung von deinem Clan erhalten.");
                    return true;
                }

                Main.getInstance().getClanManager().addClanRequest(target.getUniqueId(), clan);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + target.getName() + " §eerfolgreich in den Clan eingeladen.");

                target.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
                target.sendMessage("§eDu hast eine Einladung vom Clan §7" + clan.getName() + " §eerhalten.");
                target.sendMessage(" ");
                target.sendMessage("§eKills§8: §7" + clan.getKills() + " §8- §eTode§8: §e" + clan.getDeaths());
                target.sendMessage("§eTrophäen§8: §7" + clan.getTrophies() + " §8- §eRanking§8: §7" + clan.getRank() + ". Platz");
                target.sendMessage("§eLevel§8: §7" + clan.getLevel());
                target.sendMessage(" ");
                new JSONMessage("§a§lAnnehmen").tooltip("§aEinladung annehmen").runCommand("/clan annehmen " + clan.getName()).then(" §8● ").then("§c§lAblehnen").tooltip("§cEinladung ablehnen").runCommand("/clan ablehnen " + clan.getName()).send(target);
                target.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player || offlinePlayer == target)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat §7" + target.getName() + " §ein den Clan eingeladen.");
                }
            } else if (args[0].equalsIgnoreCase("annehmen") || args[0].equalsIgnoreCase("accept")) {

                if (Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist bereits in einem Clan.");
                    return true;
                }

                Clan clan = Main.getInstance().getClanManager().getClan(args[1]);

                if (clan == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Clan existiert nicht.");
                    return true;
                }

                List<Clan> requests = Main.getInstance().getClanManager().getClanRequests(player.getUniqueId());

                if (!requests.contains(clan)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast keine Einladung vom Clan §7" + clan.getName() + " §cerhalten.");
                    return true;
                }

                if (clan.getMemberList().getMembers().size() >= clan.getMaxMembers()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Clan ist bereits voll.");
                    return true;
                }

                Main.getInstance().getClanManager().getClanRequests(player.getUniqueId()).clear();
                Main.getInstance().getClanManager().addMember(player.getUniqueId(), player.getName(), clan, ClanRank.MEMBER);

                player.sendMessage(StringDefaults.PREFIX + "§eDu bist dem Clan beigetreten.");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §eist dem Clan beigetreten.");
                }
            } else if (args[0].equalsIgnoreCase("ablehnen") || args[0].equalsIgnoreCase("deny")) {
                Clan clan = Main.getInstance().getClanManager().getClan(args[1]);

                if (clan == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Clan existiert nicht.");
                    return true;
                }

                List<Clan> requests = Main.getInstance().getClanManager().getClanRequests(player.getUniqueId());

                if (!requests.contains(clan)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast keine Einladung vom Clan §7" + clan.getName() + " §cerhalten.");
                    return true;
                }

                Main.getInstance().getClanManager().removeClanRequest(player.getUniqueId(), clan);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast die Clan-Einladung abgelehnt.");


                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat die Einladung abgelehnt.");
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (!Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() == ClanRank.MEMBER || member.getRank() == ClanRank.TRUSTED) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst einen Spieler erst ab §5§lMOD §ckicken.");
                    return true;
                }

                String targetName = args[1];
                ClanMember target = null;

                for (Map.Entry<UUID, ClanMember> entry : clan.getMemberList().getMembers().entrySet()) {
                    if (entry.getValue().getLastName().equalsIgnoreCase(targetName))
                        target = entry.getValue();
                    break;
                }

                if (target == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler befindet sich nicht in deinem Clan.");
                    return true;
                }

                if (target == member) {
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwende §7/clan verlassen §cum den Clan zu verlassen.");
                    return true;
                }

                if (target.getRank() == ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler kann nicht gekickt werden.");
                    return true;
                }

                player.sendMessage(StringDefaults.PREFIX + "§eDu hast §7" + target.getLastName() + " §eaus dem Clan gekickt.");

                Player targetPlayer = Bukkit.getPlayer(target.getUuid());

                if (targetPlayer == null) {
                    clan.getMemberList().removeMember(target);
                    target.deleteDataAsync();
                } else {
                    Main.getInstance().getClanManager().removeMember(target.getUuid());
                    targetPlayer.sendMessage(StringDefaults.PREFIX + "§eDu wurdest von §7" + player.getName() + " §eaus dem Clan gekickt.");
                    targetPlayer.playSound(targetPlayer.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
                }

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat §7" + target.getLastName() + " §eaus dem Clan gekickt.");
                }
            } else if (args[0].equalsIgnoreCase("setwarp")) {
                if (!Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());

                if (member.getRank() == ClanRank.MEMBER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst Warps erst ab §e§lTRUSTED §csetzen.");
                    return true;
                }

                String name = args[1];

                if (name.length() > 16) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Warp Name darf nicht länger als 16 Zeichen sein.");
                    return true;
                }

                Clan clan = member.getClan();

                if (clan.getUpgradeLevel(StringDefaults.WARP_UPGRADE) == 0) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat die Clan-Warps noch nicht freigeschaltet.");
                    return true;
                }

                if (clan.getWarp(name) == null && clan.getWarps().size() >= clan.getMaxWarps()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat bereits die maximale Anzahl an Warps erreicht.");
                    return true;
                }


                Location location = new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5D, player.getLocation().getBlockY(), player.getLocation().getBlockZ() + 0.5D);
                clan.addWarp(name, location);

                player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Clan-Warp §7" + name + " §egesetzt.");
                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat den Clan-Warp §7" + name + " §egesetzt.");
                }

            } else if (args[0].equalsIgnoreCase("warp")) {
                if (!Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (clan.getWarps().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan hat noch keine Warps gesetzt.");
                    return true;
                }

                String warp = args[1];

                if (clan.getWarp(warp) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDein Clan besitzt keinen Clan-Warp mit dem Namen.");
                    return true;
                }

                Location location = clan.getWarp(warp);
                player.teleport(location);
                //TODO: add tp delay

            } else {
                sendHelp(player, label, 1);
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (args[1].equalsIgnoreCase("deletemember")) {

                    if (!player.hasPermission("arisemc.admin")) {
                        player.sendMessage(StringDefaults.NO_PERM);
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[2]);

                    if (target != null) {
                        ClanMember member = Main.getInstance().getClanManager().getClanMembers().containsKey(target.getUniqueId()) ?
                                Main.getInstance().getClanManager().getClanMembers().get(target.getUniqueId()) :
                                new ClanMember(target.getUniqueId(), false, false);

                        if (member.getClan() == null && member.getRank() == null) {
                            player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde in der Datenbank nicht gefunden.");
                            return true;
                        }

                        if (member.getClan() == null) {
                            Main.getInstance().getClanManager().getClanMembers().remove(member.getUuid());
                            //  member.deleteDataAsync();
                        } else {

                            Clan clan = member.getClan();

                            if (clan.getMemberList().getMembers().size() - 1 > 0) {
                                if (clan.getMemberList().containsMember(member)) {
                                    ClanRank rank = member.getRank();
                                    if (rank == ClanRank.OWNER) {
                                        ClanMember newOwner = null;

                                        for (ClanMember members : clan.getMemberList().getMembers().values()) {
                                            if (members == member) continue;
                                            newOwner = members;
                                        }

                                        if (newOwner == null) return true;
                                        newOwner.setRank(ClanRank.OWNER);
                                    }
                                    member.setRank(ClanRank.MEMBER);
                                    clan.getMemberList().removeMember(member);
                                }
                                Main.getInstance().getClanManager().getClanMembers().remove(member.getUuid());
                                //    member.deleteDataAsync();
                            } else {
                                Main.getInstance().getClanManager().deleteClan(clan.getName());
                            }
                        }
                        player.sendMessage(StringDefaults.PREFIX + "§eDer Clan-Member wurde aus der Datenbank entfernt.");
                    } else {
                        UUIDFetcher.getUUID(args[2], uuid -> {
                            if (uuid == null) {
                                player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde nicht gefunden.");
                                return;
                            }

                            ClanMember member = Main.getInstance().getClanManager().getClanMembers().containsKey(uuid) ?
                                    Main.getInstance().getClanManager().getClanMembers().get(uuid) :
                                    new ClanMember(uuid, false, true);

                            if (member.getClan() == null && member.getRank() == null) {
                                player.sendMessage(StringDefaults.PREFIX + "§cDer Spieler wurde in der Datenbank nicht gefunden.");
                                return;
                            }

                            if (member.getClan() == null) {
                                Main.getInstance().getClanManager().getClanMembers().remove(member.getUuid());
                                //todo:   member.deleteDataAsync();
                            } else {

                                Clan clan = member.getClan();

                                if (clan.getMemberList().getMembers().size() - 1 > 0) {
                                    if (clan.getMemberList().containsMember(member)) {
                                        ClanRank rank = member.getRank();
                                        if (rank == ClanRank.OWNER) {
                                            ClanMember newOwner = null;

                                            for (ClanMember members : clan.getMemberList().getMembers().values()) {
                                                if (members == member) continue;
                                                newOwner = members;
                                            }

                                            if (newOwner == null) return;
                                            newOwner.setRank(ClanRank.OWNER);
                                        }
                                        member.setRank(ClanRank.MEMBER);
                                        clan.getMemberList().removeMember(member);
                                    }
                                    Main.getInstance().getClanManager().getClanMembers().remove(member.getUuid());
                                    //todo: member.deleteDataAsync();
                                } else {
                                    Main.getInstance().getClanManager().deleteClan(clan.getName());
                                }
                            }
                            player.sendMessage(StringDefaults.PREFIX + "§eDer Clan-Member wurde aus der Datenbank entfernt.");
                        });
                    }
                } else if (args[1].equalsIgnoreCase("deleteclan")) {
                    if (!player.hasPermission("arisemc.admin")) {
                        player.sendMessage(StringDefaults.NO_PERM);
                        return true;
                    }

                    Clan clan = Main.getInstance().getClanManager().getClans().containsKey(args[2]) ? Main.getInstance().getClanManager().getClans().get(args[2]) : new Clan(args[2], false, true, true);

                    if (clan.getName() == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDer Clan konnte nicht gefunden werden.");
                        return true;
                    }

                    if (Main.getInstance().getClanManager().getClans().containsKey(args[2]))
                        Main.getInstance().getClanManager().deleteClan(args[2]);
                    else {

                        ArrayList<ClanMember> members = new ArrayList<>(clan.getMemberList().getMembers().values());
                        for (ClanMember member : members) {
                            if (Main.getInstance().getClanManager().getClanMembers().containsKey(member.getUuid()))
                                Main.getInstance().getClanManager().removeMember(member.getUuid());
                            //   member.deleteDataAsync();
                        }
                        //    clan.deleteDataAsync();
                    }
                    player.sendMessage(StringDefaults.PREFIX + "§eDer Clan wurde aus der Datenbank entfernt.");
                }

            } else if (args[0].equalsIgnoreCase("setrang")) {
                if (!Main.getInstance().getClanManager().hasClan(player.getUniqueId())) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu bist in keinem Clan.");
                    return true;
                }

                ClanMember member = Main.getInstance().getClanManager().getMember(player.getUniqueId());
                Clan clan = member.getClan();

                if (member.getRank() == ClanRank.MEMBER || member.getRank() == ClanRank.TRUSTED) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst Ränge erst ab §5§lMOD §cverändern.");
                    return true;
                }

                String name = args[1];
                ClanMember target = null;

                for (Map.Entry<UUID, ClanMember> entry : clan.getMemberList().getMembers().entrySet()) {
                    if (entry.getValue().getLastName().equalsIgnoreCase(name))
                        target = entry.getValue();
                    break;
                }

                if (target == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler ist nicht in deinem Clan.");
                    return true;
                }

                if (target == member) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu kannst deinen eigenen Rang nicht ändern.");
                    return true;
                }

                ClanRank newRank = ClanRank.getByName(args[2]);

                if (newRank == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Clan-Rang existiert nicht.");
                    return true;
                }

                if (newRank == ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Ownerrang kann nicht vergeben werden.");
                    return true;
                }

                if (target.getRank() == newRank) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDieser Spieler besitzt bereits diesen Rang.");
                    return true;
                }

                if (target.getRank() == ClanRank.OWNER) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDer Rang von diesem Spieler kann nicht verändert werden.");
                    return true;
                }

                target.setRank(newRank);

                Player targetPlayer = Bukkit.getPlayer(target.getUuid());

                if (targetPlayer != null) {
                    targetPlayer.sendMessage(StringDefaults.CLAN_PREFIX + "§eDu wurdest auf " + newRank.getColor() + newRank.getName() + " §egestuft.");
                }

                for (ClanMember members : clan.getMemberList().getMembers().values()) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(members.getUuid());
                    if (offlinePlayer == null || !offlinePlayer.isOnline() || offlinePlayer == player)
                        continue;
                    offlinePlayer.getPlayer().sendMessage(StringDefaults.CLAN_PREFIX + "§7" + player.getName() + " §ehat §7" + target.getLastName() + " §eauf " + newRank.getColor() + newRank.getName() + " §egestuft.");
                }
            } else {
                sendHelp(player, label, 1);
            }

        }

        return true;
    }

    private void sendHelp(Player player, String label, int page) {
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
        if (page == 1) {
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " erstellen <Name>: §7Erstellt einen Clan");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " löschen: §7Löscht einen Clan");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " verlassen: §7Verlässt einen Clan");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " einladen <Spieler>: §7Lädt einen Spieler ein");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " kick <Spieler>: §7Kickt einen Spieler");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " <1-3>: §7Hilfe-Seiten");
        } else if (page == 2) {
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " setbase: §7Setzt die Clanbase");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " base: §7Teleportiert zur Clanbase");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " setrang <Spieler> <Rang>: §7Ändert den Clanrang");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " changeowner <Spieler>: §7Wechselt den Owner");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " rang: §7Zeigt alle Ränge an");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " <1-3>: §7Hilfe-Seiten");
        } else if (page == 3) {
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " ranking: §7Zeigt das Clanranking");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " stats [Clan]: §7Zeigt die Clanstatistiken");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " level: §7Zeigt die Clan-Level");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " shop: §7Öffnet den Clan Shop");
            player.sendMessage(StringDefaults.PREFIX + "§c#<Text>: §7Schreibt im Clanchat");
            player.sendMessage(StringDefaults.PREFIX + "§c/" + label + " <1-3>: §7Hilfe-Seiten");
        }
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
    }

    private void sendStats(Player player, Clan clan) {
        List<ClanMember> members = new ArrayList<>();
        ClanMember owner = clan.getMemberList().getMembers(ClanRank.OWNER).get(0);

        members.add(owner);
        members.addAll(clan.getMemberList().getMembers(ClanRank.MOD));
        members.addAll(clan.getMemberList().getMembers(ClanRank.TRUSTED));
        members.addAll(clan.getMemberList().getMembers(ClanRank.MEMBER));

        StringBuilder builder = new StringBuilder();

        for (ClanMember member : members) {
            Player target = Bukkit.getPlayer(member.getUuid());
            if (member.getRank() == ClanRank.OWNER)
                builder.append("§8(").append(ClanRank.OWNER.getColor()).append("O§8) ");
            if (member.getRank() == ClanRank.MOD)
                builder.append("§8(").append(ClanRank.MOD.getColor()).append("M§8) ");
            if (member.getRank() == ClanRank.TRUSTED)
                builder.append("§8(").append(ClanRank.TRUSTED.getColor()).append("T§8) ");
            boolean online = (target != null);
            if (!online || (Main.getInstance().getManager().getPlayersInVanish().contains(target) && !player.hasPermission("arisemc.vanish.see")))
                online = false;
            builder.append(online ? "§a" : "§7").append(member.getLastName()).append("§7, ");
        }

        String memberList = builder.substring(0, builder.length() - 4);

        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
        player.sendMessage("§c§lClan Stats von §7" + clan.getName() + "§8:");
        player.sendMessage("§c§lKills§8: §7" + Util.formatNumber(clan.getKills()) + " §c§lTode§8: §7" + Util.formatNumber(clan.getDeaths()));
        player.sendMessage("§c§lTrophäen§8: §7" + Util.formatNumber(clan.getTrophies()));
        player.sendMessage("§c§lClan-Coins§8: §7" + Util.formatNumber(clan.getMoney()) + " Clan-Coins");
        player.sendMessage("§c§lRanking§8: §7" + clan.getRank() + ". Platz");
        player.sendMessage(" ");
        player.sendMessage("§c§lMitglieder§8: " + memberList);
        player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §9§lAriseMC §8§l§m*-*-*-*-*-*-*-*-*");
    }
}
