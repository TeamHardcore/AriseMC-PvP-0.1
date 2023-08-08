package de.realmeze.impl;

import de.realmeze.impl.event.PlayerJoinEvent;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.listeners.player.AsyncPlayerChat;
import org.bukkit.plugin.PluginManager;

@Getter
public class MezeMain {

    private CollectionController collectionController;

    private Main main;

    public MezeMain(Main main) {
        this.main = main;
        // this should init the collection part ay
        rL();
        init();
    }

    private void rL(){
        PluginManager pm = main.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinEvent(this), main);
    }

    private void init() {
        this.collectionController = new CollectionController();
        collectionController.initCollections();
    }
}
