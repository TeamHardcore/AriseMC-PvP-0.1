package de.realmeze.api.collection.collection;

import de.realmeze.api.item.IItem;

public interface ICollectionView {
    String getDisplayName();
    String getDescription();
    IItem getDisplayItem();
}
