/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.listeners.custom;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.utils.MobUtil;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MobStackEvents implements Listener {

    private final Main plugin;

    public MobStackEvents(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();

        if (!MobUtil.isStackable(entity)) {
            return;
        }
        MobUtil.tryToStack(entity);
    }


    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if (entity.hasMetadata("stacksize")) {
            int stackSize = MobUtil.getStackSize(entity);
            List<ItemStack> newDrops = MobUtil.dropDeathLoot(entity, stackSize);
            if (newDrops != null) {
                e.getDrops().clear();
                e.getDrops().addAll(newDrops);
                e.setDroppedExp(MobUtil.getExpReward(entity, stackSize));
                if (stackSize > 250 && entity.getKiller() != null) {
                    Player killer = entity.getKiller();
                    killer.sendMessage(StringDefaults.PREFIX + "§c§lHinweis: §6Es wurde nur Loot im Wert von §e250 Monstern §6gedroppt.");
                }
            }
        }
    }


    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (!(e.getEntity() instanceof LivingEntity) || !e.getEntity().hasMetadata("stacksize")) {
            return;
        }
        LivingEntity entity = (LivingEntity) e.getEntity();
        int newStackSize = MobUtil.getStackSize(entity) - 1;

        if (newStackSize > 0) {
            LivingEntity newEntity = (LivingEntity) entity.getLocation().getWorld().spawnEntity(entity.getLocation(), entity.getType());
            MobUtil.renameMobStack(newEntity, newStackSize);
        }
    }


    @EventHandler
    public void onTame(EntityTameEvent e) {
        if (!e.getEntity().hasMetadata("stacksize")) {
            return;
        }
        LivingEntity entity = e.getEntity();
        int newSize = MobUtil.getStackSize(entity) - 1;

        if (newSize > 0) {
            MobUtil.removeFromStack(entity);
        }
    }


    @EventHandler
    public void onShear(PlayerShearEntityEvent e) {
        if (!(e.getEntity() instanceof Sheep) || !e.getEntity().hasMetadata("stacksize")) {
            return;
        }
        Sheep entity = (Sheep) e.getEntity();
        int stackSize = MobUtil.getStackSize(entity);

        if (stackSize > 1) {
            MobUtil.removeFromStack(entity);
        } else {
            MobUtil.renameMobStack(entity, 1);
        }
    }


    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof LivingEntity) || !e.getRightClicked().hasMetadata("stacksize")) {
            return;
        }
        Player p = e.getPlayer();
        LivingEntity entity = (LivingEntity) e.getRightClicked();
        ItemStack hand = p.getItemInHand();

        if (hand != null && hand.getType() == Material.NAME_TAG && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
            int stackSize = MobUtil.getStackSize(entity);
            if (stackSize > 1) {
                MobUtil.removeFromStack(entity);
            } else {
                MobUtil.renameMobStack(entity, 1);
            }
        }
    }


    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();
        Entity[] entities = chunk.getEntities();

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && MobUtil.isStackable((LivingEntity) entity)) {

                LivingEntity living = (LivingEntity) entity;
                int stackSize = MobUtil.parseStackSizeFromName(living);
                if (stackSize > 1)
                    MobUtil.renameMobStack(living, stackSize);
            }
        }
    }

    public Main getPlugin() {
        return plugin;
    }
}
