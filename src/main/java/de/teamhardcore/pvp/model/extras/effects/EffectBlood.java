/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.extras.effects;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.extras.EnumKilleffect;
import de.teamhardcore.pvp.utils.SimpleUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EffectBlood extends AbstractKillEffect {

    private final static Random RANDOM = new Random();
    private static final ItemStack[] BLOOD_ITEMS = new ItemStack[]{new ItemStack(Material.REDSTONE),
            new ItemStack(Material.INK_SACK, 1, (short) 1), new ItemStack(Material.NETHER_STALK)};

    private final Location location;

    private final Set<Entity> entities = new HashSet<>();
    private BukkitTask removeTask, checkGroundTask;

    public EffectBlood(Player player) {
        super(player);
        this.location = player.getEyeLocation();
    }

    @Override
    public void playEffect() {
        startCheckGroundTask();
        for (int count = 0; count < 15; count++) {
            double randomZ, randomX;

            do {
                randomX = RANDOM.nextDouble() * 1.0D - 0.5D;
            } while (randomX <= 0.2D && randomX >= -0.2D);
            do {
                randomZ = RANDOM.nextDouble() * 1.0D - 0.5D;
            } while (randomZ <= 0.2D && randomZ >= -0.2D);

            Location randomLocation = new Location(this.location.getWorld(), this.location.getX() + randomX, this.location.getY() + RANDOM.nextDouble() * 0.5D - 0.25D, this.location.getZ() + randomZ);
            ItemStack drop = BLOOD_ITEMS[RANDOM.nextInt(BLOOD_ITEMS.length)].clone();
            ItemMeta meta = drop.getItemMeta();
            meta.setLore(Collections.singletonList(SimpleUID.generate(10).toString()));
            drop.setItemMeta(meta);

            Item item = this.location.getWorld().dropItemNaturally(randomLocation, drop);
            Vector vectorX = item.getLocation().toVector().subtract(this.location.toVector()).normalize().setY(0).multiply(RANDOM.nextDouble() * 0.3D);
            Vector vectorZ = item.getLocation().toVector().subtract(this.location.toVector()).normalize().setY(0).multiply(RANDOM.nextDouble() * 0.3D);
            item.setVelocity(new Vector(vectorX.getX(), RANDOM.nextDouble() * 0.3D, vectorZ.getZ()));
            item.setMetadata("Killeffect", new FixedMetadataValue(Main.getInstance(), this));
            this.entities.add(item);
        }
    }

    @Override
    public void stopEffect() {
        if (this.checkGroundTask != null) {
            this.checkGroundTask.cancel();
            this.checkGroundTask = null;
        }

        if (this.removeTask != null) {
            this.removeTask.cancel();
            this.removeTask = null;
        }

        this.entities.forEach(Entity::remove);
    }

    @Override
    public EnumKilleffect getEffectType() {
        return EnumKilleffect.BLOOD;
    }

    private void makeItemSolid(Item item) {
        this.entities.remove(item);
        if (item.isDead()) return;
        ItemStack itemStack = item.getItemStack();
        Location location = item.getLocation();
        item.remove();

        Location tmp = location.clone();
        tmp.setY(0.0D);
        Location teleport = location.clone().add(0.0D, -0.72D, 0.0D);

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(tmp, EntityType.ARMOR_STAND);
        armorStand.setItemInHand(itemStack);
        armorStand.setRightArmPose(new EulerAngle(0.0D, RANDOM.nextInt(360), 0.0D));
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.teleport(teleport);
        armorStand.setMetadata("Killeffect", new FixedMetadataValue(Main.getInstance(), this));
        this.entities.add(armorStand);
    }

    private void startRemoveTask() {
        this.removeTask = new BukkitRunnable() {
            @Override
            public void run() {
                Main.getInstance().getManager().stopKillEffect(EffectBlood.this);
            }
        }.runTaskLater(Main.getInstance(), 100L);
    }

    private void startCheckGroundTask() {
        this.checkGroundTask = new BukkitRunnable() {
            private final Set<Item> toRemove = new HashSet<>();

            @Override
            public void run() {

                int count = 0;

                for (Entity entity : EffectBlood.this.entities) {
                    if (!(entity instanceof Item)) continue;
                    if (entity.isOnGround() || entity.isDead()) {
                        this.toRemove.add((Item) entity);
                        continue;
                    }
                    count++;
                }

                for (Item item : this.toRemove) {
                    EffectBlood.this.makeItemSolid(item);
                }
                if (!this.toRemove.isEmpty())
                    this.toRemove.clear();
                if (count == 0) {
                    cancel();
                    EffectBlood.this.checkGroundTask = null;
                    EffectBlood.this.startRemoveTask();
                }

            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }
}
