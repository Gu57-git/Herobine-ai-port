package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Heads extends Core {

    private static final List<Location> placedHeads = new ArrayList<>();

    public Heads() {
        super(CoreType.HEADS, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return placeHead((String) data[0]);
    }

    public CoreResult placeHead(String playerName) {
        if (PluginCore.getConfigDB().maxHeads > 0) {
            PluginCore.getConfigDB().maxHeads--;
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                return new CoreResult(false, "Player not found!");
            }

            Location center = player.getLocation();
            World world = center.getWorld();

            int rx = Utils.getRandomGen().nextInt(7) - 3;
            int rz = Utils.getRandomGen().nextInt(7) - 3;
            int x = center.getBlockX() + rx;
            int z = center.getBlockZ() + rz;
            int y = world.getHighestBlockYAt(x, z);

            Location loc = new Location(world, x, y, z);
            Block block = world.getBlockAt(loc);
            block.setType(Material.PLAYER_HEAD);

            Skull skull = (Skull) block.getState();
            skull.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));

            BlockFace face = getFacing(loc, center);
            skull.setRotation(face);
            skull.update();

            placedHeads.add(loc);
            return new CoreResult(true, "Head placed!");
        }
        return new CoreResult(false, "Head limit reached!");
    }

    private BlockFace getFacing(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        if (Math.abs(dx) > Math.abs(dz)) {
            return dx > 0 ? BlockFace.EAST : BlockFace.WEST;
        } else {
            return dz > 0 ? BlockFace.SOUTH : BlockFace.NORTH;
        }
    }

    public static void clearHeads() {
        for (Location loc : new ArrayList<>(placedHeads)) {
            if (loc.getWorld() != null && loc.getWorld().getBlockAt(loc).getType() == Material.PLAYER_HEAD) {
                loc.getWorld().getBlockAt(loc).setType(Material.AIR);
            }
        }
        placedHeads.clear();
    }
}
