/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.utils;

import de.teamhardcore.pvp.Main;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MobUtil {

    private static final Class<?> craftLivingEntityClass = Reflection.getOBCClass("entity.CraftLivingEntity");
    private static final Class<?> craftPlayerClass = Reflection.getOBCClass("entity.CraftPlayer");
    private static final Class<?> entityLivingClass = Reflection.getNMSClass("EntityLiving");
    private static final Class<?> enchManagerClass = Reflection.getNMSClass("EnchantmentManager");
    private static final Method getHandleLivingMethod = Reflection.getMethod(craftLivingEntityClass, "getHandle");
    private static final Method getHandlePlayerMethod = Reflection.getMethod(craftPlayerClass, "getHandle");
    private static final Method getExpRewardMethod = Reflection.getMethod(entityLivingClass, "getExpReward");
    private static final Method bonusLootMethod = Reflection.getMethod(enchManagerClass, "getBonusMonsterLootEnchantmentLevel", entityLivingClass);
    private static final Method dropDeathLootMethod = Reflection.getMethod(entityLivingClass, "dropDeathLoot", boolean.class, int.class);
    private static final Method dropEquipmentMethod = Reflection.getMethod(entityLivingClass, "dropEquipment", boolean.class, int.class);
    private static final Method getRareDropMethod = Reflection.getMethod(entityLivingClass, "getRareDrop");
    private static final Field dropsField = Reflection.getField(entityLivingClass, "drops");
    private static final Field lastDamageByPlayerTimeField = Reflection.getField(entityLivingClass, "lastDamageByPlayerTime");

    private static final Random RANDOM = new Random();

    private static final int SEARCH_TIME = 20;
    private static final int SEARCH_RADIUS = 5;

    public static boolean isStackable(LivingEntity entity) {
        return (!entity.isDead() && (!(entity instanceof Player) && (!(entity instanceof Tameable) ||
                !((Tameable) entity).isTamed())));
    }

    public static void tryToStack(LivingEntity livingEntity) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {

                for (Entity entity : livingEntity.getNearbyEntities(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS)) {
                    if (!(entity instanceof LivingEntity) || !MobUtil.isStackable((LivingEntity) entity)) continue;
                    boolean success = stackEntity((LivingEntity) entity, livingEntity);

                    if (success) {
                        cancel();
                        return;
                    }
                }

                this.count++;
                if (this.count >= SEARCH_TIME)
                    cancel();
            }
        }.runTaskTimer(Main.getInstance(), 20L, 10L);
    }

    public static boolean stackEntity(LivingEntity entity, LivingEntity newEntity) {
        if (!isStackable(entity) || !isStackable(newEntity) || !isSameEntity(entity, newEntity)) return false;
        int newSize = getStackSize(entity) + getStackSize(newEntity);
        renameMobStack(entity, newSize);
        newEntity.remove();
        return true;
    }

    public static LivingEntity removeFromStack(LivingEntity stack) {
        Location location = stack.getLocation();
        EntityType type = stack.getType();
        int newSize = getStackSize(stack) - 1;
        LivingEntity newEntity = (LivingEntity) location.getWorld().spawnEntity(location, type);
        if (stack instanceof Ageable)
            ((Ageable) newEntity).setAge(((Ageable) stack).getAge());
        if (stack instanceof Colorable)
            ((Colorable) newEntity).setColor(((Colorable) stack).getColor());
        if (stack instanceof Sheep)
            ((Sheep) newEntity).setSheared(((Sheep) stack).isSheared());
        renameMobStack(newEntity, newSize);
        renameMobStack(stack, 1);
        tryToStack(newEntity);
        return newEntity;
    }


    public static boolean isSameEntity(LivingEntity entity1, LivingEntity entity2) {
        if (entity1.getType() != entity2.getType() || entity1.isDead() || entity2.isDead()) return false;
        if (entity1 instanceof Ageable) {
            if (!(entity2 instanceof Ageable))
                return false;
            Ageable age1 = (Ageable) entity1;
            Ageable age2 = (Ageable) entity2;
            if (age1.isAdult() != age2.isAdult())
                return false;
        }
        if (entity1 instanceof Colorable) {
            if (!(entity2 instanceof Colorable))
                return false;
            Colorable color1 = (Colorable) entity1;
            Colorable color2 = (Colorable) entity2;
            return color1.getColor() == color2.getColor();
        }
        return true;
    }

    public static int getStackSize(LivingEntity entity) {
        if (!entity.hasMetadata("stacksize"))
            entity.setMetadata("stacksize", (MetadataValue) new FixedMetadataValue(Main.getInstance(), 1));
        return entity.getMetadata("stacksize").get(0).asInt();
    }

    public static int parseStackSizeFromName(LivingEntity entity) {
        int parsedSize;
        if (entity.getCustomName() == null || entity.getCustomName().isEmpty())
            return 1;
        String name = entity.getCustomName();

        try {
            name = name.substring(9, name.length() - 6);
            parsedSize = Integer.parseInt(name);
        } catch (Exception e) {
            return 1;
        }
        return parsedSize;
    }

    public static void renameMobStack(LivingEntity entity, int newSize) {
        if (newSize > 1) {
            entity.setMetadata("stacksize", new FixedMetadataValue(Main.getInstance(), newSize));
            entity.setCustomName("§8◄● " + getSizeColor(newSize) + newSize + "x §8●►");
            entity.setCustomNameVisible(true);
        } else {
            if (entity.hasMetadata("stacksize"))
                entity.removeMetadata("stacksize", Main.getInstance());

            if (entity.isCustomNameVisible()) {
                entity.setCustomName("");
                entity.setCustomNameVisible(false);
            }
        }
    }

    private static String getSizeColor(int stackSize) {
        if (stackSize <= 100) return "§a§l";
        if (stackSize <= 150) return "§e§l";
        if (stackSize <= 200) return "§6§l";
        if (stackSize <= 250) return "§c§l";
        return "§4§l";
    }

    public static List<ItemStack> dropDeathLoot(LivingEntity entity, int quantity) {
        if (quantity > 250) quantity = 250;
        Player killer = entity.getKiller();
        int dropModifier = 0;
        Object entityLiving = Reflection.invoke(craftLivingEntityClass.cast(entity), getHandleLivingMethod);
        if (killer != null) {
            Object entityPlayer = Reflection.invoke(craftPlayerClass.cast(killer), getHandlePlayerMethod);
            dropModifier = (Integer) Reflection.invoke(null, bonusLootMethod, new Object[]{entityPlayer});
        }
        if (entity instanceof Ageable && !((Ageable) entity).isAdult()) return null;
        if (!Boolean.parseBoolean(entity.getLocation().getWorld().getGameRuleValue("doMobLoot"))) return null;
        Reflection.setForField(dropsField, entityLiving, new ArrayList<>());
        int lastDamageByPlayerTime = (Integer) Reflection.getFromField(lastDamageByPlayerTimeField, entityLiving);
        for (int i = 0; i < quantity; i++) {
            Reflection.invoke(entityLiving, dropDeathLootMethod, (lastDamageByPlayerTime > 0), dropModifier);
            if (lastDamageByPlayerTime > 0 && RANDOM.nextFloat() < 0.025F + dropModifier * 0.01F)
                Reflection.invoke(entityLiving, getRareDropMethod);
        }
        Reflection.invoke(entityLiving, dropEquipmentMethod, (lastDamageByPlayerTime > 0), dropModifier);
        List<ItemStack> droppedItems = (List<ItemStack>) Reflection.getFromField(dropsField, entityLiving);
        Reflection.setForField(dropsField, entityLiving, null);
        return droppedItems;
    }

    public static int getExpReward(LivingEntity entity, int quantity) {
        if (quantity > 250)
            quantity = 250;
        Object entityLiving = Reflection.invoke(craftLivingEntityClass.cast(entity), getHandleLivingMethod);
        int expReward = 0;
        for (int i = 0; i < quantity; i++)
            expReward += (Integer) Reflection.invoke(entityLiving, getExpRewardMethod, new Object[0]);
        return expReward;
    }


}
