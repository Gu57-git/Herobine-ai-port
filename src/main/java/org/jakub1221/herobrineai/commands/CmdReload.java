package org.jakub1221.herobrineai.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdReload extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        plugin.getConfigDB().Reload();
        sender.sendMessage(ChatColor.GREEN + "HerobrineAI config reloaded!");
        return true;
    }
}