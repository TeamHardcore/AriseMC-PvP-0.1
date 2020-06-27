package de.howaner.FakeMobs.util;

import de.howaner.FakeMobs.FakeMobsPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public class LookUpdate implements Runnable {

    @Override
    public void run() {
        try {
            for (FakeMob mob : FakeMobsPlugin.getMobs()) {
                if (!mob.isPlayerLook()) continue;
                List<Player> players = mob.getNearbyPlayers(5D);
                for (Player p : players)
                    mob.sendLookPacket(p, p.getLocation());
            }
        } catch (Exception e) {
        }
    }

}
