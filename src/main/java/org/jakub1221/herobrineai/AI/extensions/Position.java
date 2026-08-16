package org.jakub1221.herobrineai.AI.extensions;

import org.bukkit.Location;
import org.bukkit.World;

public class Position {
    public static Location getTeleportPosition(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                Location check = new Location(world, x + i, y, z + j);
                if (check.getBlock().getType().isAir()) {
                    return check;
                }
            }
        }
        return loc;
    }
}