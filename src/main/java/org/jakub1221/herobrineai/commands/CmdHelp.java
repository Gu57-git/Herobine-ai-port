package org.jakub1221.herobrineai.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;

public class CmdHelp extends SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args, HerobrineAI plugin) {
        sender.sendMessage(ChatColor.GOLD + "=== HerobrineAI Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai attack <player>" + ChatColor.WHITE + " - Sets Herobrine's attack target.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai haunt <player>" + ChatColor.WHITE + " - Sets Herobrine's haunt target.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai cancel" + ChatColor.WHITE + " - Cancel actual Herobrine's target.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai reload" + ChatColor.WHITE + " - Reloads config.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai position" + ChatColor.WHITE + " - Gets actual position of Herobrine.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai pyramid <player>" + ChatColor.WHITE + " - Builds a pyramid near the player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai graveyard <player>" + ChatColor.WHITE + " - Teleports player to the Graveyard world.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai bury <player>" + ChatColor.WHITE + " - Bury player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai temple <player>" + ChatColor.WHITE + " - Builds a temple near the player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai heads <player>" + ChatColor.WHITE + " - Spawn some heads near the player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai cave <player>" + ChatColor.WHITE + " - Create cave near the player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai curse <player>" + ChatColor.WHITE + " - Curse the player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai burn <player>" + ChatColor.WHITE + " - Burn the player.");
        sender.sendMessage(ChatColor.YELLOW + "/hb-ai allworlds" + ChatColor.WHITE + " - Add all server worlds to Worlds in config.");
        return true;
    }
}