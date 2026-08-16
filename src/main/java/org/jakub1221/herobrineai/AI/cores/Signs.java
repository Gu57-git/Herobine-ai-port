package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Signs extends Core {

    public Signs() {
        super(CoreType.SIGNS, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return placeSign((Location) data[0]);
    }

    public CoreResult placeSign(Location loc) {
        if (PluginCore.getConfigDB().maxSigns > 0) {
            PluginCore.getConfigDB().maxSigns--;
            World world = loc.getWorld();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            world.getBlockAt(x, y, z).setType(Material.OAK_SIGN);
            Sign sign = (Sign) world.getBlockAt(x, y, z).getState();
            if (!PluginCore.getConfigDB().useSignMessages.isEmpty()) {
                int index = Utils.getRandomGen().nextInt(PluginCore.getConfigDB().useSignMessages.size());
                sign.setLine(0, PluginCore.getConfigDB().useSignMessages.get(index));
            } else {
                sign.setLine(0, "I see you...");
            }
            sign.update();
            return new CoreResult(true, "Sign placed!");
        }
        return new CoreResult(false, "Sign limit reached!");
    }
}