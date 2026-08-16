package org.jakub1221.herobrineai.AI.extensions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class GraveyardWorld {
    public static void Create() {
        World world = Bukkit.getWorld("world_herobrineai_graveyard");
        if (world == null) return;

        // Clear area
        for (int x = -20; x <= 20; x++) {
            for (int z = -20; z <= 20; z++) {
                for (int y = 0; y <= 15; y++) {
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }

        // Ground - mossy stone
        for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
                world.getBlockAt(x, 3, z).setType(Material.MOSSY_COBBLESTONE);
                // Add some grass patches
                if (Math.random() < 0.3) {
                    world.getBlockAt(x, 4, z).setType(Material.SHORT_GRASS);
                }
            }
        }

        // Central tomb structure
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                world.getBlockAt(x, 4, z).setType(Material.COBBLESTONE);
            }
        }
        world.getBlockAt(0, 5, 0).setType(Material.CHEST);

        // Fence around
        for (int x = -8; x <= 8; x++) {
            world.getBlockAt(x, 4, -8).setType(Material.COBBLESTONE_WALL);
            world.getBlockAt(x, 4, 8).setType(Material.COBBLESTONE_WALL);
        }
        for (int z = -7; z <= 7; z++) {
            world.getBlockAt(-8, 4, z).setType(Material.COBBLESTONE_WALL);
            world.getBlockAt(8, 4, z).setType(Material.COBBLESTONE_WALL);
        }

        // Grave stones (crosses)
        createGrave(world, -5, 4, -5);
        createGrave(world, 5, 4, -5);
        createGrave(world, -5, 4, 5);
        createGrave(world, 5, 4, 5);
        createGrave(world, 0, 4, -6);

        // Dead trees
        createDeadTree(world, -10, 4, -10);
        createDeadTree(world, 10, 4, 10);
        createDeadTree(world, -10, 4, 10);

        // Torches
        world.getBlockAt(-8, 5, -8).setType(Material.TORCH);
        world.getBlockAt(8, 5, -8).setType(Material.TORCH);
        world.getBlockAt(-8, 5, 8).setType(Material.TORCH);
        world.getBlockAt(8, 5, 8).setType(Material.TORCH);

        Bukkit.getLogger().info("[HerobrineAI] Graveyard world created!");
    }

    private static void createGrave(World world, int x, int y, int z) {
        world.getBlockAt(x, y, z).setType(Material.COBBLESTONE);
        world.getBlockAt(x, y+1, z).setType(Material.COBBLESTONE_WALL);
        // Cross
        world.getBlockAt(x, y+2, z).setType(Material.COBBLESTONE_WALL);
    }

    private static void createDeadTree(World world, int x, int y, int z) {
        for (int h = 0; h < 4; h++) {
            world.getBlockAt(x, y+h, z).setType(Material.OAK_LOG);
        }
        world.getBlockAt(x+1, y+3, z).setType(Material.OAK_LOG);
        world.getBlockAt(x-1, y+2, z).setType(Material.OAK_LOG);
    }
}