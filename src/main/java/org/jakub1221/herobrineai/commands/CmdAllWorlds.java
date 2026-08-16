package org.jakub1221.herobrineai.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdAllWorlds extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        for (World world : Bukkit.getWorlds()) {
            if (!plugin.getConfigDB().useWorlds.contains(world.getName())) {
                plugin.getConfigDB().useWorlds.add(world.getName());
            }
        }
        plugin.getConfigDB().config.set("config.useWorlds", plugin.getConfigDB().useWorlds);
        try {
            plugin.getConfigDB().config.save("plugins/HerobrineAI/config.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.GREEN + "All worlds added to config!");
        return true;
    }
}