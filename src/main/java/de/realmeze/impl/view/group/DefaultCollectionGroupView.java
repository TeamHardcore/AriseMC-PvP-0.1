package de.realmeze.impl.view.group;

import de.realmeze.api.collection.group.ICollectionGroupView;
import de.realmeze.api.item.IItem;

public class DefaultCollectionGroupView implements ICollectionGroupView {

    private String displayName;
    private String description;
    private IItem displayItem;

    public DefaultCollectionGroupView(String displayName, String description, IItem displayItem) {
        this.displayName = displayName;
        this.description = description;
        this.displayItem = displayItem;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public IItem getDisplayItem() {
        return this.displayItem;
    }
}
