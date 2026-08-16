package org.jakub1221.herobrineai.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdGraveyard extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /hb-ai graveyard <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target != null) {
            plugin.getAICore().GraveyardTeleport(target);
            sender.sendMessage(ChatColor.GREEN + target.getName() + " teleported to graveyard!");
        } else {
            sender.sendMessage(ChatColor.RED + "Player not found!");
        }
        return true;
    }
}