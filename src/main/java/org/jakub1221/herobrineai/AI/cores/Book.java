package org.jakub1221.herobrineai.AI.cores;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.Utils;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;
import org.jakub1221.herobrineai.AI.CoreResult;

public class Book extends Core {

    public Book() {
        super(CoreType.BOOK, AppearType.APPEAR, HerobrineAI.getPluginCore());
    }

    public CoreResult CallCore(Object[] data) {
        return setBook((Player) data[0]);
    }

    public CoreResult setBook(Player player) {
        if (PluginCore.getSupport().checkBooks(player.getLocation())) {
            if (PluginCore.getConfigDB().maxBooks > 0) {
                PluginCore.getConfigDB().maxBooks--;
                Location loc = player.getLocation();
                World world = loc.getWorld();
                int x = loc.getBlockX();
                int y = loc.getBlockY();
                int z = loc.getBlockZ();
                Block block = world.getBlockAt(x, y - 1, z);
                if (block.getType() == Material.AIR) {
                    block.setType(Material.BOOKSHELF);
                }
                ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setTitle("Herobrine");
                meta.setAuthor("Herobrine");
                ArrayList<String> pages = new ArrayList<>();
                if (!PluginCore.getConfigDB().useBookMessages.isEmpty()) {
                    int index = Utils.getRandomGen().nextInt(PluginCore.getConfigDB().useBookMessages.size());
                    pages.add(PluginCore.getConfigDB().useBookMessages.get(index));
                } else {
                    pages.add("I am watching you...");
                }
                meta.setPages(pages);
                book.setItemMeta(meta);
                world.dropItemNaturally(loc, book);
                return new CoreResult(true, "Book placed!");
            }
        }
        return new CoreResult(false, "Book not placed.");
    }
}