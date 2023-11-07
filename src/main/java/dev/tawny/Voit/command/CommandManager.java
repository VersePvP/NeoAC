package dev.tawny.Voit.command;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.VoitPlugin;
import dev.tawny.Voit.command.impl.*;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.util.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CommandManager implements CommandExecutor {

    public final List<VoitCommand> commands = new ArrayList<>();

    private static CommandManager instance;

    public CommandManager(final VoitPlugin plugin) {
        instance = this;
        commands.add(new Alerts());
        commands.add(new Info());
        commands.add(new Debug());
        commands.add(new Help());
        commands.add(new Ban());
        commands.add(new Checks());
        commands.add(new Exempt());
        commands.add(new Logs());

        commands.add(new Crash());
        commands.add(new Gui());
        commands.add(new KB());
        commands.add(new Scare());
        commands.add(new Reload());
        commands.add(new Demo());

        Collections.sort(commands);
    }


    public static CommandManager getInstance() {
        return instance;
    }
    @Override
    public boolean onCommand(final CommandSender commandSender, final Command command, final String string, final String[] args) {
        if (commandSender.hasPermission("NeoVerse.commands") || commandSender.isOp()) {
            if (args.length > 0) {
                for (final VoitCommand Fox : commands) {
                    final String commandName = Fox.getCommandInfo().name();
                    if (commandName.equals(args[0])) {
                        if (!Fox.handle(commandSender, command, string, args)) {
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.PREFIX) + " Usage: /NeoVerse " +
                                    Fox.getCommandInfo().name() + " " +
                                    Fox.getCommandInfo().syntax());
                        }
                        return true;
                    }
                }
            } else {
                commandSender.sendMessage(ColorUtil.translate("-----------------"));
                commandSender.sendMessage(ColorUtil.translate("             &cAvailable Commands &d»\n" + " \n"));
                for (final VoitCommand voitcommand : commands) {
                    commandSender.sendMessage(ColorUtil.translate("&7/NeoVerse " + voitcommand.getCommandInfo().name() + " &d» &6" + voitcommand.getCommandInfo().purpose()));
                }
                commandSender.sendMessage(ColorUtil.translate("-----------------"));

                return true;
            }
        }
        else {
            commandSender.sendMessage(ColorUtil.translate(Config.PREFIX + "NeoVerse (" + Voit.INSTANCE.getUpdateChecker().getCurrentVersion() + ")"));
            return true;
        }
        return false;
    }
}