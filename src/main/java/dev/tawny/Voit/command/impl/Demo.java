package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.VoitCommand;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "demo", purpose = "Make a demo screen popup for a player", syntax = "<player>")
public class Demo extends VoitCommand {
    @Override
    protected boolean handle(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);
            assert player != null;
            final PlayerData data = PlayerDataManager.getInstance().getPlayerData(player);
            data.sendDemo(player);
            return true;
        }
        return false;
    }
}