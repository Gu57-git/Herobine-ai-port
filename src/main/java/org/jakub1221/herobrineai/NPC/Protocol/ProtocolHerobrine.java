package org.jakub1221.herobrineai.NPC.Protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class ProtocolHerobrine {
    private final ProtocolManager manager;
    private final int entityId;
    private final WrappedGameProfile profile;
    private Location currentLocation;
    private boolean spawned = false;

    public ProtocolHerobrine(int entityId, WrappedGameProfile profile, Location loc) {
        this.manager = ProtocolLibrary.getProtocolManager();
        this.entityId = entityId;
        this.profile = profile;
        this.currentLocation = loc.clone();
    }

    public void spawnAll() {
        Bukkit.getOnlinePlayers().forEach(this::spawnForPlayer);
    }

    public void spawnForPlayer(Player player) {
        if (currentLocation.getWorld() != null && player.getWorld() != currentLocation.getWorld()) return;
        try {
            Bukkit.getLogger().info("[HerobrineAI] Spawning Herobrine for " + player.getName());

            PacketContainer info = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
            info.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
            PlayerInfoData data = new PlayerInfoData(
                    profile, 1, EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromText(profile.getName()));
            info.getPlayerInfoDataLists().write(0, Collections.singletonList(data));
            manager.sendServerPacket(player, info);

            PacketContainer spawn = manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
            spawn.getIntegers().write(0, entityId);
            spawn.getUUIDs().write(0, profile.getUUID());
            spawn.getEntityTypeModifier().write(0, EntityType.PLAYER);
            spawn.getDoubles().write(0, currentLocation.getX());
            spawn.getDoubles().write(1, currentLocation.getY());
            spawn.getDoubles().write(2, currentLocation.getZ());
            // Modern ClientboundAddEntityPacket angle order: pitch, yaw, head yaw
            spawn.getBytes().write(0, (byte) (currentLocation.getPitch() * 256 / 360));
            spawn.getBytes().write(1, (byte) (currentLocation.getYaw() * 256 / 360));
            spawn.getBytes().write(2, (byte) (currentLocation.getYaw() * 256 / 360));
            manager.sendServerPacket(player, spawn);

            try {
                PacketContainer meta = manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
                meta.getIntegers().write(0, entityId);
                meta.getDataValueCollectionModifier().write(0, new ArrayList<>());
                manager.sendServerPacket(player, meta);
            } catch (Exception e) {
                Bukkit.getLogger().warning("[HerobrineAI] Could not send entity metadata: " + e.getMessage());
            }

            spawned = true;
            lastTX = Double.NaN; // force position resend after (re)spawn
            Bukkit.getLogger().info("[HerobrineAI] Successfully spawned Herobrine for " + player.getName());
        } catch (Exception e) {
            Bukkit.getLogger().severe("[HerobrineAI] Failed to spawn Herobrine for " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Cached NMS handles (looked up once, not every teleport)
    private static boolean nmsInitDone = false;
    private static boolean nmsAvailable = false;
    private static boolean nmsErrorLogged = false;
    private static java.lang.reflect.Constructor<?> vec3Ctor;
    private static java.lang.reflect.Constructor<?> pmrCtor;
    private static java.lang.reflect.Method pmrCreate; // pre-26.x fallback
    private static java.lang.reflect.Constructor<?> tpPacketCtor;

    private static synchronized void initNms() {
        if (nmsInitDone) return;
        nmsInitDone = true;
        try {
            Class<?> vec3Class = Class.forName("net.minecraft.world.phys.Vec3");
            vec3Ctor = vec3Class.getConstructor(double.class, double.class, double.class);

            Class<?> pmrClass = Class.forName("net.minecraft.world.entity.PositionMoveRotation");
            try {
                // 26.x: record canonical constructor (Vec3 position, Vec3 delta, float yRot, float xRot)
                pmrCtor = pmrClass.getConstructor(vec3Class, vec3Class, float.class, float.class);
            } catch (NoSuchMethodException e) {
                // Older versions: static factory create(Vec3, Vec3, float, float)
                pmrCreate = pmrClass.getMethod("create", vec3Class, vec3Class, float.class, float.class);
            }

            Class<?> tpPacketClass = Class.forName("net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket");
            tpPacketCtor = tpPacketClass.getConstructor(int.class, pmrClass, java.util.Set.class, boolean.class);
            nmsAvailable = true;
        } catch (Throwable t) {
            nmsAvailable = false;
            if (!nmsErrorLogged) {
                nmsErrorLogged = true;
                Bukkit.getLogger().warning("[HerobrineAI] NMS teleport unavailable: " + t);
            }
        }
    }

    private double lastTX = Double.NaN, lastTY, lastTZ;
    private float lastTYaw, lastTPitch;

    public void teleport(Location loc) {
        this.currentLocation = loc.clone();
        if (!spawned) return;

        // Skip sending if nothing changed (moveTo is called every few ticks while idle)
        if (!Double.isNaN(lastTX)
                && Math.abs(loc.getX() - lastTX) < 0.02 && Math.abs(loc.getY() - lastTY) < 0.02
                && Math.abs(loc.getZ() - lastTZ) < 0.02
                && Math.abs(loc.getYaw() - lastTYaw) < 0.5f && Math.abs(loc.getPitch() - lastTPitch) < 0.5f) {
            return;
        }
        lastTX = loc.getX(); lastTY = loc.getY(); lastTZ = loc.getZ();
        lastTYaw = loc.getYaw(); lastTPitch = loc.getPitch();

        initNms();
        if (nmsAvailable) {
            try {
                Object position = vec3Ctor.newInstance(loc.getX(), loc.getY(), loc.getZ());
                Object delta = vec3Ctor.newInstance(0.0, 0.0, 0.0);
                Object pmr = (pmrCtor != null)
                        ? pmrCtor.newInstance(position, delta, loc.getYaw(), loc.getPitch())
                        : pmrCreate.invoke(null, position, delta, loc.getYaw(), loc.getPitch());

                Object nmsPacket = tpPacketCtor.newInstance(entityId, pmr, java.util.Collections.emptySet(), false);
                broadcast(new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT, nmsPacket));
            } catch (Throwable t) {
                if (!nmsErrorLogged) {
                    nmsErrorLogged = true;
                    Bukkit.getLogger().warning("[HerobrineAI] teleport packet failed (further errors suppressed): " + t);
                }
            }
        }

        try {
            PacketContainer head = manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
            head.getIntegers().write(0, entityId);
            head.getBytes().write(0, (byte) (loc.getYaw() * 256 / 360));
            broadcast(head);
        } catch (Exception e) {
            if (!nmsErrorLogged) {
                nmsErrorLogged = true;
                Bukkit.getLogger().warning("[HerobrineAI] head rotation failed (further errors suppressed): " + e.getMessage());
            }
        }
    }

    public void lookAt(Location point) {
        if (!spawned) return;
        Location eye = currentLocation.clone().add(0, 1.62, 0);
        org.bukkit.util.Vector dir = point.toVector().subtract(eye.toVector()).normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
        float pitch = (float) Math.toDegrees(Math.asin(-dir.getY()));
        currentLocation.setYaw(yaw);
        currentLocation.setPitch(pitch);
        try {
            PacketContainer look = manager.createPacket(PacketType.Play.Server.ENTITY_LOOK);
            look.getIntegers().write(0, entityId);
            look.getBytes().write(0, (byte) (yaw * 256 / 360));
            look.getBytes().write(1, (byte) (pitch * 256 / 360));
            look.getBooleans().write(0, true);
            PacketContainer head = manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
            head.getIntegers().write(0, entityId);
            head.getBytes().write(0, (byte) (yaw * 256 / 360));
            broadcast(look);
            broadcast(head);
        } catch (Exception e) {
            if (!nmsErrorLogged) {
                nmsErrorLogged = true;
                Bukkit.getLogger().warning("[HerobrineAI] packet error (further errors suppressed): " + e.getMessage());
            }
        }
    }

    public void playAnimation(int animationId) {
        try {
            PacketContainer anim = manager.createPacket(PacketType.Play.Server.ANIMATION);
            anim.getIntegers().write(0, entityId);
            anim.getIntegers().write(1, animationId);
            broadcast(anim);
        } catch (Exception e) {
            if (!nmsErrorLogged) {
                nmsErrorLogged = true;
                Bukkit.getLogger().warning("[HerobrineAI] packet error (further errors suppressed): " + e.getMessage());
            }
        }
    }

    public void setEquipment(int slot, org.bukkit.inventory.ItemStack item) {
        if (!spawned) return;
        try {
            PacketContainer equip = manager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            equip.getIntegers().write(0, entityId);
            List<Pair<EnumWrappers.ItemSlot, org.bukkit.inventory.ItemStack>> pairs = Collections.singletonList(
                    new Pair<>(EnumWrappers.ItemSlot.values()[slot], item));
            equip.getSlotStackPairLists().write(0, pairs);
            broadcast(equip);
        } catch (Exception e) {
            if (!nmsErrorLogged) {
                nmsErrorLogged = true;
                Bukkit.getLogger().warning("[HerobrineAI] packet error (further errors suppressed): " + e.getMessage());
            }
        }
    }

    public void destroy() {
        if (!spawned) return;
        try {
            PacketContainer destroy = manager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
            destroy.getIntLists().write(0, Collections.singletonList(entityId));
            broadcast(destroy);
            PacketContainer infoRemove = manager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
            infoRemove.getUUIDLists().write(0, Collections.singletonList(profile.getUUID()));
            broadcast(infoRemove);
            spawned = false;
        } catch (Exception e) {
            if (!nmsErrorLogged) {
                nmsErrorLogged = true;
                Bukkit.getLogger().warning("[HerobrineAI] packet error (further errors suppressed): " + e.getMessage());
            }
        }
    }

    private void broadcast(PacketContainer packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (currentLocation.getWorld() != null && p.getWorld() != currentLocation.getWorld()) continue;
            try { manager.sendServerPacket(p, packet); }
            catch (Exception e) {
                if (!nmsErrorLogged) {
                    nmsErrorLogged = true;
                    Bukkit.getLogger().warning("[HerobrineAI] packet send failed (further errors suppressed): " + e.getMessage());
                }
            }
        }
    }

    public int getEntityId() { return entityId; }
    public Location getLocation() { return currentLocation.clone(); }
    public boolean isSpawned() { return spawned; }
}
