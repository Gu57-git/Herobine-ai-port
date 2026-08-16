package org.jakub1221.herobrineai.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.Core.CoreType;

public class CmdCancel extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        plugin.getAICore().CancelTarget(CoreType.ANY);
        sender.sendMessage(ChatColor.GREEN + "Target cancelled!");
        return true;
    }
}