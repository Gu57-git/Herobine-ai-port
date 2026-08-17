package org.jakub1221.herobrineai.NPC.Entity;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jakub1221.herobrineai.NPC.Protocol.ProtocolHerobrine;

public class HumanNPC {

    private Zombie entity;
    private final ProtocolHerobrine protocolEntity;
    private final int id;

    public HumanNPC(Zombie entity, ProtocolHerobrine protocolEntity, int id) {
        this.entity = entity;
        this.protocolEntity = protocolEntity;
        this.id = id;
    }

    public int getID() { return id; }
    public Zombie getEntity() { return entity; }
    public void setEntity(Zombie entity) { this.entity = entity; }
    public ProtocolHerobrine getProtocolEntity() { return protocolEntity; }

    public void ArmSwingAnimation() {
        ensureAlive();
        if (entity != null) entity.swingMainHand();
        protocolEntity.playAnimation(0);
    }

    private void ensureAlive() {
        org.jakub1221.herobrineai.HerobrineAI.getPluginCore().getNPCCore()
                .ensureZombie(this, protocolEntity.getLocation());
    }

    public void HurtAnimation() {
        ensureAlive();
        if (entity != null) {
            double healthBefore = entity.getHealth();
            entity.damage(0.5D);
            entity.setHealth(healthBefore);
        }
        protocolEntity.playAnimation(1);
    }

    public void setItemInHand(ItemStack item) {
        ensureAlive();
        if (item != null && entity != null && entity.getEquipment() != null) {
            entity.getEquipment().setItemInMainHand(item);
            protocolEntity.setEquipment(0, item);
        }
    }

    public String getName() {
        return (entity != null && entity.getCustomName() != null) ? entity.getCustomName() : "Herobrine";
    }

    public void setPitch(float pitch) {
        Location loc = protocolEntity.getLocation();
        loc.setPitch(pitch);
        moveTo(loc);
    }

    public void moveTo(Location loc) {
        ensureAlive();
        if (entity != null) entity.teleport(loc);
        protocolEntity.teleport(loc);
    }

    public void Teleport(Location loc) { moveTo(loc); }

    public org.bukkit.inventory.EntityEquipment getInventory() {
        return entity.getEquipment();
    }

    public void removeFromWorld() {
        protocolEntity.destroy();
        if (entity != null) entity.remove();
    }

    public void setYaw(float yaw) {
        Location loc = protocolEntity.getLocation();
        loc.setYaw(yaw);
        moveTo(loc);
    }

    public void lookAtPoint(Location point) {
        if (protocolEntity.getLocation().getWorld() != point.getWorld()) return;
        ensureAlive();
        if (entity != null) {
            Location eye = entity.getEyeLocation();
            org.bukkit.util.Vector direction = point.toVector().subtract(eye.toVector());
            Location newLoc = eye.clone();
            newLoc.setDirection(direction);
            entity.teleport(newLoc);
        }
        protocolEntity.lookAt(point);
    }

    public void setYawA(float yaw) { setYaw(yaw); }

    public org.bukkit.entity.Entity getBukkitEntity() {
        return entity;
    }
}