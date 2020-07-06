package de.realmeze.minecraft.command;

import de.realmeze.impl.CollectionMain;
import de.realmeze.impl.collection.command.CollectionCommandImpl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CollectionCommand implements CommandExecutor {
    private CollectionMain collectionMain;
    public CollectionCommand(CollectionMain collectionMain) {
        this.collectionMain = collectionMain;
    }

    public CollectionMain getCollectionMain() {
        return collectionMain;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("collection")){
            CollectionCommandImpl collectionCommand = new CollectionCommandImpl(sender, args, getCollectionMain());
            return collectionCommand.command();
        }
        return false;
    }
}
