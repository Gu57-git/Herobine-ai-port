package org.jakub1221.herobrineai.AI;

import org.bukkit.Bukkit;
import org.jakub1221.herobrineai.HerobrineAI;

public class ResetLimits {
    private int taskId = -1;

    public ResetLimits() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(HerobrineAI.getPluginCore(), () -> {
            HerobrineAI.getPluginCore().getConfigDB().maxBooks = 1;
            HerobrineAI.getPluginCore().getConfigDB().maxSigns = 1;
            HerobrineAI.getPluginCore().getConfigDB().maxHeads = 1;
        }, 72000L, 72000L);
    }

    public void disable() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }
}