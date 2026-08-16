package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Burn extends Core {

    public Burn() {
        super(CoreType.BURN, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return burn((Player) data[0]);
    }

    public CoreResult burn(Player player) {
        player.setFireTicks(100);
        return new CoreResult(true, "Player burned!");
    }
}