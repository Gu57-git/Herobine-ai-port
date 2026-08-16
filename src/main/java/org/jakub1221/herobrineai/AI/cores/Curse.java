package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Curse extends Core {

    public Curse() {
        super(CoreType.CURSE, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return curse((Player) data[0]);
    }

    public CoreResult curse(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 200, 1));
        return new CoreResult(true, "Player cursed!");
    }
}