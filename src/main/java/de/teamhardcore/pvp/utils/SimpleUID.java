/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.utils;

import java.security.SecureRandom;

public class SimpleUID {
    private static final SecureRandom generator = new SecureRandom();

    private static final String charPattern = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789";

    private char[] uidChars;
    private String uidString;

    public SimpleUID(int length) {
        generate(length);
    }


    public SimpleUID(char[] uidChars) {
        this.uidChars = uidChars;
        this.uidString = new String(uidChars);
    }


    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleUID))
            return false;
        SimpleUID objUid = (SimpleUID) obj;
        return objUid.toString().equals(this.uidString);
    }


    public String toString() {
        return this.uidString;
    }


    public static SimpleUID generate(int length) {
        char[] chars = new char[length];

        for (int i = 0; i < length; i++)
            chars[i] = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789".charAt(generator.nextInt("aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789".length()));

        return new SimpleUID(chars);
    }
}