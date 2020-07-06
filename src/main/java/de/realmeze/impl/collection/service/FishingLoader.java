package de.realmeze.impl.collection.service;

import de.realmeze.api.collection.collection.ICollection;
import de.realmeze.api.collection.collection.ICollectionMilestone;
import de.realmeze.api.collection.collection.ICollectionType;
import de.realmeze.api.collection.collection.ICollectionView;
import de.realmeze.api.collection.player.IPlayerCollectionData;
import de.realmeze.api.collection.service.ICollectionLoader;
import de.realmeze.impl.collection.Collection;
import de.realmeze.impl.collection.data.collection.CollectionData;
import de.realmeze.impl.collection.identifier.CollectionId;
import de.realmeze.impl.item.ItemBuilder;
import de.realmeze.impl.item.CollectionItem;
import de.realmeze.impl.collection.milestone.Milestones;
import de.realmeze.impl.collection.type.CollectionTypes;
import de.realmeze.impl.collection.view.CollectionViews;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class FishingLoader implements ICollectionLoader {

    public FishingLoader() {
    }

    @Override
    public ArrayList<ICollection> load() {
        ArrayList<ICollection> collections = new ArrayList<ICollection>();
        ItemBuilder rawfish = new ItemBuilder();
        ItemBuilder salmonbuilder = new ItemBuilder();
        ItemBuilder puffer = new ItemBuilder();
        ItemBuilder clown = new ItemBuilder();
        ICollectionType rawfishType = CollectionTypes.FISHINGTYPES.RAWFISH.getCollectionType();
        ICollectionType salmonType = CollectionTypes.FISHINGTYPES.SALMON.getCollectionType();
        ICollectionType clownfishType = CollectionTypes.FISHINGTYPES.CLOWNFISH.getCollectionType();
        ICollectionType pufferfishType = CollectionTypes.FISHINGTYPES.PUFFERFISH.getCollectionType();

        ICollectionView rawfishView = CollectionViews.Fishing.RAWFISH.getCollectionView();
        CollectionItem rawfishCollectionItem = rawfish.setMaterial(Material.RAW_FISH).setLore(0, rawfishType.getRarity().getRarityName()).buildCollectionItem();
        ICollectionView salmonView = CollectionViews.Fishing.SALMON.getCollectionView();
        CollectionItem salmonCollectionItem = salmonbuilder.setMaterial(Material.RAW_FISH).setLore(0, salmonType.getRarity().getRarityName()).setFishType(1).buildCollectionItem();
        ICollectionView clownfishView = CollectionViews.Fishing.CLOWNFISH.getCollectionView();
        CollectionItem clownfishCollectionItem = puffer.setMaterial(Material.RAW_FISH).setLore(0, clownfishType.getRarity().getRarityName()).setFishType(2).buildCollectionItem();
        ICollectionView pufferfishView = CollectionViews.Fishing.PUFFERFISH.getCollectionView();
        CollectionItem pufferfishCollectionItem = clown.setMaterial(Material.RAW_FISH).setLore(0, pufferfishType.getRarity().getRarityName()).setFishType(3).buildCollectionItem();

        CollectionId rawFishId = new CollectionId(rawfishType, rawfishView.getDisplayName());
        CollectionId salmonId = new CollectionId(salmonType, salmonView.getDisplayName());
        CollectionId clownfishId = new CollectionId(clownfishType, clownfishView.getDisplayName());
        CollectionId pufferfishId = new CollectionId(pufferfishType, pufferfishView.getDisplayName());
        ArrayList<ICollectionMilestone> fishingMilestones = new ArrayList<ICollectionMilestone>() {{
            add(Milestones.Fishing.FISH0.getMilestone());
            add(Milestones.Fishing.FISH1.getMilestone());
            add(Milestones.Fishing.FISH2.getMilestone());
            add(Milestones.Fishing.FISH3.getMilestone());
            add(Milestones.Fishing.FISH4.getMilestone());
            add(Milestones.Fishing.FISH5.getMilestone());
            add(Milestones.Fishing.FISH6.getMilestone());
            add(Milestones.Fishing.FISH7.getMilestone());
            add(Milestones.Fishing.FISH8.getMilestone());
            add(Milestones.Fishing.FISH9.getMilestone());
            add(Milestones.Fishing.FISH10.getMilestone());
        }};

        ICollection rawFish = new Collection(rawfishType, rawfishView, rawfishCollectionItem,
                new CollectionData(fishingMilestones, new HashMap<Player, IPlayerCollectionData>()), rawFishId, new String[]{"fishing"});
        ICollection salmon = new Collection(salmonType, salmonView, salmonCollectionItem,
                new CollectionData(fishingMilestones, new HashMap<Player, IPlayerCollectionData>()), salmonId, new String[]{"fishing"});
        ICollection clownfish = new Collection(clownfishType, clownfishView, clownfishCollectionItem,
                new CollectionData(fishingMilestones, new HashMap<Player, IPlayerCollectionData>()), clownfishId, new String[]{"fishing"});
        ICollection pufferfish = new Collection(pufferfishType, pufferfishView, pufferfishCollectionItem,
                new CollectionData(fishingMilestones, new HashMap<Player, IPlayerCollectionData>()), pufferfishId, new String[]{"fishing"});
        collections.add(rawFish);
        collections.add(salmon);
        collections.add(clownfish);
        collections.add(pufferfish);
        return collections;
    }
}
