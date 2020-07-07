package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.files.*;

public class FileManager {

    private final Main plugin;
    private final WarpFile warpFile;
    private final ConfigFile configFile;
    private final SpawnerFile spawnerFile;
    private final DuelFile duelFile;
    private final FakeEntityFile fakeEntityFile;
    private final ArenaFile arenaFile;
    private final MaintenanceFile maintenanceFile;

    public FileManager(Main plugin) {
        this.plugin = plugin;
        this.configFile = new ConfigFile();
        this.warpFile = new WarpFile();
        this.spawnerFile = new SpawnerFile();
        this.duelFile = new DuelFile();
        this.fakeEntityFile = new FakeEntityFile();
        this.arenaFile = new ArenaFile();
        this.maintenanceFile = new MaintenanceFile();
    }

    public ArenaFile getArenaFile() {
        return arenaFile;
    }

    public FakeEntityFile getFakeEntityFile() {
        return fakeEntityFile;
    }

    public DuelFile getDuelFile() {
        return duelFile;
    }

    public SpawnerFile getSpawnerFile() {
        return spawnerFile;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public WarpFile getWarpFile() {
        return warpFile;
    }

    public MaintenanceFile getMaintenanceFile() {
        return maintenanceFile;
    }

    public Main getPlugin() {
        return plugin;
    }
}
