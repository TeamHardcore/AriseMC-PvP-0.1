package de.realmeze.impl;

import de.realmeze.api.collection.player.IPlayer;
import org.bukkit.entity.Player;

public class CollectionPlayer implements IPlayer {

    private Player player;

    public CollectionPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getVanillaPlayer() {
        return this.player;
    }
}
