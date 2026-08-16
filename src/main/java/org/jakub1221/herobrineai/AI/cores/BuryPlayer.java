package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class BuryPlayer extends Core {

    public BuryPlayer() {
        super(CoreType.BURY_PLAYER, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return bury((Player) data[0]);
    }

    public CoreResult bury(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = 0; k <= 2; k++) {
                    world.getBlockAt(x + i, y + k, z + j).setType(Material.DIRT);
                }
            }
        }
        return new CoreResult(true, "Player buried!");
    }
}