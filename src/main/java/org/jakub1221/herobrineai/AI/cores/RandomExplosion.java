package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomExplosion extends Core {

    public RandomExplosion() {
        super(CoreType.RANDOM_EXPLOSION, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return explode((Player) data[0]);
    }

    public CoreResult explode(Player player) {
        Location loc = player.getLocation();
        loc.getWorld().createExplosion(loc, 2.0f, false, false);
        return new CoreResult(true, "Explosion!");
    }
}