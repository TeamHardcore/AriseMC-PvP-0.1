/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.howaner.FakeMobs.FakeMobsPlugin;
import de.howaner.FakeMobs.util.FakeMob;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.fakeentity.FakeEntity;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionBase;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityType;
import de.teamhardcore.pvp.model.fakeentity.options.CommandFakeEntityOption;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class FakeEntityManager {

    private final Main plugin;

    private final Map<String, FakeEntity> customEntities;
    private final Map<FakeEntityOptionBase, Class<?>> options;

    private BukkitTask effectTask;

    public FakeEntityManager(Main plugin) {
        this.plugin = plugin;
        this.customEntities = new HashMap<>();
        this.options = new HashMap<>();

        initEntityOptions();
    }

    private void initEntityOptions() {
        this.options.put(new FakeEntityOptionBase(FakeEntityOptionBase.ExecutingState.CLICK, "general", "command"), CommandFakeEntityOption.class);
    }

    public void loadAllCustomEntities() {
        FileConfiguration cfg = this.plugin.getFileManager().getFakeEntityFile().getConfig();
        if (cfg.get("") == null) return;
        for (String name : cfg.getConfigurationSection("").getKeys(false)) {
            FakeEntity entity = new FakeEntity(name);
            this.customEntities.put(name, entity);
        }
    }

    public void onDisable() {
        for (Map.Entry<String, FakeEntity> entry : this.customEntities.entrySet()) {
            FakeEntity entity = entry.getValue();
            FakeMobsPlugin.removeMob(entity.getFakeMob().getId());
            entity.setVisible(false);
        }
    }

    public boolean isEntityExists(String name) {
        return this.customEntities.containsKey(name);
    }

    public void createNewCustomEntity(String name, Location location, FakeEntityType type) {
        if (isEntityExists(name)) return;
        FakeEntity entity = new FakeEntity(name, location, type);
        this.customEntities.put(name, entity);
    }

    public void removeCustomEntity(String name) {
        if (!isEntityExists(name)) return;
        FakeEntity entity = getEntityByName(name);
        this.customEntities.remove(name);
        entity.setVisible(false);
        entity.deleteData();
    }

    public FakeEntity getEntityByName(String name) {
        if (!this.customEntities.containsKey(name)) return null;
        return this.customEntities.get(name);
    }

    public FakeEntity getEntityByFakemob(FakeMob mob) {
        for (FakeEntity entity : this.customEntities.values()) {
            if (entity.getFakeMob().equals(mob))
                return entity;
        }
        return null;
    }


    public List<FakeEntityOptionBase> getOptionsByState(FakeEntityOptionBase.ExecutingState state) {
        List<FakeEntityOptionBase> options = new ArrayList<>();
        for (FakeEntityOptionBase option : this.options.keySet()) {
            if (option.getExecutingState() == state)
                options.add(option);
        }
        return options;
    }

    public List<FakeEntityOptionBase> getOptionsByStateAndCategory(FakeEntityOptionBase.ExecutingState state, String category) {
        List<FakeEntityOptionBase> options = new ArrayList<>();
        for (FakeEntityOptionBase option : this.options.keySet()) {
            if (option.getExecutingState() == state && option.getCategory().equalsIgnoreCase(category))
                options.add(option);
        }
        return options;
    }

    public FakeEntityOptionBase getOption(FakeEntityOptionBase.ExecutingState state, String category, String optionName) {
        for (FakeEntityOptionBase option : getOptionsByStateAndCategory(state, category)) {
            if (option.getOptionName().equalsIgnoreCase(optionName))
                return option;
        }
        return null;
    }

    public Class<?> getOptionClass(FakeEntityOptionBase option) {
        if (!this.options.containsKey(option))
            return null;
        return this.options.get(option);
    }

    public void clearInteractCooldowns(Player player) {
        for (FakeEntity entity : this.customEntities.values())
            entity.getInteractCooldowns().remove(player);
    }

    public Map<FakeEntityOptionBase, Class<?>> getOptions() {
        return options;
    }

    public Map<String, FakeEntity> getCustomEntities() {
        return customEntities;
    }
}
