package de.realmeze.impl.collection.event;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.event.ICollectionEvent;
import de.realmeze.api.fishing.ICustomRod;
import de.realmeze.api.fishing.IFishingTime;
import de.realmeze.api.fishing.ISetFishingTime;
import de.realmeze.impl.CollectionMain;
import de.realmeze.impl.fishing.CustomRod;
import de.realmeze.impl.fishing.SetFishingTime;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;

public class FishingCollectionEventWrapper implements ICollectionEvent {

    private PlayerFishEvent event;
    private CollectionMain collectionMain;

    public FishingCollectionEventWrapper(PlayerFishEvent event, CollectionMain collectionMain) {
        this.event = event;
        this.collectionMain = collectionMain;
    }

    @Override
    public void execute() {
        ICustomRod customRod = new CustomRod(event.getPlayer().getItemInHand());
        IFishingTime fishingTime = customRod.getTime();
        ISetFishingTime setFishingTime = new SetFishingTime(fishingTime);
        setFishingTime.setCatchTime(event.getHook());
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            awardCollections();
            event.setExpToDrop(0);
            event.getCaught().remove();
        }
    }

    @Override
    public void awardCollections() {
        for (ICollection collection : getCollections()) {
            collection.getCollectable().giveToPlayer(event.getPlayer());
            collection.getData().getPlayerCollectionDataHashMap().get(event.getPlayer()).countUp(1);
            event.getPlayer().sendMessage(StringDefaults.GLOBAL_PREFIX + " §a+1 §7<" + collection.getType().getRarity().getRarityName() + "> " + collection.getType().getName().toUpperCase());
        }
    }

    @Override
    public ArrayList<ICollection> getCollections() {
        ArrayList<ICollection> fishingCollections = new ArrayList<>();
        for (ICollection collection : collectionMain.getCollectionController().getCollections()) {
            for (String dropsIn : collection.drops()) {
                if (dropsIn.equalsIgnoreCase("fishing")) {
                    fishingCollections.add(collection);
                }
            }
        }
        return fishingCollections;
    }
}
