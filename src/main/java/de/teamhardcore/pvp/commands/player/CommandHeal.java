/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.heal")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/heal [Spieler]");
            return true;
        }

        if (args.length == 0) {
            boolean hasPermission = player.hasPermission("arisemc.heal.bypass");

            if (Main.getInstance().getManager().getHealCooldown().containsKey(player) || !hasPermission) {
                long diff = Main.getInstance().getManager().getHealCooldown().get(player) - System.currentTimeMillis();

                if (diff / 1000L > 0L) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu musst noch §7" + TimeUtil.timeToString(diff) + " §cwarten, bevor du dich erneut heilen kannst.");
                    return true;
                }
                Main.getInstance().getManager().getHealCooldown().remove(player);
            }

            player.setHealth(20);
            player.setFoodLevel(30);
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.setFireTicks(0);

            player.sendMessage(StringDefaults.PREFIX + "§eDu wurdest geheilt.");
            Main.getInstance().getManager().getHealCooldown().put(player, System.currentTimeMillis() + 3600000L);


        }

        if (args.length == 1) {
            if (!player.hasPermission("arisemc.heal.other")) {
                player.sendMessage(StringDefaults.NO_PERM);
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {

                return true;
            }

            if (target == player) {
                player.performCommand("/heal");
                return true;
            }

            boolean hasPermission = player.hasPermission("arisemc.heal.bypass");

            if (Main.getInstance().getManager().getHealCooldown().containsKey(player) || !hasPermission) {
                long diff = Main.getInstance().getManager().getHealCooldown().get(player) - System.currentTimeMillis();

                if (diff / 1000L > 0L) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu musst noch §7" + TimeUtil.timeToString(diff) + " §cwarten, bevor du dich erneut heilen kannst.");
                    return true;
                }
                Main.getInstance().getManager().getHealCooldown().remove(player);
            }

            target.setHealth(20);
            target.setFoodLevel(30);
            target.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            target.setFireTicks(0);

            target.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
            target.playEffect(player.getLocation(), Effect.HEART, 1.0F);

            target.sendMessage(StringDefaults.PREFIX + "§eDu wurdest geheilt.");
            player.sendMessage(StringDefaults.PREFIX + "§eDu hast den Spieler §7" + target.getName() + " §eerfolgreich geheilt.");
            Main.getInstance().getManager().getHealCooldown().put(player, System.currentTimeMillis() + 3600000L);
        }

        return true;
    }
}
