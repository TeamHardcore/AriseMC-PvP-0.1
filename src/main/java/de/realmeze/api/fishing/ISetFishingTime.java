package de.realmeze.api.fishing;

import org.bukkit.entity.Fish;
import org.bukkit.entity.FishHook;

public interface ISetFishingTime {
    IFishingTime getFishingTime();
    void setCatchTime(Fish fishingHook);
}
