package org.jakub1221.herobrineai.NPC.AI;

import org.bukkit.Location;
import org.jakub1221.herobrineai.HerobrineAI;

public class Path {
    private float x;
    private float z;
    private HerobrineAI plugin;

    public Path(float x, float z, HerobrineAI plugin) {
        this.x = x;
        this.z = z;
        this.plugin = plugin;
    }

    public void update() {
        if (plugin.HerobrineNPC != null && plugin.HerobrineNPC.getBukkitEntity() != null) {
            Location loc = plugin.HerobrineNPC.getBukkitEntity().getLocation();
            loc.setX(loc.getX() + x);
            loc.setZ(loc.getZ() + z);
            plugin.HerobrineNPC.moveTo(loc);
        }
    }

    public float getX() { return x; }
    public float getZ() { return z; }
}