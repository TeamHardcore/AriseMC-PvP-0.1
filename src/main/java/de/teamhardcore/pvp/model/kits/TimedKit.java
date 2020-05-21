/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.kits;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.TimeUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TimedKit extends Kit {

    private long cooldown;

    public TimedKit(String name, List<ItemStack> content, long cooldown) {
        super(name, content);

        this.cooldown = cooldown;
    }

    public long getCooldown() {
        return cooldown;
    }

    @Override
    public boolean giveKit(Player player) {
        UserData data = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserData();

        long diff = data.getKitCooldowns().containsKey(getName()) ? (data.getKitCooldowns().get(getName()) - System.currentTimeMillis()) : -1L;

        if (diff / 1000L > 0L) {
            player.sendMessage(StringDefaults.PREFIX + "§cDas Kit §7" + getName() + " §cist in §7" + TimeUtil.timeToString(diff) + " §cwieder verfübar.");
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
            return false;
        }

        player.closeInventory();
        data.addKitCooldown(getName(), this.cooldown + System.currentTimeMillis());
        super.giveKit(player);
        return true;
    }
}
