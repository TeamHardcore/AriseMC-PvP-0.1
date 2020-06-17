/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.utils;

import de.teamhardcore.pvp.model.gambling.crates.CrateValue;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;

import java.util.Comparator;

public class CrateSortingComparator implements Comparator<BaseCrate> {

    public static final CrateSortingComparator $ = new CrateSortingComparator();

    @Override
    public int compare(BaseCrate o1, BaseCrate o2) {
        CrateValue v1 = o1.getAddon().getValue();
        CrateValue v2 = o2.getAddon().getValue();

        if (v1.getRarity() > v2.getRarity())
            return -1;

        if (v1.getRarity() < v2.getRarity())
            return 1;
        return o1.getAddon().getName().compareTo(o2.getAddon().getName());
    }

}
