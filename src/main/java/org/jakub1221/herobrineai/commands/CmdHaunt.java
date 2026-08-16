package org.jakub1221.herobrineai.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.CoreResult;

public class CmdHaunt extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hb-ai haunt <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target != null) {
            CoreResult result = plugin.getAICore().setHauntTarget(target);
            if (result.getResult()) {
                sender.sendMessage(ChatColor.GREEN + result.getMessage());
            } else {
                sender.sendMessage(ChatColor.RED + "Failed: " + result.getMessage());
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found!");
        }
        return true;
    }
}