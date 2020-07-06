package de.realmeze.impl;

import de.realmeze.impl.collection.CollectionController;
import de.realmeze.minecraft.PlayerCaughtFishListener;
import de.realmeze.minecraft.command.CollectionCommand;
import de.realmeze.minecraft.listener.PlayerJoinListener;
import de.teamhardcore.pvp.Main;
import org.bukkit.plugin.PluginManager;

public class CollectionMain {

    private CollectionController collectionController;

    private Main main;

    public CollectionMain(Main main) {
        this.main = main;
        registerCommands();
        registerListeners();
        init();
    }

    public CollectionController getCollectionController() {
        return collectionController;
    }

    private void registerListeners(){
        PluginManager pm = main.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), main);
        pm.registerEvents(new PlayerCaughtFishListener(this), main);
    }

    private void registerCommands(){
        main.getCommand("collection").setExecutor(new CollectionCommand(this));
    }

    private void init() {
        this.collectionController = new CollectionController();
        collectionController.initCollections();
    }

}
