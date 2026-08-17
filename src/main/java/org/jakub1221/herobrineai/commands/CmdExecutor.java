package org.jakub1221.herobrineai.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jakub1221.herobrineai.HerobrineAI;

import java.util.HashMap;

public class CmdExecutor implements CommandExecutor {
    private HerobrineAI PluginCore;
    private HashMap<String, SubCommand> commands = new HashMap<>();

    public CmdExecutor(HerobrineAI plugin) {
        this.PluginCore = plugin;
        commands.put("help", new CmdHelp());
        commands.put("attack", new CmdAttack());
        commands.put("burn", new CmdBurn());
        commands.put("bury", new CmdBury());
        commands.put("cancel", new CmdCancel());
        commands.put("cave", new CmdCave());
        commands.put("curse", new CmdCurse());
        commands.put("graveyard", new CmdGraveyard());
        commands.put("haunt", new CmdHaunt());
        commands.put("heads", new CmdHeads());
        commands.put("position", new CmdPosition());
        commands.put("pyramid", new CmdPyramid());
        commands.put("reload", new CmdReload());
        commands.put("temple", new CmdTemple());
        commands.put("allworlds", new CmdAllWorlds());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /hb-ai help");
            return true;
        }
        SubCommand sub = commands.get(args[0].toLowerCase());
        if (sub != null) {
            String perm = "herobrineai.command." + args[0].toLowerCase();
            if (!sender.hasPermission(perm)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            return sub.execute(sender, args, PluginCore);
        }
        sender.sendMessage(ChatColor.RED + "Unknown command. Use /hb-ai help");
        return true;
    }
}
