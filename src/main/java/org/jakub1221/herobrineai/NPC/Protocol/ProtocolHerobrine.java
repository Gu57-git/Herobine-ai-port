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
            spawn.getBytes().write(0, (byte) (currentLocation.getYaw() * 256 / 360));
            spawn.getBytes().write(1, (byte) (currentLocation.getPitch() * 256 / 360));
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
            Bukkit.getLogger().info("[HerobrineAI] Successfully spawned Herobrine for " + player.getName());
        } catch (Exception e) {
            Bukkit.getLogger().severe("[HerobrineAI] Failed to spawn Herobrine for " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void teleport(Location loc) {
        this.currentLocation = loc.clone();
        if (!spawned) return;

        try {
            Class<?> vec3Class = Class.forName("net.minecraft.world.phys.Vec3");
            Object position = vec3Class.getConstructor(double.class, double.class, double.class)
                    .newInstance(loc.getX(), loc.getY(), loc.getZ());
            Object delta = vec3Class.getConstructor(double.class, double.class, double.class)
                    .newInstance(0.0, 0.0, 0.0);

            Class<?> pmrClass = Class.forName("net.minecraft.world.entity.PositionMoveRotation");
            Object pmr = pmrClass.getMethod("create", vec3Class, vec3Class, float.class, float.class)
                    .invoke(null, position, delta, loc.getYaw(), loc.getPitch());

            Class<?> tpPacketClass = Class.forName("net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket");
            Object nmsPacket = tpPacketClass.getConstructor(int.class, pmrClass, java.util.Set.class, boolean.class)
                    .newInstance(entityId, pmr, java.util.Collections.emptySet(), false);

            PacketContainer tp = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT, nmsPacket);
            broadcast(tp);

            PacketContainer head = manager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
            head.getIntegers().write(0, entityId);
            head.getBytes().write(0, (byte) (loc.getYaw() * 256 / 360));
            broadcast(head);
        } catch (Exception e) {
            Bukkit.getLogger().warning("[HerobrineAI] teleport failed: " + e.getMessage());
            e.printStackTrace();
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
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playAnimation(int animationId) {
        try {
            PacketContainer anim = manager.createPacket(PacketType.Play.Server.ANIMATION);
            anim.getIntegers().write(0, entityId);
            anim.getIntegers().write(1, animationId);
            broadcast(anim);
        } catch (Exception e) { e.printStackTrace(); }
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
        } catch (Exception e) { e.printStackTrace(); }
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
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void broadcast(PacketContainer packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try { manager.sendServerPacket(p, packet); }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public int getEntityId() { return entityId; }
    public Location getLocation() { return currentLocation.clone(); }
    public boolean isSpawned() { return spawned; }
}
