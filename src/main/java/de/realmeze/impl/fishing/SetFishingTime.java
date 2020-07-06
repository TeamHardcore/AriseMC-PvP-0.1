package de.realmeze.impl.fishing;

import com.boydti.fawe.util.ReflectionUtils;
import de.realmeze.api.fishing.IFishingTime;
import de.realmeze.api.fishing.ISetFishingTime;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Fish;

public class SetFishingTime implements ISetFishingTime {

    private IFishingTime fishingTime;

    public SetFishingTime(IFishingTime fishingTime) {
        this.fishingTime = fishingTime;
    }

    @Override
    public IFishingTime getFishingTime() {
        return fishingTime;
    }

    @Override
    public void setCatchTime(Fish fishingHook) {
        net.minecraft.server.v1_8_R3.EntityFishingHook hook = (EntityFishingHook) ((CraftEntity) fishingHook).getHandle();
        ReflectionUtils.setField("aw", hook, getFishingTime().getCatchTime());
    }
}
