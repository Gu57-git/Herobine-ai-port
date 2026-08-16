package org.jakub1221.herobrineai;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Utils {
    private static Random randomGen = new Random();

    public static Random getRandomGen() {
        return randomGen;
    }

    public static Player getRandomPlayer() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return null;
        int index = randomGen.nextInt(Bukkit.getOnlinePlayers().size());
        return (Player) Bukkit.getOnlinePlayers().toArray()[index];
    }
}