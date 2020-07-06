package de.realmeze.impl.collection.view;

import de.realmeze.api.collection.collection.ICollectionView;
import de.realmeze.impl.item.ItemBuilder;
import org.bukkit.Material;

public enum CollectionViews {
    ;
    public enum Fishing{
        RAWFISH(new DefaultCollectionView("Fish", "Der langweiligste aller Fische",
                new ItemBuilder().setMaterial(Material.RAW_FISH).setName("§bRoher Fisch").setAmount(1))),
        SALMON(new DefaultCollectionView("Salmon", "Nen Fisch halt",
                new ItemBuilder().setMaterial(Material.RAW_FISH).setFishType(1).setName("§bLachs").setAmount(1))),
        CLOWNFISH(new DefaultCollectionView("Clownfish", "Fast wie unser Owner KEKW",
                new ItemBuilder().setMaterial(Material.RAW_FISH).setFishType(2).setName("§Clownfish").setAmount(1))),
        PUFFERFISH(new DefaultCollectionView("Pufferfish", "Aua",
                new ItemBuilder().setMaterial(Material.RAW_FISH).setFishType(3).setName("§Pufferfisch").setAmount(1))),
        ;
        private ICollectionView collectionView;

        Fishing(ICollectionView collectionView) {
            this.collectionView = collectionView;
        }

        public ICollectionView getCollectionView() {
            return this.collectionView;
        }
    }

    public enum Treasure {
        DIVINECRATE(new DefaultCollectionView("Divine Crate", "woaw omegalul",
                new ItemBuilder().setMaterial(Material.CHEST).setName("§3DIVINE CRATE").setAmount(1)));
        private ICollectionView collectionView;

        Treasure(ICollectionView collectionView) {
            this.collectionView = collectionView;
        }

        public ICollectionView getCollectionView() {
            return this.collectionView;
        }
    }
}
