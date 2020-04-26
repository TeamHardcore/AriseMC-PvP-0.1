package de.teamhardcore.pvp.commands.player;

import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandFill implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.fill")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        int filled = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getType() != Material.GLASS_BOTTLE)
                continue;

            itemStack.setType(Material.POTION);
            filled++;
        }
        if (filled > 0) {
            player.sendMessage(StringDefaults.PREFIX + "§eAlle Glassflaschen wurden gefüllt.");
        } else {
            player.sendMessage(StringDefaults.PREFIX + "§cEs konnten keine Glassflaschen gefüllt werden.");
        }


        return true;
    }
}
