package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomPosition extends Core {

    private int randomTicks = 0;

    public RandomPosition() {
        super(CoreType.RANDOM_POSITION, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return setRandomPosition((World) data[0]);
    }

    public CoreResult setRandomPosition(World world) {
        int x = Utils.getRandomGen().nextInt(PluginCore.getConfigDB().WalkingModeXRadius * 2) - PluginCore.getConfigDB().WalkingModeXRadius;
        int z = Utils.getRandomGen().nextInt(PluginCore.getConfigDB().WalkingModeZRadius * 2) - PluginCore.getConfigDB().WalkingModeZRadius;
        Location loc = new Location(world, x, 100, z);
        loc.setY(world.getHighestBlockYAt(x, z));
        PluginCore.HerobrineNPC.moveTo(loc);
        AICore.isTarget = true;
        PluginCore.getAICore().setCoreTypeNow(CoreType.RANDOM_POSITION);
        PluginCore.getAICore().Start_RM();
        PluginCore.getAICore().Start_RS();
        PluginCore.getAICore().Start_CG();
        return new CoreResult(true, "Random position set!");
    }

    public void RandomMove() {
        if (AICore.isTarget && PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
            Location loc = PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
            loc.setX(loc.getX() + (Utils.getRandomGen().nextInt(3) - 1));
            loc.setZ(loc.getZ() + (Utils.getRandomGen().nextInt(3) - 1));
            loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
            PluginCore.HerobrineNPC.moveTo(loc);
        }
    }

    public void CheckGravity() {
        if (AICore.isTarget && PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
            Location loc = PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
            if (loc.getY() < -10) {
                loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
                PluginCore.HerobrineNPC.moveTo(loc);
            }
        }
    }

    public void CheckPlayerPosition() {
        if (AICore.isTarget && PluginCore.getAICore().getCoreTypeNow() == CoreType.RANDOM_POSITION) {
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                if (player.getWorld() == PluginCore.HerobrineNPC.getBukkitEntity().getWorld()) {
                    if (player.getLocation().distance(PluginCore.HerobrineNPC.getBukkitEntity().getLocation()) < 10) {
                        PluginCore.HerobrineNPC.lookAtPoint(player.getLocation());
                    }
                }
            }
        }
    }

    public void setRandomTicks(int ticks) {
        this.randomTicks = ticks;
    }
}