package de.realmeze.impl.view.group;

import de.realmeze.impl.item.ItemBuilder;
import org.bukkit.Material;

public enum CollectionGroupViews {
    ;
    public enum FISHING {
        RAWFISH(new DefaultCollectionGroupView("§1FISCHING COLLECTIONS", "Wo ist nemo, du kelb?",
                new ItemBuilder().setMaterial(Material.FISHING_ROD).setName("§9FISCHING COLLECTIONS").setAmount(1)))
        ;
        private DefaultCollectionGroupView collectionGroupView;

        FISHING(DefaultCollectionGroupView defaultCollectionGroupView){
            this.collectionGroupView = defaultCollectionGroupView;
        }

        public DefaultCollectionGroupView getCollectionGroupView(){
            return this.collectionGroupView;
        }

    }
    public enum MINING {

    }
}
