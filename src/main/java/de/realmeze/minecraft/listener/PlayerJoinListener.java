package de.realmeze.minecraft.listener;

import de.realmeze.impl.CollectionMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

        private CollectionMain collectionMain;

        public PlayerJoinListener(CollectionMain collectionMain) {
                this.collectionMain = collectionMain;
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event){
            addToCollectionReg(event.getPlayer());
            Bukkit.broadcastMessage("test");
        }

        private void addToCollectionReg(Player player){
                collectionMain.getCollectionController().registerPlayerInCollections(player);
        }
}
