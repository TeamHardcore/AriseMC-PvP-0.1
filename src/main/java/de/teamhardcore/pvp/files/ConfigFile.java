/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.files;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigFile extends FileBase {

    private List<String> blockedCombatCommands = new ArrayList<>();

    public ConfigFile() {
        super("", "config");

        writeDefaults();
        loadBlockedCombatCommands();
    }

    private void writeDefaults() {
        FileConfiguration cfg = getConfig();
        cfg.addDefault("MySQL.Host", "host");
        cfg.addDefault("MySQL.Port", "3306");
        cfg.addDefault("MySQL.User", "user");
        cfg.addDefault("MySQL.Pass", "pass");
        cfg.addDefault("MySQL.DB", "db");
        cfg.addDefault("BlockedCombatCommands", Arrays.asList("cmd1", "cmd2"));
        cfg.options().copyDefaults(true);
        saveConfig();
    }

    private void loadBlockedCombatCommands() {
        FileConfiguration cfg = getConfig();
        if (cfg.get("BlockedCombatCommands") == null)
            return;
        for (String allowed : cfg.getStringList("BlockedCombatCommands")) {
            this.blockedCombatCommands.add(allowed.toLowerCase());
        }
    }

    public List<String> getBlockedCombatCommands() {
        return blockedCombatCommands;
    }
}
