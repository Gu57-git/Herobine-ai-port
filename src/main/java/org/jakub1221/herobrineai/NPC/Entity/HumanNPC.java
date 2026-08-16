package org.jakub1221.herobrineai.NPC.Entity;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.NPC.Protocol.ProtocolHerobrine;

public class HumanNPC {

    private final Zombie entity;
    private final ProtocolHerobrine protocolEntity;
    private final int id;

    public HumanNPC(Zombie entity, ProtocolHerobrine protocolEntity, int id) {
        this.entity = entity;
        this.protocolEntity = protocolEntity;
        this.id = id;
    }

    public int getID() { return id; }
    public Zombie getEntity() { return entity; }
    public ProtocolHerobrine getProtocolEntity() { return protocolEntity; }

    public void ArmSwingAnimation() {
        entity.swingMainHand();
        protocolEntity.playAnimation(0);
    }

    public void HurtAnimation() {
        double healthBefore = entity.getHealth();
        entity.damage(0.5D);
        entity.setHealth(healthBefore);
        protocolEntity.playAnimation(1);
    }

    public void setItemInHand(ItemStack item) {
        if (item != null && entity.getEquipment() != null) {
            entity.getEquipment().setItemInMainHand(item);
            protocolEntity.setEquipment(0, item);
        }
    }

    public String getName() {
        return entity.getCustomName() != null ? entity.getCustomName() : "Herobrine";
    }

    public void setPitch(float pitch) {
        Location loc = entity.getLocation();
        loc.setPitch(pitch);
        moveTo(loc);
    }

    public void moveTo(Location loc) {
        entity.teleport(loc);
        protocolEntity.teleport(loc);
    }

    public void Teleport(Location loc) { moveTo(loc); }

    public org.bukkit.inventory.EntityEquipment getInventory() {
        return entity.getEquipment();
    }

    public void removeFromWorld() {
        protocolEntity.destroy();
        entity.remove();
    }

    public void setYaw(float yaw) {
        Location loc = entity.getLocation();
        loc.setYaw(yaw);
        moveTo(loc);
    }

    public void lookAtPoint(Location point) {
        if (entity.getWorld() != point.getWorld()) return;
        Location eye = entity.getEyeLocation();
        org.bukkit.util.Vector direction = point.toVector().subtract(eye.toVector());
        Location newLoc = eye.clone();
        newLoc.setDirection(direction);
        entity.teleport(newLoc);
        protocolEntity.lookAt(point);
    }

    public void setYawA(float yaw) { setYaw(yaw); }

    public org.bukkit.entity.Entity getBukkitEntity() {
        return entity;
    }
}