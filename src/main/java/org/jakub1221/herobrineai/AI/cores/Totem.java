package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jakub1221.herobrineai.HerobrineAI;
import org.bukkit.Bukkit;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Totem extends Core {

    public Totem() {
        super(CoreType.TOTEM, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return buildTotem((Location) data[0], (String) data[1]);
    }

    public CoreResult buildTotem(Location loc, String playerName) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        world.getBlockAt(x, y, z).setType(Material.NETHERRACK);
        world.getBlockAt(x, y + 1, z).setType(Material.FIRE);

        if (PluginCore.getConfigDB().TotemExplodes) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(AICore.plugin, () -> {
                world.createExplosion(loc, 3.0f, false, false);
            }, 40L);
        }

        AICore.isTotemCalled = false;
        return new CoreResult(true, "Totem built!");
    }
}