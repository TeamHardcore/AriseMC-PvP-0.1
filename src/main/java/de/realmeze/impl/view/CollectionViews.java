package de.realmeze.impl.view;

import de.realmeze.impl.item.ItemBuilder;
import org.bukkit.Material;

public enum CollectionViews {
    ;
    public enum Fishing{
        RAWFISH(new DefaultCollectionView("Fisch", "Wo ist nemo, du kelb?",
                new ItemBuilder().setMaterial(Material.RAW_FISH).setName("§bROHER FISCH").setAmount(1))),
        ;
        private DefaultCollectionView collectionView;

        Fishing(DefaultCollectionView collectionView) {
            this.collectionView = collectionView;
        }

        public DefaultCollectionView getCollectionView() {
            return this.collectionView;
        }
    }
}
