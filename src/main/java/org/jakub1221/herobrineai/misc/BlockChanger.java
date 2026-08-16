package org.jakub1221.herobrineai.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockChanger {
    public static void ChangeBlock(Location loc, Material material) {
        Block b = loc.getBlock();
        b.setType(material);
    }
}