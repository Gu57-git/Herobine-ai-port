package org.jakub1221.herobrineai.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdPosition extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        sender.sendMessage(ChatColor.GREEN + "Herobrine position: " + plugin.HerobrineNPC.getBukkitEntity().getLocation());
        return true;
    }
}