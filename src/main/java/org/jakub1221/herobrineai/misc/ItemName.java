package org.jakub1221.herobrineai.misc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemName {
    public static ItemStack setNameAndLore(ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static List<String> getLore(ItemStack item) {
        if (item != null && item.getItemMeta() != null) {
            return item.getItemMeta().getLore();
        }
        return null;
    }

    public static ItemStack colorLeatherArmor(ItemStack item, Color color) {
        if (item.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(color);
            item.setItemMeta(meta);
        }
        return item;
    }
}