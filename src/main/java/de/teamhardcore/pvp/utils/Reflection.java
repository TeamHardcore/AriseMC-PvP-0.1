/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.utils;

import de.teamhardcore.pvp.Main;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection {
    private static Class<?> craftPlayerClass = getOBCClass("entity.CraftPlayer");
    private static Class<?> entityPlayerClass = getNMSClass("EntityPlayer");
    private static Class<?> playerConnectionClass = getNMSClass("PlayerConnection");
    private static Class<?> networkManagerClass = getNMSClass("NetworkManager");


    public static String getVersion() {
        String pkg = Main.getInstance().getServer().getClass().getPackage().getName();
        pkg = pkg.substring(pkg.lastIndexOf(".") + 1);
        return pkg;
    }

    public static int getMinecraftVersion(Player p) {
        Object playerConnection = getFromField(getField(entityPlayerClass, "playerConnection"), invoke(p, getMethod(craftPlayerClass, "getHandle", new Class[0]), new Object[0]));
        Object networkManager = getFromField(getField(playerConnectionClass, "networkManager"), playerConnection);
        return (Integer) invoke(networkManager, getMethod(networkManagerClass, "getVersion", new Class[0]), new Object[0]);
    }

    public static Class<?> getNMSClass(String name) {
        String pkg = "net.minecraft.server." + getVersion() + "." + name;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(pkg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nmsClass;
    }

    public static Class<?> getOBCClass(String name) {
        String pkg = "org.bukkit.craftbukkit." + getVersion() + "." + name;
        Class<?> obcClass = null;
        try {
            obcClass = Class.forName(pkg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obcClass;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... params) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(name, params);
            method.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return method;
    }

    public static Field getField(Class<?> clazz, String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return field;
    }

    public static Object invoke(Object obj, Method method, Object... params) {
        Object invoked = null;
        try {
            invoked = method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invoked;
    }

    public static Object getFromField(Field field, Object from) {
        Object got = null;
        try {
            got = field.get(from);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return got;
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
        Constructor<?> constructor = null;
        try {
            constructor = clazz.getConstructor(params);
            constructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return constructor;
    }

    public static Object newInstance(Constructor<?> constructor, Object... params) {
        Object obj = null;
        try {
            obj = constructor.newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void setForField(Field field, Object forWhom, Object what) {
        try {
            field.set(forWhom, what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
