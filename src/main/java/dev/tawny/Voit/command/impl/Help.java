package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.CommandManager;
import dev.tawny.Voit.command.VoitCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "help", purpose = "Prints every command.")
public class Help extends VoitCommand {
    @Override
    protected boolean handle(CommandSender sender, Command command, String label, String[] args) {
        for (final VoitCommand voitcommand : CommandManager.getInstance().commands) {
            final String commandName = voitcommand.getCommandInfo().name();
            if (commandName.equals(args[0])) {
                    sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "NeoVerse" + ChatColor.GRAY + "]" + " Usage: /NeoVerse " +
                            voitcommand.getCommandInfo().name() + " " +
                            voitcommand.getCommandInfo().syntax());
                return true;
            }
        }
        return false;
    }
}
