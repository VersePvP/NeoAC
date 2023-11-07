package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.VoitCommand;
import dev.tawny.Voit.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@CommandInfo(name = "reload", purpose = "Reloads the configs.")

public class Reload extends VoitCommand {
    @Override
    protected boolean handle(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        p.sendMessage(ChatColor.GRAY + "Reloading Config");
        Config.MAX_VIOLATIONS.clear();
        Config.ENABLED_CHECKS.clear();
        Voit.INSTANCE.getPlugin().reloadConfig();
        Voit.INSTANCE.reloadConfig();
        p.sendMessage(ChatColor.RED + "Successfully reloaded configs! (Experimental)");

        return true;
    }
}