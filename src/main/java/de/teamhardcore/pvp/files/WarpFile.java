package de.teamhardcore.pvp.files;

import de.teamhardcore.pvp.model.Warp;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class WarpFile extends FileBase {

    private final HashMap<String, Warp> warps = new HashMap<>();

    public WarpFile() {
        super("", "warps");

        loadWarps();
    }

    private void loadWarps() {
        this.warps.clear();
        FileConfiguration cfg = getConfig();
        if (cfg.get("Warps") == null) return;

        for (String name : cfg.getConfigurationSection("Warps").getKeys(false)) {
            Location location = Util.stringToLocation(cfg.getString("Warps." + name));
            this.warps.put(name, new Warp(name, location));
        }
    }

    public void addWarp(String name, Location location) {
        this.warps.put(name, new Warp(name, location));

        FileConfiguration cfg = getConfig();
        cfg.set("Warps." + name, Util.locationToString(location));
        saveConfig();
    }

    public void removeWarp(String name) {
        if (!this.warps.containsKey(name)) return;

        this.warps.remove(name);

        FileConfiguration cfg = getConfig();
        cfg.set("Warps." + name, null);
        saveConfig();
    }

    public HashMap<String, Warp> getWarps() {
        return warps;
    }
}
