package de.realmeze.impl.collection.service;

import de.realmeze.api.collection.collection.*;
import de.realmeze.api.collection.collection.treasure.ITreasure;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import de.realmeze.api.collection.service.ICollectionLoader;
import de.realmeze.impl.collection.Collection;
import de.realmeze.impl.collection.data.collection.CollectionData;
import de.realmeze.impl.collection.identifier.CollectionId;
import de.realmeze.impl.collection.treasure.CrateTreasure;
import de.realmeze.impl.collection.type.CollectionTypes;
import de.realmeze.impl.collection.view.CollectionViews;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TreasureLoader implements ICollectionLoader {
    @Override
    public ArrayList<ICollection> load() {
        ArrayList<ICollection> collections = new ArrayList<ICollection>();
        ITreasure divineCrate = new CrateTreasure("DivineCrate");
        ICollectionData divineData = new CollectionData(new ArrayList<ICollectionMilestone>(), new HashMap<Player, IPlayerCollectionData>());
        ICollectionType divineType = CollectionTypes.TREASURETYPES.DIVINECRATE.getCollectionType();
        ICollectionView divineView = CollectionViews.Treasure.DIVINECRATE.getCollectionView();
        CollectionId divineId = new CollectionId(divineType, divineView.getDisplayName());
        ICollection divineCollection = new Collection(divineType, divineView, divineCrate, divineData, divineId, new String[]{"fishing"});
        collections.add(divineCollection);
        return collections;
    }
}
