package de.realmeze.minecraft;

import de.realmeze.impl.CollectionMain;
import de.realmeze.impl.collection.event.FishingCollectionEventWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerCaughtFishListener implements Listener {

    private CollectionMain collectionMain;

    public PlayerCaughtFishListener(CollectionMain collectionMain) {
        this.collectionMain = collectionMain;
    }

    @EventHandler
    public void playerCaughtFish(PlayerFishEvent event){
        FishingCollectionEventWrapper fishingEventWrapper = new FishingCollectionEventWrapper(event, collectionMain);
        fishingEventWrapper.execute();
    }

}
