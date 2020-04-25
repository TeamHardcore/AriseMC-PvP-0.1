package de.teamhardcore.pvp.model;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Support {

    private HashMap<Player, SupportRole> roles = new HashMap<>();

    public Support(Player player, Player supporter) {
        this.roles.put(player, SupportRole.MEMBER);
        this.roles.put(supporter, SupportRole.SUPPORTER);
    }

    public List<Player> getPlayers(SupportRole role) {
        ArrayList<Player> players = new ArrayList<>();
        for (Map.Entry<Player, SupportRole> entryPlayers : this.roles.entrySet()) {
            if (entryPlayers.getValue() != role) continue;
            players.add(entryPlayers.getKey());
        }
        return players;
    }

    public HashMap<Player, SupportRole> getRoles() {
        return roles;
    }

    public enum SupportRole {
        MEMBER("Spieler"),
        SUPPORTER("Supporter");

        private String name;

        SupportRole(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
