package de.realmeze.impl.collection.data.player;

import de.realmeze.api.collection.collection.ICollectionType;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "player-data")
public class PlayerCollectionData implements IPlayerCollectionData {

    private final Player player;
    private final ICollectionType collectionType;
    private long collectedAmount;
    @Column
    private int currentMilestoneTier;

    public PlayerCollectionData(Player player, ICollectionType collectionType) {
        this.player = player;
        this.collectionType = collectionType;
        set();
    }

    private void set(){
        this.collectedAmount = 0;
        this.currentMilestoneTier = 0;
    }

    @Override
    public ICollectionType getCollectionType() {
        return this.collectionType;
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
    public void countUp(long amount) {
        this.collectedAmount = getCollectedAmount() + amount;
    }

    @Override
    public void countDown(long amount) {
        this.collectedAmount = getCollectedAmount() - amount;
    }

    @Override
    public Player getVanillaPlayer() {
        return player;
    }
}
