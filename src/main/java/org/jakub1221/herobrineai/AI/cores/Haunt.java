package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Haunt extends Core {

    private int _ticks = 0;
    private int spawnedWolves = 0;
    private int spawnedBats = 0;
    private int KL_INT = 0;
    private int PS_INT = 0;
    private boolean isHandler = false;
    private boolean isFirst = true;

    public Haunt() {
        super(CoreType.HAUNT, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return setHauntTarget((Player) data[0]);
    }

    public CoreResult setHauntTarget(Player player) {
        if (PluginCore.getSupport().checkHaunt(player.getLocation())) {
            if (!PluginCore.canAttackPlayerNoMSG(player)) {
                return new CoreResult(false, "This player is protected.");
            }
            PluginCore.getAICore().CancelTarget(CoreType.ANY);
            spawnedWolves = 0;
            spawnedBats = 0;
            _ticks = 0;
            isFirst = true;
            AICore.isTarget = true;
            AICore.PlayerTarget = player;
            PluginCore.getAICore().setCoreTypeNow(CoreType.HAUNT);
            AICore.log.info("[HerobrineAI] Hauntig player!");
            Location loc = PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
            loc.setY(-20);
            PluginCore.HerobrineNPC.moveTo(loc);
            StartHandler();
            return new CoreResult(true, "Herobrine haunts " + player.getName() + "!");
        }
        return new CoreResult(false, "Player is in secure area!");
    }

    public void StartHandler() {
        isHandler = true;
        KL_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
            public void run() { KeepLookingHaunt(); }
        }, 1 * 5L, 1 * 5L);
        PS_INT = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(AICore.plugin, new Runnable() {
            public void run() { PlaySounds(); }
        }, 1 * 35L, 1 * 35L);
    }

    public void StopHandler() {
        if (isHandler) {
            isHandler = false;
            Bukkit.getServer().getScheduler().cancelTask(KL_INT);
            Bukkit.getServer().getScheduler().cancelTask(PS_INT);
        }
    }

    private void stopIfStale() {
        StopHandler();
        if (PluginCore.getAICore().getCoreTypeNow() == CoreType.HAUNT) {
            PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
        }
    }

    private void hideHerobrine() {
        Location hbloc = PluginCore.HerobrineNPC.getProtocolEntity().getLocation();
        hbloc.getWorld().spawnParticle(org.bukkit.Particle.SMOKE, hbloc, 20, 0.3, 1, 0.3, 0);
        Location hide = hbloc.clone();
        hide.setY(-20);
        PluginCore.HerobrineNPC.moveTo(hide);
    }

    private void KeepLookingHaunt() {
        if (AICore.PlayerTarget != null && AICore.PlayerTarget.isOnline() && AICore.isTarget
                && PluginCore.getAICore().getCoreTypeNow() == CoreType.HAUNT) {
            if (!AICore.PlayerTarget.isDead()
                    && AICore.PlayerTarget.getWorld() == PluginCore.HerobrineNPC.getBukkitEntity().getWorld()) {
                if (_ticks >= 300) {
                    hideHerobrine();
                    PluginCore.getAICore().CancelTarget(CoreType.HAUNT);
                } else {
                    _ticks++;
                    // Herobrine appears near the player, stares, then vanishes
                    if (_ticks % 80 == 20) {
                        Location ploc = AICore.PlayerTarget.getLocation();
                        double angle = Utils.getRandomGen().nextDouble() * Math.PI * 2;
                        double dist = 4 + Utils.getRandomGen().nextInt(5);
                        Location hloc = ploc.clone().add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
                        int highest = ploc.getWorld().getHighestBlockYAt(hloc.getBlockX(), hloc.getBlockZ());
                        hloc.setY(Math.abs(highest - ploc.getY()) > 5 ? ploc.getY() : highest);
                        PluginCore.HerobrineNPC.moveTo(hloc);
                        Location look = ploc.clone();
                        look.setY(look.getY() + 1.5);
                        PluginCore.HerobrineNPC.lookAtPoint(look);
                        PluginCore.HerobrineNPC.ArmSwingAnimation();
                    } else if (_ticks % 80 == 60) {
                        hideHerobrine();
                    }
                    if (isFirst) {
                        isFirst = false;
                        if (PluginCore.getConfigDB().SpawnWolves) {
                            if (spawnedWolves < 3) {
                                spawnedWolves++;
                                Location ploc = AICore.PlayerTarget.getLocation();
                                Wolf wolf = (Wolf) ploc.getWorld().spawnEntity(ploc, EntityType.WOLF);
                                wolf.setAngry(true);
                                wolf.setTarget(AICore.PlayerTarget);
                            }
                        }
                        if (PluginCore.getConfigDB().SpawnBats) {
                            if (spawnedBats < 5) {
                                spawnedBats++;
                                Location ploc = AICore.PlayerTarget.getLocation();
                                ploc.getWorld().spawnEntity(ploc, EntityType.BAT);
                            }
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

    private void PlaySounds() {
        if (AICore.PlayerTarget != null && AICore.PlayerTarget.isOnline() && AICore.isTarget
                && PluginCore.getAICore().getCoreTypeNow() == CoreType.HAUNT) {
            AICore.PlayerTarget.playSound(AICore.PlayerTarget.getLocation(), org.bukkit.Sound.AMBIENT_CAVE, 1.0f, 1.0f);
        }
    }
}