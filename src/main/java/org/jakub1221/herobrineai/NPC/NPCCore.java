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
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
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

    private NamespacedKey npcKey;

    public NPCCore(JavaPlugin plugin) {
        var cfg = HerobrineAI.getPluginCore().getConfigDB();
        this.herobrineUUID = UUID.fromString(cfg.HerobrineUUID);
        this.npcKey = new NamespacedKey(HerobrineAI.getPluginCore(), "npc");

        this.herobrineProfile = new WrappedGameProfile(herobrineUUID, cfg.HerobrineName);
        try {
            if (cfg.HerobrineTexture != null && cfg.HerobrineSignature != null) {
                herobrineProfile.getProperties().put("textures",
                    new WrappedSignedProperty("textures", cfg.HerobrineTexture, cfg.HerobrineSignature));
            }
        } catch (Throwable t) {
            // GameProfile internals changed in recent MC versions; skin may not apply, but the plugin must still load
            HerobrineAI.log.warning("[HerobrineAI] Could not apply Herobrine skin: " + t);
        }
        this.taskid = -1; // no cleanup task: unloaded NPCs are healed on demand via ensureZombie()
    }

    private Zombie createHerobrineZombie(String name, Location l) {
        Zombie zombie = (Zombie) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
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
        zombie.getPersistentDataContainer().set(npcKey, PersistentDataType.BYTE, (byte) 1);
        zombie.addPotionEffect(new PotionEffect(
            PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
        return zombie;
    }

    private Zombie findPersistedNpcZombie(World world) {
        for (Zombie z : world.getEntitiesByClass(Zombie.class)) {
            if (z.getPersistentDataContainer().has(npcKey)) return z;
        }
        return null;
    }

    /**
     * The backing zombie gets unloaded with its chunk when no players are around,
     * which kills the Bukkit handle. Re-adopt the persisted entity or respawn it,
     * but only when players are actually in the world (otherwise keep it dormant).
     */
    public void ensureZombie(HumanNPC npc, Location loc) {
        Zombie z = npc.getEntity();
        if (z != null && !z.isDead() && z.isValid()) return;
        if (loc.getWorld() == null) return;
        if (loc.getWorld().getPlayers().isEmpty()) return; // dormant, protocol entity still visible client-side

        Zombie found = findPersistedNpcZombie(loc.getWorld());
        Zombie nz = (found != null) ? found : createHerobrineZombie(npc.getName(), loc);
        npc.setEntity(nz);
        HerobrineAI.getPluginCore().HerobrineEntityID = nz.getEntityId();
    }

    public void removeAll() {
        npcs.forEach(HumanNPC::removeFromWorld);
        npcs.clear();
    }

    public void DisableTask() {
        if (taskid > 0) Bukkit.getScheduler().cancelTask(taskid);
    }

    public HumanNPC spawnHumanNPC(String name, Location l) {
        return spawnHumanNPC(name, l, ++lastID);
    }

    public HumanNPC spawnHumanNPC(String name, Location l, int id) {
        Zombie zombie = createHerobrineZombie(name, l);

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