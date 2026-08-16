package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Pyramid extends Core {

    public Pyramid() {
        super(CoreType.PYRAMID, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return buildPyramid((Player) data[0]);
    }

    public CoreResult buildPyramid(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                world.getBlockAt(x + i, y, z + j).setType(Material.SANDSTONE);
            }
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                world.getBlockAt(x + i, y + 1, z + j).setType(Material.SANDSTONE);
            }
        }
        world.getBlockAt(x, y + 2, z).setType(Material.SANDSTONE);
        return new CoreResult(true, "Pyramid built!");
    }
}