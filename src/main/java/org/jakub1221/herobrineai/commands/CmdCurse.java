package org.jakub1221.herobrineai.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdCurse extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hb-ai curse <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target != null) {
            plugin.getAICore().getCore(org.jakub1221.herobrineai.AI.Core.CoreType.CURSE).RunCore(new Object[]{target});
            sender.sendMessage(ChatColor.GREEN + target.getName() + " is cursed!");
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found!");
        }
        return true;
    }
}