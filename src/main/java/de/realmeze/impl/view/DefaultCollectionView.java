package de.realmeze.impl.view;

import de.realmeze.api.collection.collection.ICollectionView;
import de.realmeze.api.item.IItem;

public class DefaultCollectionView implements ICollectionView {

    private String displayName;
    private String description;
    private IItem displayItem;

    public DefaultCollectionView(String displayName, String description, IItem displayItem) {
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
