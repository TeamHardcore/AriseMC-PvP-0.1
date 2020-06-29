package de.realmeze.minecraft;

import de.realmeze.impl.event.FishingCollectionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerCaughtFishListener implements Listener {
    @EventHandler
    public void playerCaughtFish(PlayerFishEvent event){
        FishingCollectionEvent fishingEventWrapper = new FishingCollectionEvent(event);
        fishingEventWrapper.execute();
    }

}
