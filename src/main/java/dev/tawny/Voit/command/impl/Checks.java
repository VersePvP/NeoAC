

package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.VoitCommand;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.PlayerDataManager;
import dev.tawny.Voit.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "checks", syntax = "<player>", purpose = "Get the players registered checks.")
public final class Checks extends VoitCommand {

    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);

            if (player != null) {
                final PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);

                if (playerData != null) {
                    sendLineBreak(sender);
                    sendMessage(sender, ColorUtil.translate("&cThere are " + playerData.getChecks().size() + " registered checks for " + player.getName() + "\n" + " \n"));
                    for (final Check check : playerData.getChecks()) {
                        sendMessage(sender, ColorUtil.translate("&c" + check.getCheckInfo().name() + " (" + check.getCheckInfo().type() + ")"));
                    }
                    sendLineBreak(sender);
                    return true;
                }
            }
        }
        return false;
    }
}
