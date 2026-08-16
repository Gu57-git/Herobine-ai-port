package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class DestroyTorches extends Core {

    public DestroyTorches() {
        super(CoreType.DESTROY_TORCHES, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return destroyTorches((Location) data[0]);
    }

    public CoreResult destroyTorches(Location loc) {
        World world = loc.getWorld();
        int radius = PluginCore.getConfigDB().DestroyTorchesRadius;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location check = loc.clone().add(x, y, z);
                    if (check.getBlock().getType() == Material.TORCH || check.getBlock().getType() == Material.WALL_TORCH) {
                        check.getBlock().setType(Material.AIR);
                    }
                }
            }
        }
        return new CoreResult(true, "Torches destroyed!");
    }
}