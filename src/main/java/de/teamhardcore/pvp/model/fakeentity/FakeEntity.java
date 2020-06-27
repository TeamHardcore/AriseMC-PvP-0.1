/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.fakeentity;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.howaner.FakeMobs.FakeMobsPlugin;
import de.howaner.FakeMobs.util.FakeMob;
import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class FakeEntity {

    private final String name;
    private Location location;
    private List<String> nametag;
    private Hologram hologram;

    private FakeEntityType type;
    private FakeMob fakeMob;
    private boolean visible;
    private final Map<Player, Long> interactCooldowns;
    private final Set<FakeEntityOptionImpl> entityOptions;


    public FakeEntity(String name) {
        this.name = name;
        this.nametag = new ArrayList<>();
        this.interactCooldowns = new HashMap<>();
        this.entityOptions = new HashSet<>();

        loadData();
    }

    public FakeEntity(String name, Location location, FakeEntityType type) {
        this.nametag = new ArrayList<>();
        this.interactCooldowns = new HashMap<>();
        this.entityOptions = new HashSet<>();
        this.name = name;
        this.location = location;
        this.type = type;
        saveData();
        setVisible(true);
    }

    private void loadData() {
        if (this.name == null) return;
        FileConfiguration cfg = Main.getInstance().getFileManager().getFakeEntityFile().getConfig();

        if (cfg.get(this.name) == null) return;
        this.type = FakeEntityType.getTypeByName(cfg.getString(this.name + ".Type"));
        this.location = Util.stringToLocation(cfg.getString(this.name + ".Location"));
        for (String tag : cfg.getStringList(this.name + ".Nametag"))
            this.nametag.add(ChatColor.translateAlternateColorCodes('&', tag));
        if (cfg.get(this.name + ".Options") != null)
            for (String optionStr : cfg.getStringList(this.name + ".Options")) {
                FakeEntityOptionImpl option = FakeEntityOptionImpl.deserialize(this, optionStr);
                if (option != null) {
                    this.entityOptions.add(option);
                }
            }
        setVisible(true);
    }

    public void saveData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getFakeEntityFile().getConfig();
        if (cfg.get(this.name) != null)
            cfg.set(this.name, null);
        cfg.set(this.name + ".Type", this.type.name());
        cfg.set(this.name + ".Location", Util.locationToString(this.location));
        cfg.set(this.name + ".Nametag", this.nametag);
        if (!this.entityOptions.isEmpty()) {
            List<String> serializedOptions = new ArrayList<>();
            for (FakeEntityOptionImpl option : this.entityOptions)
                serializedOptions.add(FakeEntityOptionImpl.serialize(option));
            cfg.set(this.name + ".Options", serializedOptions);
        }
        Main.getInstance().getFileManager().getFakeEntityFile().saveConfig();
    }

    public void deleteData() {
        FileConfiguration cfg = Main.getInstance().getFileManager().getFakeEntityFile().getConfig();
        if (cfg.get(this.name) != null)
            cfg.set(this.name, null);
        Main.getInstance().getFileManager().getFakeEntityFile().saveConfig();
    }

    public void setNametag(List<String> nametag) {
        this.nametag = nametag;
        refreshHologram();
        saveData();
    }

    public void setLocation(Location location) {
        this.location = location;
        setVisible(false);
        setVisible(true);
    }

    public void refreshHologram() {
        if (this.hologram != null) despawnHologram();
        spawnHologram();
    }

    public void spawnHologram() {
        if (this.hologram != null) return;
        double currentY = this.type.getNameTagHeight();

        for (int i = 0; i < this.nametag.size(); i++) {
            if (i != 0)
                currentY += 0.3D;
        }

        this.hologram = HologramsAPI.createHologram(Main.getInstance(), this.location.clone().add(0, currentY, 0));

        if (this.nametag.isEmpty()) {
            this.hologram.appendTextLine(this.name);
        } else {
            for (String names : this.nametag)
                this.hologram.appendTextLine(names);
        }
    }

    public void despawnHologram() {
        if (this.hologram == null) return;
        hologram.delete();
        this.hologram = null;
    }

    public void setVisible(boolean visible) {
        if (this.visible == visible) return;
        this.visible = visible;
        if (visible) {
            this.fakeMob = FakeMobsPlugin.spawnMob(this.location, this.type.getType());
            this.fakeMob.setPlayerLook(true);
            spawnHologram();
            if (!this.entityOptions.isEmpty()) {
                for (FakeEntityOptionImpl option : this.entityOptions) {
                    if (option.getExecutingState() != FakeEntityOptionBase.ExecutingState.DESIGN) continue;
                    option.executeOnSpawn();
                }
            }
        } else {
            for (FakeEntityOptionImpl option : this.entityOptions)
                option.executeOnDestroy();
            FakeMobsPlugin.removeMob(this.fakeMob.getId());
            despawnHologram();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public FakeEntityOptionImpl findEntityOption(FakeEntityOptionBase option) {
        for (FakeEntityOptionImpl options : this.entityOptions) {
            if (options.getCategory().equalsIgnoreCase(option.getCategory()) && options.getOptionName().equalsIgnoreCase(option.getOptionName()))
                return options;
        }
        return null;
    }

    public boolean hasEntityOption(FakeEntityOptionBase option) {
        return (findEntityOption(option) != null);
    }

    public void addEntityOption(FakeEntityOptionImpl option) {
        if (hasEntityOption(option))
            return;
        this.entityOptions.add(option);
        if (option.getExecutingState() == FakeEntityOptionBase.ExecutingState.DESIGN) {
            setVisible(false);
            setVisible(true);
        }
        saveData();
    }

    public void removeEntityOption(FakeEntityOptionBase option) {
        FakeEntityOptionImpl optionImpl = findEntityOption(option);
        if (optionImpl == null)
            return;
        optionImpl.executeOnDestroy();
        this.entityOptions.remove(optionImpl);
        if (option.getExecutingState() == FakeEntityOptionBase.ExecutingState.DESIGN) {
            setVisible(false);
            setVisible(true);
        }
        saveData();
    }

    public String getName() {
        return name;
    }

    public List<String> getNametag() {
        return nametag;
    }

    public Map<Player, Long> getInteractCooldowns() {
        return interactCooldowns;
    }

    public Set<FakeEntityOptionImpl> getEntityOptions() {
        return entityOptions;
    }

    public FakeMob getFakeMob() {
        return fakeMob;
    }
}
