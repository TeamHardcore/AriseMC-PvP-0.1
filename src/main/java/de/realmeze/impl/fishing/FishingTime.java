package de.realmeze.impl.fishing;

import de.realmeze.api.fishing.IFishingTime;
import net.minecraft.server.v1_8_R3.MathHelper;

import java.util.Random;

public class FishingTime implements IFishingTime {

    private int minTicks;
    private int maxTicks;

    public FishingTime(int minTicks, int maxTicks) {
        this.minTicks = minTicks;
        this.maxTicks = maxTicks;
    }
    public FishingTime(int[] timings) {
        this.minTicks = timings[0];
        this.maxTicks = timings[1];
    }

    @Override
    public int getMinTicks() {
        return this.minTicks;
    }

    @Override
    public int getMaxTicks() {
        return this.maxTicks;
    }

    @Override
    public int getCatchTime() {
        int randomCatchTime = MathHelper.nextInt(new Random(), getMinTicks(), getMaxTicks());
        return randomCatchTime;
    }
}
