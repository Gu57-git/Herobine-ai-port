package org.jakub1221.herobrineai.entity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Color;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.misc.ItemName;

public class EntityManager {

    private HashMap<Integer, CustomEntity> mobList = new HashMap<>();

    public void spawnCustomZombie(Location loc, MobType mbt) {
        World world = loc.getWorld();
        Zombie zombie = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
        zombie.setRemoveWhenFarAway(false);

        if (mbt == MobType.ARTIFACT_GUARDIAN) setupGuardian(zombie);
        else if (mbt == MobType.HEROBRINE_WARRIOR) setupWarrior(zombie);

        mobList.put(zombie.getEntityId(), new BukkitCustomEntity(zombie, mbt));
    }

    public void spawnCustomSkeleton(Location loc, MobType mbt) {
        World world = loc.getWorld();
        Skeleton skel = (Skeleton) world.spawnEntity(loc, EntityType.SKELETON);
        skel.setRemoveWhenFarAway(false);

        if (mbt == MobType.DEMON) setupDemon(skel);

        mobList.put(skel.getEntityId(), new BukkitCustomEntity(skel, mbt));
    }

    private void setSpeedAndHealth(org.bukkit.entity.LivingEntity ent, double speed, double hp) {
        AttributeInstance s = ent.getAttribute(Attribute.MOVEMENT_SPEED);
        if (s != null) s.setBaseValue(speed);
        AttributeInstance h = ent.getAttribute(Attribute.MAX_HEALTH);
        if (h != null) h.setBaseValue(hp);
        ent.setHealth(hp);
    }

    private void setupGuardian(Zombie z) {
        var cfg = HerobrineAI.getPluginCore().getConfigDB().npc;
        setSpeedAndHealth(z, cfg.getDouble("npc.Guardian.Speed"), cfg.getInt("npc.Guardian.HP"));
        z.setCustomName("Artifact Guardian");
        z.getEquipment().setItemInMainHand(new ItemStack(org.bukkit.Material.GOLDEN_SWORD));
        z.getEquipment().setHelmet(new ItemStack(org.bukkit.Material.GOLDEN_HELMET));
        z.getEquipment().setChestplate(new ItemStack(org.bukkit.Material.GOLDEN_CHESTPLATE));
        z.getEquipment().setLeggings(new ItemStack(org.bukkit.Material.GOLDEN_LEGGINGS));
        z.getEquipment().setBoots(new ItemStack(org.bukkit.Material.GOLDEN_BOOTS));
    }

    private void setupWarrior(Zombie z) {
        var cfg = HerobrineAI.getPluginCore().getConfigDB().npc;
        setSpeedAndHealth(z, cfg.getDouble("npc.Warrior.Speed"), cfg.getInt("npc.Warrior.HP"));
        z.setCustomName("Herobrine Warrior");
        z.getEquipment().setItemInMainHand(new ItemStack(org.bukkit.Material.IRON_SWORD));
        z.getEquipment().setHelmet(new ItemStack(org.bukkit.Material.IRON_HELMET));
        z.getEquipment().setChestplate(new ItemStack(org.bukkit.Material.IRON_CHESTPLATE));
        z.getEquipment().setLeggings(new ItemStack(org.bukkit.Material.IRON_LEGGINGS));
        z.getEquipment().setBoots(new ItemStack(org.bukkit.Material.IRON_BOOTS));
    }

    private void setupDemon(Skeleton s) {
        var cfg = HerobrineAI.getPluginCore().getConfigDB().npc;
        setSpeedAndHealth(s, cfg.getDouble("npc.Demon.Speed"), cfg.getInt("npc.Demon.HP"));
        s.setCustomName("Demon");
        s.getEquipment().setItemInMainHand(new ItemStack(org.bukkit.Material.GOLDEN_APPLE));
        s.getEquipment().setHelmet(ItemName.colorLeatherArmor(
            new ItemStack(org.bukkit.Material.LEATHER_HELMET), Color.RED));
        s.getEquipment().setChestplate(ItemName.colorLeatherArmor(
            new ItemStack(org.bukkit.Material.LEATHER_CHESTPLATE), Color.RED));
        s.getEquipment().setLeggings(ItemName.colorLeatherArmor(
            new ItemStack(org.bukkit.Material.LEATHER_LEGGINGS), Color.RED));
        s.getEquipment().setBoots(ItemName.colorLeatherArmor(
            new ItemStack(org.bukkit.Material.LEATHER_BOOTS), Color.RED));
    }

    public boolean isCustomMob(int id) { return mobList.containsKey(id); }
    public CustomEntity getMobType(int id) { return mobList.get(id); }

    public void removeMob(int id) {
        CustomEntity ent = mobList.get(id);
        if (ent != null) ent.Kill();
        mobList.remove(id);
    }

    public void removeAllMobs() { mobList.clear(); }

    public void killAllMobs() {
        mobList.values().forEach(CustomEntity::Kill);
        removeAllMobs();
    }
}

class BukkitCustomEntity implements CustomEntity {
    private final org.bukkit.entity.LivingEntity entity;
    private final MobType type;

    public BukkitCustomEntity(org.bukkit.entity.LivingEntity entity, MobType type) {
        this.entity = entity;
        this.type = type;
    }

    public void Kill() { entity.setHealth(0); }
    public MobType getMobType() { return type; }

    public org.bukkit.entity.LivingEntity getEntity() {
        return entity;
    }
}