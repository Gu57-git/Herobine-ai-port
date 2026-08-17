package org.jakub1221.herobrineai.AI.cores;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;
import org.jakub1221.herobrineai.AI.Message;
import org.jakub1221.herobrineai.AI.extensions.Position;

public class Attack extends Core {

    private int ticksToEnd = 0;
    private int HandlerINT = 0;
    private boolean isHandler = false;

    public Attack() {
        super(CoreType.ATTACK, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return setAttackTarget((Player) data[0]);
    }

    public CoreResult setAttackTarget(Player player) {
        if (!PluginCore.getAICore().checkAncientSword(player.getInventory())) {
            if (PluginCore.getSupport().checkAttack(player.getLocation())) {
                if (!PluginCore.canAttackPlayerNoMSG(player)) {
                    return new CoreResult(false, "This player is protected.");
                }

                PluginCore.getAICore().CancelTarget(CoreType.ANY);
                HerobrineAI.HerobrineHP = HerobrineAI.HerobrineMaxHP;
                ticksToEnd = 0;
                AICore.PlayerTarget = player;
                AICore.isTarget = true;
                PluginCore.getAICore().setCoreTypeNow(CoreType.ATTACK);
                AICore.log.info("[HerobrineAI] Teleporting to target. (" + AICore.PlayerTarget.getName() + ")");
                Location ploc = AICore.PlayerTarget.getLocation();
                Object[] data = { ploc };
                PluginCore.getAICore().getCore(CoreType.DESTROY_TORCHES).RunCore(data);
                if (PluginCore.getConfigDB().UsePotionEffects) {
                    AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 1000, 1));
                    AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000, 1));
                    AICore.PlayerTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1000, 1));
                }
                Location tploc = Position.getTeleportPosition(ploc);
                PluginCore.HerobrineNPC.moveTo(tploc);
                Message.SendMessage(AICore.PlayerTarget);
                StartHandler();
                return new CoreResult(true, "Herobrine attacks " + player.getName() + "!");
            } else {
                return new CoreResult(false, "Player is in secure area.");
            }
        } else {
            return new CoreResult(false, "Player has Ancient Sword.");
        }
    }

    public void StopHandler() {
        if (isHandler) {
            Bukkit.getScheduler().cancelTask(HandlerINT);
            isHandler = false;
        }
    }

    public void StartHandler() {
        KeepLooking();
        FollowHideRepeat();
        isHandler = true;
        HandlerINT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
            public void run() { Handler(); }
        }, 1 * 5L, 1 * 5L);
    }

    private void Handler() {
        KeepLooking();
        if (ticksToEnd == 1 || ticksToEnd % 16 == 0)
            FollowHideRepeat();
    }

    public void KeepLooking() {
        if (AICore.PlayerTarget != null && AICore.PlayerTarget.isOnline() && AICore.isTarget
                && PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
            if (!AICore.PlayerTarget.isDead() && targetInSameWorld()) {
                if (ticksToEnd == 160) {
                    PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
                } else {
                    ticksToEnd++;
                    Location ploc = AICore.PlayerTarget.getLocation();
                    ploc.setY(ploc.getY() + 1.5);
                    PluginCore.HerobrineNPC.lookAtPoint(ploc);
                    if (PluginCore.getConfigDB().Lighting == true) {
                        int lchance = Utils.getRandomGen().nextInt(100);
                        if (lchance > 75) {
                            Location newloc = ploc.clone();
                            int randx = Utils.getRandomGen().nextInt(50);
                            int randz = Utils.getRandomGen().nextInt(50);
                            if (Utils.getRandomGen().nextBoolean()) newloc.setX(newloc.getX() + randx);
                            else newloc.setX(newloc.getX() - randx);
                            if (Utils.getRandomGen().nextBoolean()) newloc.setZ(newloc.getZ() + randz);
                            else newloc.setZ(newloc.getZ() - randz);
                            newloc.setY(250);
                            newloc.getWorld().strikeLightning(newloc);
                        }
                    }
                }
            } else {
                stopIfStale();
            }
        } else {
            stopIfStale();
        }
    }

    private void stopIfStale() {
        StopHandler();
        if (PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
            PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
        }
    }

    private boolean targetInSameWorld() {
        return AICore.PlayerTarget != null && PluginCore.HerobrineNPC != null
                && AICore.PlayerTarget.getWorld() == PluginCore.HerobrineNPC.getBukkitEntity().getWorld();
    }

    public void Follow() {
        if (AICore.PlayerTarget != null && AICore.PlayerTarget.isOnline() && AICore.isTarget
                && PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
            if (!AICore.PlayerTarget.isDead()) {
                if (PluginCore.getConfigDB().isWorldAllowed(AICore.PlayerTarget.getWorld().getName())
                        && targetInSameWorld()
                        && PluginCore.getSupport().checkAttack(AICore.PlayerTarget.getLocation())) {
                    PluginCore.HerobrineNPC.moveTo(Position.getTeleportPosition(AICore.PlayerTarget.getLocation()));
                    Location ploc = AICore.PlayerTarget.getLocation();
                    ploc.setY(ploc.getY() + 1.5);
                    PluginCore.HerobrineNPC.lookAtPoint(ploc);
                    AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.ENTITY_PLAYER_BREATH, 0.75f, 0.75f);
                    if (PluginCore.getConfigDB().HitPlayer == true) {
                        int hitchance = Utils.getRandomGen().nextInt(100);
                        if (hitchance < 55) {
                            AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), Sound.ENTITY_PLAYER_HURT, 0.75f, 0.75f);
                            AICore.PlayerTarget.damage(4);
                        }
                    }
                } else {
                    PluginCore.getAICore().CancelTarget(CoreType.ATTACK);
                }
            } else {
                stopIfStale();
            }
        } else {
            stopIfStale();
        }
    }

    public void Hide() {
        if (AICore.PlayerTarget != null && AICore.PlayerTarget.isOnline() && AICore.isTarget
                && PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
            if (!AICore.PlayerTarget.isDead()) {
                Location ploc = AICore.PlayerTarget.getLocation();
                ploc.setY(-20);
                for(int i=0; i < 5; i++){
                    for(float j=0; j < 2; j+= 0.5f){
                        Location hbloc = PluginCore.HerobrineNPC.getProtocolEntity().getLocation();
                        hbloc.setY(hbloc.getY() + j);
                        hbloc.getWorld().spawnParticle(Particle.SMOKE, hbloc, 10, 0.3, 0.3, 0.3, 0);
                    }
                }
                if (PluginCore.getConfigDB().SpawnBats) {
                    Location hbloc = PluginCore.HerobrineNPC.getProtocolEntity().getLocation();
                    ploc.getWorld().spawnEntity(hbloc, EntityType.BAT);
                    ploc.getWorld().spawnEntity(hbloc, EntityType.BAT);
                }
                PluginCore.HerobrineNPC.moveTo(ploc);
            } else {
                stopIfStale();
            }
        } else {
            stopIfStale();
        }
    }

    public void FollowHideRepeat() {
        if (AICore.PlayerTarget != null && AICore.PlayerTarget.isOnline() && AICore.isTarget
                && PluginCore.getAICore().getCoreTypeNow() == CoreType.ATTACK) {
            if (!AICore.PlayerTarget.isDead()) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
                    public void run() {
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AICore.plugin, new Runnable() {
                            public void run() { Hide(); }
                        }, 1 * 30L);
                        Follow();
                    }
                }, 1 * 45L);
            } else {
                stopIfStale();
            }
        } else {
            stopIfStale();
        }
    }
}