package de.realmeze.impl.collection.command;

import de.realmeze.api.collection.player.IPlayerCollectionData;
import de.realmeze.impl.CollectionMain;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CollectionCommandImpl {

    private Player player;
    private String[] args;
    private CollectionMain collectionMain;

    public CollectionCommandImpl(CommandSender sender, String[] args, CollectionMain collectionMain) {
        assert sender instanceof Player;
        this.player = (Player) sender;
        this.args = args;
        this.collectionMain = collectionMain;
    }

    public boolean command() {
        if (getArgs().length == 0) {
            player.sendMessage("Collections:");
            collectionMain.getCollectionController().showCollectionsInChat(player);
            return true;
        } else if (getArgs().length == 1) {
            if (getArgs(0).equalsIgnoreCase("stats")) {
                for (IPlayerCollectionData playerData: collectionMain.getCollectionController().getAllPlayerData(player)) {
                    player.sendMessage(playerData.getCollectionType().getName() + " amount" + playerData.getCollectedAmount());
                }
            }
            if (getArgs(0).equalsIgnoreCase("upall")) {
                for (IPlayerCollectionData playerData: collectionMain.getCollectionController().getAllPlayerData(player)) {
                    playerData.countUp(1);
                }
            }
            else if(getArgs(0).equalsIgnoreCase("debug")){

            }
        }

        return false;
    }


    public CollectionMain getCollectionMain() {
        return collectionMain;
    }

    public String[] getArgs() {
        return args;
    }

    public String getArgs(int i) {
        return getArgs()[i];
    }
}
