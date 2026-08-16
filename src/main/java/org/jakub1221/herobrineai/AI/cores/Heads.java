package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Skull;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Heads extends Core {

    public Heads() {
        super(CoreType.HEADS, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return placeHead((String) data[0]);
    }

    public CoreResult placeHead(String playerName) {
        if (PluginCore.getConfigDB().maxHeads > 0) {
            PluginCore.getConfigDB().maxHeads--;
            Location loc = PluginCore.HerobrineNPC.getBukkitEntity().getLocation();
            World world = loc.getWorld();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            world.getBlockAt(x, y, z).setType(Material.PLAYER_HEAD);
            Skull skull = (Skull) world.getBlockAt(x, y, z).getState();
            skull.setOwner(playerName);
            skull.update();
            return new CoreResult(true, "Head placed!");
        }
        return new CoreResult(false, "Head limit reached!");
    }
}