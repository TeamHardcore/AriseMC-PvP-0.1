package de.realmeze.impl.item;

import de.realmeze.api.collection.collection.ICollectable;
import org.bukkit.Material;

public class CollectionItem extends AriseBaseItemBuilder implements ICollectable {
    @Override
    public Material getToCollect() {
        return getVanillaItemStack().getType();
    }
}
