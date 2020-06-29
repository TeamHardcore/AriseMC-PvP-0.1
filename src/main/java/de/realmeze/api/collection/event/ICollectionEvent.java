package de.realmeze.api.collection.event;

import org.bukkit.event.Event;

public interface ICollectionEvent {
    boolean execute();
    void countToCollection();
}
