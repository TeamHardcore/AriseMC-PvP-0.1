package de.realmeze.impl.event;

import de.realmeze.impl.MezeMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.xml.bind.Marshaller;

public class PlayerJoinEvent implements Listener {
        private MezeMain mezeMain;

        public PlayerJoinEvent(MezeMain mezeMain) {
                this.mezeMain = mezeMain;
        }

        @EventHandler
        public void onJoin(org.bukkit.event.player.PlayerJoinEvent event){
            addToCollectionReg(event.getPlayer());
        }

        private void addToCollectionReg(Player player){
                Bukkit.broadcastMessage(mezeMain.getCollectionController().show());
        }
}
