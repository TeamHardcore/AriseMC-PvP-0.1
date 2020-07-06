package de.realmeze.impl.fishing;

import de.realmeze.api.fishing.ICustomRod;
import de.realmeze.api.fishing.IFishingTime;
import org.bukkit.inventory.ItemStack;

// for now: ARISEROD NAME MIN MAX
public class CustomRod implements ICustomRod {

    private ItemStack rod;

    public CustomRod(ItemStack rod) {
        this.rod = rod;
    }

    @Override
    public boolean isCustom() {
        return rod.getItemMeta().getDisplayName().contains("ARISEROD");
    }

    @Override
    public ItemStack getRod() {
        return this.rod;
    }

    @Override
    public int[] parseTime() {
        int minTime;
        int maxTime;
        if(isCustom()){
            String toParse = rod.getItemMeta().getDisplayName();
            String[] values = toParse.split(" ");
            minTime = Integer.parseInt(values[2]);
            maxTime = Integer.parseInt(values[3]);
            return new int[]{minTime, maxTime};
        } else {
            return new int[]{100, 500};
        }
    }

    @Override
    public IFishingTime getTime() {
        return new FishingTime(parseTime());
    }
}
