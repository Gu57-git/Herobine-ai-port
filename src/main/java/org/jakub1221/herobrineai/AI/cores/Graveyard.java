package org.jakub1221.herobrineai.AI.cores;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Graveyard extends Core {

    private Location savedLocation = null;

    public Graveyard() {
        super(CoreType.GRAVEYARD, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return graveyard((Player) data[0]);
    }

    public CoreResult graveyard(Player player) {
        savedLocation = player.getLocation().clone();
        World graveyard = Bukkit.getWorld("world_herobrineai_graveyard");
        if (graveyard != null) {
            player.teleport(new Location(graveyard, -2.49, 4, 10.69, -179.85f, 0.45f));
            AICore.isTarget = true;
            AICore.PlayerTarget = player;
            PluginCore.getAICore().setCoreTypeNow(CoreType.GRAVEYARD);

            // Schedule return after 15 seconds (cutscene duration)
            Bukkit.getScheduler().scheduleSyncDelayedTask(AICore.plugin, () -> {
                if (player.isOnline() && player.getWorld().getName().equals("world_herobrineai_graveyard")) {
                    // Return player to original location
                    player.teleport(savedLocation);
                    AICore.isTarget = false;
                    AICore.PlayerTarget = null;
                    PluginCore.getAICore().setCoreTypeNow(CoreType.ANY);
                    player.sendMessage("<Herobrine> You escaped... this time.");
                }
            }, 20L * 15); // 15 seconds

            return new CoreResult(true, "Player teleported to graveyard!");
        }
        return new CoreResult(false, "Graveyard world not found!");
    }

    public Location getSavedLocation() {
        return savedLocation;
    }
}