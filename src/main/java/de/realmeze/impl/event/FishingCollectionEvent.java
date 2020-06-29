package de.realmeze.impl.event;

import de.realmeze.api.collection.event.ICollectionEvent;
import de.realmeze.impl.CollectionPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingCollectionEvent implements ICollectionEvent {

    private PlayerFishEvent event;

    public FishingCollectionEvent(Event event){
        if (event instanceof PlayerFishEvent) {
            this.event = (PlayerFishEvent) event;
        }
    }

    @Override
    public boolean execute() {
        countToCollection();
        event.setCancelled(true);
        return false;
    }

    @Override
    public void countToCollection() {
        CollectionPlayer collectionPlayer = new CollectionPlayer(event.getPlayer());
    }
}
