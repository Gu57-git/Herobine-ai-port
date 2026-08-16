package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class RandomSound extends Core {

    public RandomSound() {
        super(CoreType.RANDOM_SOUND, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return playSound((Player) data[0]);
    }

    public CoreResult playSound(Player player) {
        player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
        return new CoreResult(true, "Sound played!");
    }
}