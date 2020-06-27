package de.howaner.FakeMobs.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import de.howaner.FakeMobs.FakeMobsPlugin;
import de.howaner.FakeMobs.merchant.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class FakeMob {
    private final int id;
    private final UUID uniqueId;
    private String name = null;
    private Location loc;
    private EntityType type;
    private WrappedDataWatcher dataWatcher = null;
    private final List<Player> loadedPlayers = new ArrayList<Player>();

    private boolean sitting = false;
    private boolean invisibility = false;
    private boolean playerLook = false;
    private MobInventory inventory = new MobInventory();
    private MobShop shop = null;
    private Multimap<String, WrappedSignedProperty> playerSkin;

    public FakeMob(int id, Location loc, EntityType type) {
        this.id = id;
        this.loc = loc;
        this.type = type;
        this.uniqueId = UUID.nameUUIDFromBytes(("FakeMob-" + id).getBytes(Charsets.UTF_8));
        this.dataWatcher = DataWatchCreator.createDefaultWatcher(this);
    }

    public UUID getUniqueID() {
        return this.uniqueId;
    }

    public MobInventory getInventory() {
        return this.inventory;
    }

    public void setInventory(MobInventory inv) {
        this.inventory = inv;
        if (this.inventory == null)
            this.inventory = new MobInventory();
    }

    public boolean haveShop() {
        return (this.shop != null);
    }

    public MobShop getShop() {
        return this.shop;
    }

    public void setShop(MobShop shop) {
        this.shop = shop;
    }

    public int getEntityId() {
        return 2300 + this.id;
    }

    public Multimap<String, WrappedSignedProperty> getPlayerSkin() {
        return this.playerSkin;
    }

    public void setPlayerSkin(Multimap<String, WrappedSignedProperty> skin) {
        this.playerSkin = skin;
    }

    public void setPlayerSkin(Player player) {
        this.playerSkin = WrappedGameProfile.fromPlayer(player).getProperties();
    }

    public List<Player> getNearbyPlayers() {
        return this.getNearbyPlayers(3D);
    }

    public List<Player> getNearbyPlayers(double radius) {
        List<Player> players = new ArrayList<Player>();

        for (Player player : this.getWorld().getPlayers()) {
            if (this.loc.distance(player.getLocation()) <= radius) {
                players.add(player);
            }
        }

        return players;
    }

    public void updateInventory() {
        for (Player p : this.loadedPlayers)
            this.sendInventoryPacket(p);
    }

    public boolean isPlayerLoaded(Player player) {
        return this.loadedPlayers.contains(player);
    }

    public void loadPlayer(Player player) {
        if (this.isPlayerLoaded(player)) return;

        this.loadedPlayers.add(player);
        this.sendSpawnPacket(player);
    }

    public void unloadPlayer(Player player) {
        if (!this.isPlayerLoaded(player)) return;

        this.loadedPlayers.remove(player);
        this.sendDestroyPacket(player);
    }

    public boolean isInRange(Player player) {
        return this.loc.getWorld() == player.getLocation().getWorld() && (this.loc.distance(player.getLocation()) <= 48D);
    }

    public int getId() {
        return this.id;
    }

    public String getCustomName() {
        return this.name;
    }

    public Location getLocation() {
        return this.loc;
    }

    public World getWorld() {
        return this.loc.getWorld();
    }

    public EntityType getType() {
        return this.type;
    }

    public boolean isSitting() {
        return this.sitting;
    }

    public boolean isInvisibility() {
        return this.invisibility;
    }

    public boolean isPlayerLook() {
        return this.playerLook;
    }

    public void setLocation(Location loc) {
        this.loc = loc;
    }

    public void setCustomName(String name) {
        if (name != null && name.length() > 32) name = name.substring(0, 32);
        this.name = name;

        if (this.name != null && this.name.isEmpty()) {
            this.name = null;
        }

        if (this.type == EntityType.PLAYER) {
            // No need to change the watcher
            return;
        }

        if (this.name == null) {
            this.dataWatcher.setObject(11, (byte) 0);
            this.dataWatcher.setObject(10, "");
            this.dataWatcher.setObject(3, (byte) 0);
            this.dataWatcher.setObject(2, "");
        } else {
            this.dataWatcher.setObject(11, (byte) 1);
            this.dataWatcher.setObject(10, this.name);
            this.dataWatcher.setObject(3, (byte) 1);
            this.dataWatcher.setObject(2, this.name);
        }
    }

    public void setSitting(boolean sitting) {
        if (this.type != EntityType.OCELOT && this.type != EntityType.WOLF && this.type != EntityType.PLAYER) return;
        if (this.sitting == sitting) return;
        this.sitting = sitting;

        if (this.getType() == EntityType.PLAYER) {
            byte current = this.dataWatcher.getByte(0);
            if (sitting)
                this.dataWatcher.setObject(0, (byte) (current | 1 << 1));
            else
                this.dataWatcher.setObject(0, (byte) (current & (1 << 1 ^ 0xFFFFFFFF)));
        } else if (sitting) {
            this.dataWatcher.setObject(16, (byte) 0x1);
        } else {
            this.dataWatcher.setObject(16, (byte) 0x0);
        }
    }

    public void setInvisibility(boolean invisibility) {
        if (this.invisibility == invisibility) return;
        this.invisibility = invisibility;

        byte current = this.dataWatcher.getByte(0);
        if (invisibility) {
            this.dataWatcher.setObject(0, (byte) (current | 1 << 5));
        } else {
            this.dataWatcher.setObject(0, (byte) (current & (1 << 5 ^ 0xFFFFFFFF)));
        }
    }

    public void setPlayerLook(boolean look) {
        if (this.playerLook == look) return;

        if (!look) {
            for (Player player : this.loadedPlayers)
                this.sendLookPacket(player, this.getLocation().getYaw());
        }

        this.playerLook = look;
    }

    public void teleport(Location loc) {
        this.loc = loc;

        for (Player player : this.loadedPlayers)
            this.sendPositionPacket(player);
    }

    public void setType(EntityType type) {
        if (type == null || this.type == type || !type.isAlive()) return;

        for (Player p : this.loadedPlayers)
            this.sendDestroyPacket(p);

        this.type = type;
        this.dataWatcher = DataWatchCreator.createDefaultWatcher(this);

        for (Player p : this.loadedPlayers)
            this.sendSpawnPacket(p);
    }

    public void updateMetadata() {
        for (Player player : this.loadedPlayers) {
            this.sendMetaPacket(player);
        }
    }

    public void updateCustomName() {
        for (Player player : this.loadedPlayers) {
            if (this.getType() == EntityType.PLAYER) {
                this.sendDestroyPacket(player);
            } else
                this.sendMetaPacket(player);
        }

        // Need a 5 tick delay because mojang did mistakes in 1.8 ...
        if (this.getType() == EntityType.PLAYER) {
            Bukkit.getScheduler().runTaskLater(FakeMobsPlugin.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (Player player : FakeMob.this.loadedPlayers) {
                        FakeMob.this.sendSpawnPacket(player);
                    }
                }
            }, 5L);
        }
    }


    //////////////// -- PACKETS -- ////////////////

    public void sendSpawnPacket(Player player) {
        if (this.getType() == EntityType.PLAYER)
            this.sendPlayerSpawnPacket(player);
        else
            this.sendEntitySpawnPacket(player);
    }

    public void sendPlayerSpawnPacket(final Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);

        packet.getIntegers().write(0, this.getEntityId());
        packet.getIntegers().write(1, (int) Math.floor(this.loc.getX() * 32D)); //X
        packet.getIntegers().write(2, (int) Math.floor(this.loc.getY() * 32D)); //Y
        packet.getIntegers().write(3, (int) Math.floor(this.loc.getZ() * 32D)); //Z
        packet.getIntegers().write(4, 0); //Item in Hand Slot

        packet.getBytes().write(0, (byte) (int) (this.loc.getYaw() * 256.0F / 360.0F)); //Yaw
        packet.getBytes().write(1, (byte) (int) (this.loc.getPitch() * 256.0F / 360.0F)); //Pitch

        final WrappedGameProfile profile = new WrappedGameProfile(this.uniqueId, (this.getCustomName() == null) ? "No Name" : this.getCustomName());
        if (this.playerSkin != null) {
            profile.getProperties().putAll(this.playerSkin);
        }

        final boolean isSpigot18 = (packet.getGameProfiles().size() == 0);
        if (isSpigot18)
            packet.getSpecificModifier(UUID.class).write(0, profile.getUUID());
        else
            packet.getGameProfiles().write(0, profile);
        packet.getDataWatcherModifier().write(0, this.dataWatcher);

        int protocolVersion = ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
        if (protocolVersion >= 47 || protocolVersion == Integer.MIN_VALUE) {
            PacketContainer infoPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);

            if (isSpigot18) {
                Object playerInfo = ReflectionUtils.createPlayerInfoData(profile.getHandle(), GameMode.SURVIVAL, 0, " ");
                infoPacket.getSpecificModifier(ReflectionUtils.PlayerInfoAction.getNMSClass()).write(0, ReflectionUtils.PlayerInfoAction.ADD_PLAYER);
                infoPacket.getSpecificModifier(List.class).write(0, Collections.singletonList(playerInfo));
            } else {
                infoPacket.getIntegers().write(0, 0); //Packet: Create
                infoPacket.getIntegers().write(1, 0); //Gamemode: Survival
                infoPacket.getIntegers().write(2, 0); //Ping: 0

                infoPacket.getGameProfiles().write(0, profile);
            }

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, infoPacket);
            } catch (Exception e) {
                FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send spawn packet to {0} from mob #{1}", new Object[]{player.getName(), getId()});
                e.printStackTrace();
            }

            Bukkit.getScheduler().runTaskLater(FakeMobsPlugin.getInstance(), () -> {
                if (!FakeMob.this.isPlayerLoaded(player)) {
                    return;
                }
                PacketContainer infoPacket1 = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);

                if (isSpigot18) {
                    Object playerInfo = ReflectionUtils.createPlayerInfoData(profile.getHandle(), GameMode.SURVIVAL, 0, "");
                    infoPacket1.getSpecificModifier(ReflectionUtils.PlayerInfoAction.getNMSClass()).write(0, ReflectionUtils.PlayerInfoAction.REMOVE_PLAYER);
                    infoPacket1.getSpecificModifier(List.class).write(0, Arrays.asList(new Object[]{playerInfo}));
                } else {
                    infoPacket1.getIntegers().write(0, 4); //Packet: Remove
                    infoPacket1.getIntegers().write(1, 0); //Gamemode: Survival
                    infoPacket1.getIntegers().write(2, 0); //Ping: 0

                    infoPacket1.getGameProfiles().write(0, profile);
                }

                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, infoPacket1);
                } catch (Exception e) {
                    FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send spawn packet to {0} from mob #{1}", new Object[]{player.getName(), getId()});
                    e.printStackTrace();
                }
            }, (this.playerSkin == null ? 5L : 40L));
        }

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send spawn packet to {0} from mob #{1}", new Object[]{player.getName(), getId()});
            e.printStackTrace();
            return;
        }

        this.sendLookPacket(player, this.loc.getYaw());
        this.sendInventoryPacket(player);
    }

    public void sendEntitySpawnPacket(Player player) {
        if (this.type.isAlive()) {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);

            packet.getIntegers().write(0, this.getEntityId());
            packet.getIntegers().write(1, (int) this.type.getTypeId()); //Id
            packet.getIntegers().write(2, (int) Math.floor(this.loc.getX() * 32D)); //X
            packet.getIntegers().write(3, (int) Math.floor((this.loc.getY() + 0.001D) * 32D)); //Y
            packet.getIntegers().write(4, (int) Math.floor(this.loc.getZ() * 32D)); //Z

            packet.getBytes().write(0, (byte) (int) (this.loc.getYaw() * 256.0F / 360.0F)); //Yaw
            packet.getBytes().write(1, (byte) (int) (this.loc.getPitch() * 256.0F / 360.0F)); //Pitch
            packet.getBytes().write(2, (byte) (int) (this.loc.getYaw() * 256.0F / 360.0F)); //Head

            packet.getDataWatcherModifier().write(0, this.dataWatcher);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (Exception e) {
                FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send spawn packet to {0} from mob #{1}", new Object[]{player.getName(), getId()});
                e.printStackTrace();
                return;
            }

            this.sendInventoryPacket(player);
        } else {
            PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY);

            packet.getIntegers().write(0, getEntityId());
            packet.getIntegers().write(1, (int) Math.floor(this.loc.getX() * 32.0D));
            packet.getIntegers().write(2, (int) Math.floor((this.loc.getY() + 0.001D) * 32.0D));
            packet.getIntegers().write(3, (int) Math.floor(this.loc.getZ() * 32.0D));
            packet.getIntegers().write(4, 0);
            packet.getIntegers().write(5, 0);
            packet.getIntegers().write(6, 0);
            packet.getIntegers().write(7, (int) (this.loc.getYaw() * 256.0D / 360.0D));
            packet.getIntegers().write(8, (int) (this.loc.getPitch() * 256.0D / 360.0D));
            packet.getIntegers().write(9, Util.getIdForEntity(this.type));
            packet.getIntegers().write(10, 0);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (Exception e) {
                FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send spawn packet to {0} from mob #{1}", new Object[]{player.getName(), Integer.valueOf(getId())});
                e.printStackTrace();
                return;
            }
        }
    }

    public void sendMetaPacket(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);

        packet.getIntegers().write(0, this.getEntityId());
        packet.getWatchableCollectionModifier().write(0, this.dataWatcher.getWatchableObjects());

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send meta packet to {0} from mob #{1}", new Object[]{player.getName(), getId()});
            e.printStackTrace();
        }
    }

    public void sendInventoryPacket(Player player) {
        List<PacketContainer> packets = this.inventory.createPackets(this.getEntityId());
        if (packets.isEmpty()) return;

        try {
            for (PacketContainer packet : packets)
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send inventory packet to {0} from mob #{1}", new Object[]{player.getName(), Integer.valueOf(getId())});
            e.printStackTrace();
        }
    }

    public void sendLookPacket(Player player, Location point) {
        double xDiff = point.getX() - this.loc.getX();
        //double yDiff = point.getY() - this.loc.getY();
        double zDiff = point.getZ() - this.loc.getZ();
        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        //double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180.0D / 3.141592653589793D;
        //double newPitch = Math.acos(yDiff / DistanceY) * 180.0D / 3.141592653589793D - 90.0D;
        if (zDiff < 0.0D)
            newYaw += Math.abs(180.0D - newYaw) * 2.0D;
        double yaw = ((float) newYaw - 98.0D);

        this.sendLookPacket(player, yaw);
    }

    public void sendLookPacket(Player player, double yaw) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);

        packet.getIntegers().write(0, this.getEntityId());
        packet.getBytes().write(0, (byte) (int) (yaw * 256.0F / 360.0F));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send look packet to {0} from mob #{1}", new Object[]{player.getName(), Integer.valueOf(getId())});
            e.printStackTrace();
        }
    }

    public void sendPositionPacket(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_TELEPORT);

        packet.getIntegers().write(0, this.getEntityId()); //Id
        packet.getIntegers().write(1, (int) Math.floor(this.loc.getX() * 32)); //X
        packet.getIntegers().write(2, (int) Math.floor((this.loc.getY() + 0.001D) * 32)); //Y
        packet.getIntegers().write(3, (int) Math.floor(this.loc.getZ() * 32)); //Z

        packet.getBytes().write(0, (byte) (int) (this.loc.getYaw() * 256.0F / 360.0F)); //Yaw
        packet.getBytes().write(1, (byte) (int) (this.loc.getPitch() * 256.0F / 360.0F)); //Pitch

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send position packet to {0} from mob #{1}", new Object[]{player.getName(), Integer.valueOf(getId())});
            e.printStackTrace();
        }
    }

    public void sendDestroyPacket(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntegerArrays().write(0, new int[]{this.getEntityId()});

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send destroy packet to {0} from mob #{1}", new Object[]{player.getName(), Integer.valueOf(getId())});
            e.printStackTrace();
            return;
        }

        if (ProtocolLibrary.getProtocolManager().getProtocolVersion(player) >= 47 && this.getType() == EntityType.PLAYER) {
            WrappedGameProfile profile = new WrappedGameProfile(this.uniqueId, (this.getCustomName() == null) ? "No Name" : this.getCustomName());
            PacketContainer infoPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);

            boolean spigot18 = (infoPacket.getIntegers().size() == 0);
            if (spigot18) {
                Object playerInfo = ReflectionUtils.createPlayerInfoData(profile.getHandle(), GameMode.SURVIVAL, 0, "");
                infoPacket.getSpecificModifier(ReflectionUtils.PlayerInfoAction.getNMSClass()).write(0, ReflectionUtils.PlayerInfoAction.REMOVE_PLAYER);
                infoPacket.getSpecificModifier(List.class).write(0, Arrays.asList(new Object[]{playerInfo}));
            } else {
                infoPacket.getIntegers().write(0, 4);
                infoPacket.getIntegers().write(1, 0);
                infoPacket.getIntegers().write(2, 0);

                infoPacket.getGameProfiles().write(0, profile);
            }

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, infoPacket);
            } catch (Exception e) {
                FakeMobsPlugin.getInstance().getLogger().log(Level.SEVERE, "Can''t send player info destroy packet to {0} from mob #{1}", new Object[]{player.getName(), getId()});
                e.printStackTrace();
            }
        }
    }

}
