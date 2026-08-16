package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class BuildCave extends Core {

    public BuildCave() {
        super(CoreType.BUILD_CAVE, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return buildCave((Location) data[0]);
    }

    public CoreResult buildCave(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    world.getBlockAt(x + i, y + j, z + k).setType(Material.AIR);
                }
            }
        }
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                world.getBlockAt(x + i, y - 2, z + j).setType(Material.STONE);
            }
        }
        return new CoreResult(true, "Cave built!");
    }
}