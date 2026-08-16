package org.jakub1221.herobrineai.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class StructureLoader {
    public static void loadStructure(Location loc, List<String> blocks) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        for (String block : blocks) {
            String[] parts = block.split(":");
            if (parts.length >= 4) {
                int dx = Integer.parseInt(parts[0]);
                int dy = Integer.parseInt(parts[1]);
                int dz = Integer.parseInt(parts[2]);
                Material mat = Material.matchMaterial(parts[3]);
                if (mat != null) {
                    world.getBlockAt(x + dx, y + dy, z + dz).setType(mat);
                }
            }
        }
    }
}