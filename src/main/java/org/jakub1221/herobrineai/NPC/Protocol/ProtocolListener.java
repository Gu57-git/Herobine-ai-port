package org.jakub1221.herobrineai.NPC.Protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.NPC.Entity.HumanNPC;

public class ProtocolListener extends PacketAdapter {

    public ProtocolListener() {
        super(HerobrineAI.getPluginCore(), PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    @SuppressWarnings("removal")
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        int targetId = packet.getIntegers().read(0);

        HumanNPC npc = HerobrineAI.getPluginCore().getNPCCore().getNPCByProtocolId(targetId);
        if (npc == null) return;

        EnumWrappers.EntityUseAction action = packet.getEntityUseActions().read(0);
        Player player = event.getPlayer();

        if (action == EnumWrappers.EntityUseAction.ATTACK) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(HerobrineAI.getPluginCore(), () -> {
                HerobrineAI.getPluginCore().getNPCCore()
                        .ensureZombie(npc, npc.getProtocolEntity().getLocation());
                double damage = 1.0;
                try {
                    damage = player.getAttribute(org.bukkit.attribute.Attribute.ATTACK_DAMAGE).getValue();
                } catch (Exception e) {
                    // Fallback
                }
                @SuppressWarnings("removal")
                EntityDamageByEntityEvent dmg = new EntityDamageByEntityEvent(
                        player, npc.getBukkitEntity(),
                        EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
                Bukkit.getPluginManager().callEvent(dmg);
                if (!dmg.isCancelled()) {
                    ((org.bukkit.entity.Damageable) npc.getBukkitEntity()).damage(dmg.getFinalDamage(), player);
                }
            });
        }
    }
}
