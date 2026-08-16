package org.jakub1221.herobrineai.NPC;

import java.util.ArrayList;
import java.util.UUID;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;
import org.jakub1221.herobrineai.NPC.Protocol.ProtocolHerobrine;

public class NPCCore {

    private ArrayList<HumanNPC> npcs = new ArrayList<>();
    private int taskid;
    private int lastID = 0;
    private final UUID herobrineUUID;
    private final WrappedGameProfile herobrineProfile;

    public NPCCore(JavaPlugin plugin) {
        var cfg = HerobrineAI.getPluginCore().getConfigDB();
        this.herobrineUUID = UUID.fromString(cfg.HerobrineUUID);

        this.herobrineProfile = new WrappedGameProfile(herobrineUUID, cfg.HerobrineName);
        if (cfg.HerobrineTexture != null && cfg.HerobrineSignature != null) {
            herobrineProfile.getProperties().put("textures",
                new WrappedSignedProperty("textures", cfg.HerobrineTexture, cfg.HerobrineSignature));
        }

        this.taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(
            HerobrineAI.getPluginCore(), () -> {
                ArrayList<HumanNPC> toRemove = new ArrayList<>();
                for (HumanNPC humanNPC : npcs) {
                    if (humanNPC.getBukkitEntity() == null || humanNPC.getBukkitEntity().isDead()) {
                        toRemove.add(humanNPC);
                    }
                }
                toRemove.forEach(n -> {
                    n.getProtocolEntity().destroy();
                    npcs.remove(n);
                });
            }, 1L, 1L);
    }

    public void removeAll() {
        npcs.forEach(HumanNPC::removeFromWorld);
        npcs.clear();
    }

    public void DisableTask() {
        Bukkit.getScheduler().cancelTask(taskid);
    }

    public HumanNPC spawnHumanNPC(String name, Location l) {
        return spawnHumanNPC(name, l, ++lastID);
    }

    public HumanNPC spawnHumanNPC(String name, Location l, int id) {
        World world = l.getWorld();
        Zombie zombie = (Zombie) world.spawnEntity(l, EntityType.ZOMBIE);

        zombie.setCustomName(name);
        zombie.setCustomNameVisible(false);
        zombie.setAI(false);
        zombie.setSilent(true);
        zombie.setCollidable(false);
        zombie.setGravity(true);
        zombie.setRemoveWhenFarAway(false);
        zombie.setCanBreakDoors(false);
        zombie.setAdult();
        zombie.setShouldBurnInDay(false);
        zombie.setInvisible(true);
        zombie.addPotionEffect(new PotionEffect(
            PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));

        int protocolId = (int) (Math.random() * Integer.MAX_VALUE);
        ProtocolHerobrine ph = new ProtocolHerobrine(protocolId, herobrineProfile, l);
        ph.spawnAll();

        HumanNPC humannpc = new HumanNPC(zombie, ph, id);
        npcs.add(humannpc);
        Bukkit.getLogger().info("[HerobrineAI] Spawned Herobrine NPC (Zombie entity: " + zombie.getEntityId() + ", Protocol entity: " + protocolId + ")");
        return humannpc;
    }

    public HumanNPC getHumanNPC(int id) {
        for (HumanNPC n : npcs) {
            if (n.getID() == id) return n;
        }
        return null;
    }

    public HumanNPC getNPCByProtocolId(int protocolId) {
        for (HumanNPC n : npcs) {
            if (n.getProtocolEntity().getEntityId() == protocolId) return n;
        }
        return null;
    }

    public ArrayList<HumanNPC> getNPCs() {
        return new ArrayList<>(npcs);
    }
}