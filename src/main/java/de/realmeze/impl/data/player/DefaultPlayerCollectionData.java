package de.realmeze.impl.data.player;

import de.realmeze.api.collection.player.IPlayer;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import org.bukkit.entity.Player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "player-data")
public class DefaultPlayerCollectionData implements IPlayerCollectionData {

    @Column
    private final IPlayer player;
    @Column
    private long collectedAmount;
    @Column
    private int currentMilestoneTier;

    public DefaultPlayerCollectionData(IPlayer player) {
        this.player = player;
    }

    @Override
    public long getCollectedAmount() {
        return this.collectedAmount;
    }

    @Override
    public int getCurrentMilestoneTier() {
        return this.currentMilestoneTier;
    }

    @Override
    public Player getVanillaPlayer() {
        // deal with iplayer impls later KEKW
        return player.getVanillaPlayer();
    }
}
