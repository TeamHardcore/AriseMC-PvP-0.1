package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.files.ConfigFile;
import de.teamhardcore.pvp.files.WarpFile;

public class FileManager {

    private final Main plugin;
    private WarpFile warpFile;
    private ConfigFile configFile;

    public FileManager(Main plugin) {
        this.plugin = plugin;
        this.configFile = new ConfigFile();
        this.warpFile = new WarpFile();
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public WarpFile getWarpFile() {
        return warpFile;
    }

    public Main getPlugin() {
        return plugin;
    }
}
