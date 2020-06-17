/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.crates.utils;

import de.teamhardcore.pvp.model.gambling.crates.content.ContentPiece;
import de.teamhardcore.pvp.model.gambling.crates.content.ContentValue;

import java.util.Comparator;

public class ContentSortingComparator implements Comparator<ContentPiece> {

    public static final ContentSortingComparator $ = new ContentSortingComparator();

    @Override
    public int compare(ContentPiece o1, ContentPiece o2) {
        ContentValue v1 = o1.getContentValue();
        ContentValue v2 = o2.getContentValue();

        if (v1.getValue() > v2.getValue())
            return 1;
        if (v1.getValue() < v2.getValue())
            return -1;
        if (o1.getChanceWeight() > o2.getChanceWeight())
            return -1;
        if (o1.getChanceWeight() < o1.getChanceWeight())
            return 1;

        return v1.getName().compareTo(v2.getName());
    }
}
