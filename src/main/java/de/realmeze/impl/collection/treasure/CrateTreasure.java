package de.realmeze.impl.collection.treasure;

import de.realmeze.api.collection.collection.treasure.ITreasureType;
import de.realmeze.impl.collection.treasure.type.TreasureTypes;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CrateTreasure extends AbstractTreasure {

    private String crateName;

    public CrateTreasure(String crateName) {
        super(TreasureTypes.CRATE.getTreasureType());
        this.crateName = crateName;
    }

    @Override
    public void giveToPlayer(Player player) {
        Main main = Main.getInstance();
        Bukkit.broadcastMessage("lelelel");
        BaseCrate crate = main.getCrateManager().getCrate(this.crateName);
        main.getUserManager().getUser(player.getUniqueId()).getUserData().addCrate(crate);
    }
}
