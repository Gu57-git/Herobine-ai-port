package org.jakub1221.herobrineai.commands;

import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;

public abstract class SubCommand {
    public abstract boolean execute(CommandSender sender, String[] args, HerobrineAI plugin);
}