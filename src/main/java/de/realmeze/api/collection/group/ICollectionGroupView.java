package de.realmeze.api.collection.group;

import de.realmeze.api.item.IItem;

public interface ICollectionGroupView {
    String getDisplayName();
    String getDescription();
    IItem getDisplayItem();
}
