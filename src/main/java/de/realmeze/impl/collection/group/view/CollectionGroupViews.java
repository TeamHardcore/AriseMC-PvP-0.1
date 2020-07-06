package de.realmeze.impl.collection.group.view;

import de.realmeze.api.collection.group.ICollectionGroupView;
import de.realmeze.impl.item.ItemBuilder;
import org.bukkit.Material;

public enum CollectionGroupViews {

    FISHING(new CollectionGroupView("§bFISCHING COLLECTIONS", "Nutz die Angel zum fischen, nicht im PvP du KEK!",
            new ItemBuilder().setMaterial(Material.FISHING_ROD).setName("§9FISCHING COLLECTIONS").setAmount(1))),
    TREASURE(new CollectionGroupView("§cTREASURE COLLECTIONS", "Wertvoller Stuff, denn du sammeln kannst wow",
            new ItemBuilder().setMaterial(Material.ENCHANTED_BOOK).setAmount(1).setName("§cTREASURE COLLECTIONS"))),
    ;
        private ICollectionGroupView collectionGroupView;

        CollectionGroupViews(ICollectionGroupView defaultCollectionGroupView){
            this.collectionGroupView = defaultCollectionGroupView;
        }

        public ICollectionGroupView getCollectionGroupView(){
            return this.collectionGroupView;
        }


}
