package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.Support;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class SupportManager {

    private final Main plugin;

    private final ArrayList<Player> waitingPlayers = new ArrayList<>();
    private final HashMap<Player, Support> supports = new HashMap<>();

    public SupportManager(Main plugin) {
        this.plugin = plugin;

        startInformationTask();
    }

    public void setWaiting(Player player, boolean waiting) {
        if (waiting) {
            if (this.waitingPlayers.contains(player)) return;
            this.waitingPlayers.add(player);
        } else {
            if (!this.waitingPlayers.contains(player)) return;
            this.waitingPlayers.remove(player);
        }
    }

    public void createSupport(Player player, Player supporter) {
        if (this.supports.containsKey(player) || this.supports.containsKey(supporter)) return;

        Support support = new Support(player, supporter);
        this.supports.put(player, support);
        this.supports.put(supporter, support);
    }

    public boolean isWaiting(Player player) {
        return this.waitingPlayers.contains(player);
    }

    public Support getSupport(Player player) {
        if (!this.supports.containsKey(player)) return null;
        return this.supports.get(player);
    }

    private void startInformationTask() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            if (this.waitingPlayers.isEmpty()) return;

            int size = this.waitingPlayers.size();
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (all.hasPermission("arisemc.support.notify")) {
                    all.sendMessage(StringDefaults.PREFIX + "§cEs benötig" + (size == 1 ? "t" : "en") + " noch §7" + size + " Spieler Support!");
                }
            }

        }, 2400L, 2400L);


    }

    public ArrayList<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    public HashMap<Player, Support> getSupports() {
        return supports;
    }

    public Main getPlugin() {
        return plugin;
    }
}
