package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Temple extends Core {

    public Temple() {
        super(CoreType.TEMPLE, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return buildTemple((Player) data[0]);
    }

    public CoreResult buildTemple(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                world.getBlockAt(x + i, y, z + j).setType(Material.MOSSY_COBBLESTONE);
            }
        }

        for (int h = 1; h <= 3; h++) {
            for (int i = -2; i <= 2; i++) {
                world.getBlockAt(x + i, y + h, z - 2).setType(Material.MOSSY_COBBLESTONE);
                world.getBlockAt(x + i, y + h, z + 2).setType(Material.MOSSY_COBBLESTONE);
            }
            for (int j = -1; j <= 1; j++) {
                world.getBlockAt(x - 2, y + h, z + j).setType(Material.MOSSY_COBBLESTONE);
                world.getBlockAt(x + 2, y + h, z + j).setType(Material.MOSSY_COBBLESTONE);
            }
        }

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                world.getBlockAt(x + i, y + 4, z + j).setType(Material.MOSSY_COBBLESTONE);
            }
        }

        for (int h = 1; h <= 2; h++) {
            for (int j = -1; j <= 1; j++) {
                world.getBlockAt(x - 2, y + h, z + j).setType(Material.IRON_BARS);
            }
        }

        world.getBlockAt(x + 2, y + 1, z).setType(Material.AIR);
        world.getBlockAt(x + 2, y + 2, z).setType(Material.AIR);

        world.getBlockAt(x, y + 1, z).setType(Material.CHEST);
        org.bukkit.block.Chest chest = (org.bukkit.block.Chest) world.getBlockAt(x, y + 1, z).getState();
        org.bukkit.inventory.Inventory inv = chest.getInventory();
        inv.addItem(PluginCore.getAICore().createAncientSword());
        inv.addItem(PluginCore.getAICore().createAppleOfDeath());
        inv.addItem(PluginCore.getAICore().createLightningSword());

        world.getBlockAt(x - 1, y + 3, z - 1).setType(Material.REDSTONE_TORCH);
        world.getBlockAt(x + 1, y + 3, z + 1).setType(Material.REDSTONE_TORCH);

        return new CoreResult(true, "Temple built!");
    }
}
