package org.jakub1221.herobrineai.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdCave extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hb-ai cave <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target != null) {
            plugin.getAICore().getCore(org.jakub1221.herobrineai.AI.Core.CoreType.BUILD_CAVE).RunCore(new Object[]{target.getLocation()});
            sender.sendMessage(ChatColor.GREEN + "Cave built near " + target.getName());
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found!");
        }
        return true;
    }
}