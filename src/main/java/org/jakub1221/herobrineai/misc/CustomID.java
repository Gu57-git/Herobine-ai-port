package org.jakub1221.herobrineai.misc;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomID {
    private Material material;
    private int amount;

    public CustomID(String materialName) {
        this.material = Material.matchMaterial(materialName);
        this.amount = 1;
    }

    public CustomID(String materialName, int amount) {
        this.material = Material.matchMaterial(materialName);
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        if (material != null) {
            return new ItemStack(material, amount);
        }
        return null;
    }

    public Material getMaterial() {
        return material;
    }
}